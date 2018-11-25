package com.pain.tom.handler;

import com.pain.tom.protocol.packet.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(new Date() + ": 客户端开始登录。。。");
        ByteBuf buf = (ByteBuf) msg;

        Packet packet = PacketEncoder.INSTANCE.decode(buf);

        if (packet instanceof LoginRequestPacket) {
            System.out.println("Valid request...");
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(loginRequestPacket.getVersion());

            if (isValid(loginRequestPacket)) {
                loginResponsePacket.setSuccess(true);
            } else {
                loginResponsePacket.setReason("账号密码校验失败");
                loginResponsePacket.setSuccess(false);
            }

            ByteBuf responseBuf = PacketEncoder.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
            ctx.channel().writeAndFlush(responseBuf);
        } else if (packet instanceof MessageRequestPacket) {
            System.out.println("服务端接收消息。。。");
            MessageRequestPacket requestPacket = (MessageRequestPacket) packet;
            System.out.println(new Date() + ": 收到客户端消息； " + requestPacket.getMessage());

            MessageResponsePacket responsePacket = new MessageResponsePacket();
            responsePacket.setMessage("服务端回复：【" + requestPacket.getMessage() + "】");
            ByteBuf responseBuf = PacketEncoder.INSTANCE.encode(ctx.alloc(), responsePacket);
            ctx.channel().writeAndFlush(responseBuf);
        } else {
            System.out.println("Invalid request...");
        }
    }

    private boolean isValid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
