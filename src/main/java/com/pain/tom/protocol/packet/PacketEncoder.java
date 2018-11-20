package com.pain.tom.protocol.packet;

import com.pain.tom.protocol.serialize.JSONSerializer;
import com.pain.tom.protocol.serialize.SerializeAlgorithm;
import com.pain.tom.protocol.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

public class PacketEncoder {

    private static final int MAGIC_NUMBER = 0x12345678;

    private static final Map<Byte, Class<? extends Packet>> PACKET_TYPE_MAP = new HashMap<Byte, Class<? extends Packet>>();
    private static final Map<Byte, Serializer> SERIALIZER_MAP = new HashMap<Byte, Serializer>();

    static {
        PACKET_TYPE_MAP.put(Command.LOGIN_REQUEST, LoginPacket.class);
        SERIALIZER_MAP.put(SerializeAlgorithm.JSON, new JSONSerializer());
    }

    public ByteBuf encode(Packet packet) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.ioBuffer();
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        buf.writeInt(MAGIC_NUMBER);
        buf.writeByte(packet.getVersion());
        buf.writeByte(Serializer.DEFAULT.getSerializeAlgorithm());
        buf.writeByte(packet.getCommand());
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);

        return buf;
    }

    public Packet decode(ByteBuf buf) {
        buf.skipBytes(4);
        buf.skipBytes(1);

        byte serializeAlgorithm = buf.readByte();
        byte command = buf.readByte();
        int len = buf.readInt();

        byte[] bytes = new byte[len];
        buf.readBytes(bytes);

        Class<? extends Packet> clazz = getPacketType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);

        if (clazz != null && serializer != null) {
            return serializer.deserialize(clazz, bytes);
        }

        return null;
    }

    private Class<? extends Packet> getPacketType(byte command) {
        return PACKET_TYPE_MAP.get(command);
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        return SERIALIZER_MAP.get(serializeAlgorithm);
    }
}