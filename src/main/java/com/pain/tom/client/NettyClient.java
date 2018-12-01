package com.pain.tom.client;

import com.pain.tom.client.handler.LoginResponseHandler;
import com.pain.tom.client.handler.MessageResponseHandler;
import com.pain.tom.handler.*;
import com.pain.tom.protocol.packet.MessageRequestPacket;
import com.pain.tom.protocol.packet.PacketCodeConverter;
import com.pain.tom.util.LoginUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NettyClient {

    private static final int MAX_RETRY = 5;

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap
                // 指定线程模型
                .group(group)

                // 给客户端 Channel 绑定自定义属性
                .attr(AttributeKey.newInstance("clientName"), "nettyClient")

                // 给连接设置 TCP 底层相关的属性
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)

                // 指定 IO 类型
                .channel(NioSocketChannel.class)

                // IO 处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        // 添加逻辑处理器
//                        channel.pipeline().addLast(new StringEncoder());
//                        channel.pipeline().addLast(new FirstClientHandler());
//                        channel.pipeline().addLast(new ClientHandler());
                        channel.pipeline().addLast(new Spliter());
                        channel.pipeline().addLast(new PacketDecoder());
                        channel.pipeline().addLast(new LoginResponseHandler());
                        channel.pipeline().addLast(new MessageResponseHandler());
                        channel.pipeline().addLast(new PacketEncoder());
                    }
                });

        connect(bootstrap, "127.0.0.1", 8000, MAX_RETRY);
    }

    private static void connect(final Bootstrap bootstrap, String host, int port, final int retryTimes) {
        bootstrap.connect(host, port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("客户端连接成功!");
                    Channel channel = ((ChannelFuture) future).channel();
                    startConsoleThread(channel);
                } else {
                    System.out.println("客户端连接失败!");

                    if (retryTimes == 0) {
                        throw new RuntimeException("Out of retry times!");
                    } else {
                        int delay = 1 << (MAX_RETRY - retryTimes + 1);
                        bootstrap
                                .config()
                                .group()
                                .schedule(() -> connect(bootstrap, host, port, retryTimes - 1), delay, TimeUnit.SECONDS);
                    }
                }
            }
        });
    }

    private static void startConsoleThread(Channel channel) {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (LoginUtil.hasLogin(channel)) {
                    System.out.println("输入消息发送到客户端：");

                    Scanner scanner = new Scanner(System.in);
                    String line = scanner.nextLine();

                    for (int i = 0; i < 10; ++i) {
                        MessageRequestPacket packet = new MessageRequestPacket();
                        packet.setMessage(line);
                        channel.writeAndFlush(packet);
                    }
                }
            }
        }).start();
    }
}
