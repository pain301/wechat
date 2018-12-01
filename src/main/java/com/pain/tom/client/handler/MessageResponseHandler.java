package com.pain.tom.client.handler;

import com.pain.tom.protocol.packet.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket responsePacket) throws Exception {
        System.out.println(new Date() + ": 收到服务端消息：" + responsePacket.getMessage());
    }
}
