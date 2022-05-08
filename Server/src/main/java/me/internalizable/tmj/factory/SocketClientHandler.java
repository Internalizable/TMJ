package me.internalizable.tmj.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.internalizable.tmj.ServerCore;
import me.internalizable.tmj.accounts.TMJAccount;
import me.internalizable.tmj.requests.AuthObject;
import me.internalizable.tmj.requests.SocketMessage;
import me.internalizable.tmj.responses.SocketResponse;
import me.internalizable.tmj.responses.auth.AuthMessageHistory;
import me.internalizable.tmj.responses.auth.AuthResponse;

import java.util.ArrayList;
import java.util.List;

public class SocketClientHandler extends SimpleChannelInboundHandler<String> {

    private final ServerCore serverCore;
    private final Gson gson;
    private final List<Channel> channels;

    public SocketClientHandler(ServerCore serverCore) {

        this.serverCore = serverCore;

        this.gson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();

        this.channels = new ArrayList<>();
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        System.out.println("Client joined - " + ctx);
        channels.add(ctx.channel());
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {

        SocketMessage socketMessage = gson.fromJson(msg, SocketMessage.class);

        if(socketMessage == null) // Not a socket message, discard.
            return;

        if(socketMessage.getObject() instanceof AuthObject authObject) { // This is an authentication object.
            TMJAccount tmjAccount = null;

            if(!authObject.isLogin()) // Creation of a new account
                tmjAccount = serverCore.getMongoStore().createAccount(authObject.getUsername(), authObject.getPassword());
            else
                tmjAccount = serverCore.getMongoStore().login(authObject.getUsername(), authObject.getPassword());

            buildAuthResponse(ctx, tmjAccount);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Closing connection for client - " + ctx);
        ctx.close();
    }

    private void buildAuthResponse(ChannelHandlerContext ctx, TMJAccount tmjAccount) {
        if(tmjAccount == null) {
            SocketResponse socketResponse = SocketResponse.builder()
                    .responseCode(500).response(null).build();

            ctx.writeAndFlush(gson.toJson(socketResponse));
            return;
        }

        SocketResponse socketResponse = SocketResponse.builder()
                .responseCode(200).response(AuthResponse.builder()
                        .uuid(tmjAccount.getId())
                        .authMessageHistoryList(tmjAccount.getMessageHistoryList()
                                .stream()
                                .map(messageHistory ->
                                        AuthMessageHistory.builder()
                                                .message(messageHistory.getMessage())
                                                .receiverId(messageHistory.getReceiverId())
                                                .timestamp(messageHistory.getTimestamp()).build()).toList()))
                .build();

        ctx.writeAndFlush(gson.toJson(socketResponse));
    }
}
