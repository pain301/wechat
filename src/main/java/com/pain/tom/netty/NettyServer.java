package com.pain.tom.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class NettyServer {
    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        // 负责处理新的连接
        NioEventLoopGroup boss = new NioEventLoopGroup();

        //负责读取处理连接中的数据
        NioEventLoopGroup worker = new NioEventLoopGroup();

        serverBootstrap
                .group(boss, worker)

                // 指定 io 模型
                .channel(NioServerSocketChannel.class)

                // 给服务端 channel 指定自定义属性
                .attr(AttributeKey.newInstance("serverName"), "nettyServer")
                .attr(AttributeKey.newInstance("version"), "1.0")

                // 给每一条连接指定自定义属性
                .childAttr(AttributeKey.newInstance("clientKey"), "clientKey")

                // 给服务端 channel 设置属性
                .option(ChannelOption.SO_BACKLOG, 1024)

                // 每条连接设置 TCP 底层相关的属性
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)

                // 指定服务启动过程中的逻辑
                .handler(new ChannelInitializer<NioServerSocketChannel>() {
                    @Override
                    protected void initChannel(NioServerSocketChannel nioServerSocketChannel) throws Exception {
                        System.out.println("服务端启动中...");
                    }
                })

                // 指定处理新连接数据的读写处理逻辑
                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new FirstServerHandler());
//                        channel.pipeline().addLast(new StringDecoder());
//                        channel.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
//                            @Override
//                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
//                                System.out.println(s);
//                            }
//                        });
                    }
                });

        bind(serverBootstrap, 8000);
    }

    private static void bind(ServerBootstrap serverBootstrap, int port) {
        serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println(String.format("服务器绑定端口 %d 成功!", port));
                } else {
                    System.out.println(String.format("服务器绑定端口 %d 失败!", port));
                    Thread.sleep(500);
                    bind(serverBootstrap, port + 1);
                }
            }
        });
    }
}