package com.pain.tom.protocol.packet;

public class MessageRequestPacket extends Packet {

    private String message;

    MessageRequestPacket() {
        super();
    }

    public MessageRequestPacket(String message) {
        super();
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
