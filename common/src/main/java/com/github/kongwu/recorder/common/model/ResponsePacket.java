package com.github.kongwu.recorder.common.model;

public class ResponsePacket {
    private byte state;
    private byte type;
    private String body;

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public ResponsePacket(byte state, byte type, String body) {
        this.state = state;
        this.type = type;
        this.body = body;
    }
}
