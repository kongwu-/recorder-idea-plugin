package com.github.kongwu.recorder.common.model;

public class RequestPacket extends Packet{
    private byte event;

    private String body;

    public RequestPacket(byte event, String body) {
        super(PacketConstant.WAY_REQUEST);
        this.event = event;
        this.body = body;
    }

    public RequestPacket(long id, byte event, String body) {
        super(PacketConstant.WAY_REQUEST,id);
        this.event = event;
        this.body = body;
    }

    public byte getEvent() {
        return event;
    }

    public void setEvent(byte event) {
        this.event = event;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
