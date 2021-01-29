package com.github.kongwu.recorder.common.model;

public class RequestPacket {
    private byte type;
    private String body;

    public RequestPacket(byte type, String body) {
        this.type = type;
        this.body = body;
    }

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

}
