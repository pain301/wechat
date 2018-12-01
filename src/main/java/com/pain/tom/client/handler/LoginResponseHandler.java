package com.pain.tom.client.handler;

import com.pain.tom.protocol.packet.LoginRequestPacket;
import com.pain.tom.protocol.packet.LoginResponsePacket;
import com.pain.tom.protocol.packet.PacketCodeConverter;
import com.pain.tom.util.LoginUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;
import java.util.UUID;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + ": 客户端开始登录...");

        LoginRequestPacket packet = new LoginRequestPacket();
        packet.setUserId(UUID.randomUUID().toString());
        packet.setUsername("pain");
        packet.setPassword("123456");

        ctx.channel().writeAndFlush(packet);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket responsePacket) throws Exception {

        if (responsePacket.isSuccess()) {
            LoginUtil.markAsLogin(ctx.channel());
            System.out.println(new Date() + ": 客户端登录成功...");
        } else {
            System.out.println(new Date() + ": 客户端登录失败，原因是：" + responsePacket.getReason());
        }
    }
}
