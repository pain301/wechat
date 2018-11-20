package com.pain.tom.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.Date;

public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 在接收到客户端发来的数据之后被回调
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println(new Date() + ": 服务器端读到的数据 -> " + buf.toString(Charset.forName("utf-8")));

        ByteBuf out = getByteBuf(ctx);
        System.out.println(new Date() + ": 服务端写出数据 -> " + out.toString(Charset.forName("utf-8")));

        ctx.channel().writeAndFlush(out);
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        byte[] bytes = "Hello client".getBytes(Charset.forName("utf-8"));
        ByteBuf buf = ctx.alloc().buffer();
        buf.writeBytes(bytes);
        return buf;
    }
}