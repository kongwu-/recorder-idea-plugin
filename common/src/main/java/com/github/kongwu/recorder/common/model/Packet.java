package com.github.kongwu.recorder.common.model;

import java.nio.ByteBuffer;

public class Packet {
    private int length;
    private ByteBuffer data;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public ByteBuffer getData() {
        return data;
    }

    public void setData(ByteBuffer data) {
        this.data = data;
    }
}
