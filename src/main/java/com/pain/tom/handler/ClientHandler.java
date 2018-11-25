package com.pain.tom.handler;

import com.pain.tom.protocol.packet.LoginRequestPacket;
import com.pain.tom.protocol.packet.LoginResponsePacket;
import com.pain.tom.protocol.packet.Packet;
import com.pain.tom.protocol.packet.PacketEncoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.UUID;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        System.out.println(new Date() + ": 客户端开始登录...");

        LoginRequestPacket packet = new LoginRequestPacket();
        packet.setUserId(UUID.randomUUID().toString());
        packet.setUsername("pain");
        packet.setPassword("123456");

        ByteBuf buf = PacketEncoder.INSTANCE.encode(ctx.alloc(), packet);
        ctx.channel().writeAndFlush(buf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        Packet packet = PacketEncoder.INSTANCE.decode(buf);

        if (packet instanceof LoginResponsePacket) {
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;

            if (loginResponsePacket.isSuccess()) {
                System.out.println(new Date() + ": 客户端登录成功...");
            } else {
                System.out.println(new Date() + ": 客户端登录失败，原因是：" + loginResponsePacket.getReason());
            }
        } else {
            System.out.println("Invalid request...");
        }
    }
}
