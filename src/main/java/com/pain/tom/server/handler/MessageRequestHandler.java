package com.pain.tom.server.handler;

import com.pain.tom.protocol.packet.MessageRequestPacket;
import com.pain.tom.protocol.packet.MessageResponsePacket;
import com.pain.tom.protocol.packet.PacketCodeConverter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket requestPacket) throws Exception {


        System.out.println(new Date() + ": 收到客户端消息； " + requestPacket.getMessage());

        MessageResponsePacket responsePacket = new MessageResponsePacket();
        responsePacket.setMessage("服务端回复：【" + requestPacket.getMessage() + "】");
        ctx.channel().writeAndFlush(responsePacket);
    }
}
