package me.internalizable.tmj;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.Getter;
import lombok.SneakyThrows;
import me.internalizable.tmj.factory.SocketClientHandler;
import me.internalizable.tmj.mongo.MongoStore;

public class ServerCore {

    private static final int SERVER_PORT = 6000;

    @Getter
    private final MongoStore mongoStore;

    public ServerCore() {
        this.mongoStore = new MongoStore();
    }

    @SneakyThrows
    public static void main(String[] args) {
        ServerCore serverCore = new ServerCore();

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new StringDecoder());
                            p.addLast(new StringEncoder());

                            p.addLast(new SocketClientHandler(serverCore));
                        }
                    });

            ChannelFuture f = b.bind(SERVER_PORT).sync();
            System.out.println("Chat Server started. Ready to accept chat clients.");

            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
