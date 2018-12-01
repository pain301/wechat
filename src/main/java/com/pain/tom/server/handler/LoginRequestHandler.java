package com.pain.tom.server.handler;

import com.pain.tom.protocol.packet.LoginRequestPacket;
import com.pain.tom.protocol.packet.LoginResponsePacket;
import com.pain.tom.protocol.packet.PacketCodeConverter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket requestPacket) throws Exception {

        System.out.println(new Date() + ": 客户端开始登录。。。");

        LoginResponsePacket responsePacket = new LoginResponsePacket();
        responsePacket.setVersion(requestPacket.getVersion());

        if (isValid(requestPacket)) {
            responsePacket.setSuccess(true);
            System.out.println(new Date() + ": 客户端登录成功。。。");
        } else {
            responsePacket.setReason("账号密码校验失败");
            responsePacket.setSuccess(true);
            System.out.println(new Date() + ": 客户端登录失败。。。");
        }

        ctx.channel().writeAndFlush(responsePacket);
    }

    private boolean isValid(LoginRequestPacket requestPacket) {
        return true;
    }
}
