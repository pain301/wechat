package com.pain.tom.protocol.packet;

public abstract class Packet {

    /**
     * 协议版本
     */
    private byte version = 1;

    public abstract byte getCommand();

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }
}
