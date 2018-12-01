package com.pain.tom.protocol.packet;

import com.alibaba.fastjson.annotation.JSONField;

public abstract class Packet {

    /**
     * 协议版本
     */
    @JSONField(deserialize = false, serialize = false)
    private byte version = 1;

    @JSONField(serialize = false)
    public abstract Byte getCommand();

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }
}
