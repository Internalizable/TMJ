package me.internalizable.tmj.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.internalizable.tmj.persistence.SocketMessage;

import java.util.ArrayList;
import java.util.List;

public class SocketClientHandler extends SimpleChannelInboundHandler<String> {

    private final Gson gson;
    private final List<Channel> channels;

    public SocketClientHandler() {
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

        for (Channel c : channels) {
            c.writeAndFlush("-> " + msg + '\n');
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Closing connection for client - " + ctx);
        ctx.close();
    }

}
