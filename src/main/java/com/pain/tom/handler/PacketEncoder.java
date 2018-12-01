package com.pain.tom.handler;

import com.pain.tom.protocol.packet.Packet;
import com.pain.tom.protocol.packet.PacketCodeConverter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf buf) throws Exception {
        PacketCodeConverter.INSTANCE.encode(packet, buf);
    }
}
