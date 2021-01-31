package com.github.kongwu.recorder.common.model;

public class ResponsePacket extends Packet {

    private byte state;

    private String body;


    public ResponsePacket(long id, byte state, String body) {
        super(PacketConstant.WAY_RESPONSE,id);
        this.state = state;
        this.body = body;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
