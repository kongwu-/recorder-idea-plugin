package com.github.kongwu.recorder.common.model;

import java.nio.ByteBuffer;

public class Packet {
    protected byte way;
    protected long id;

    public Packet() {
    }

    public Packet(byte way, long id) {
        this.way = way;
        this.id = id;
    }

    public Packet(byte way) {
        this.way = way;
        this.id = System.currentTimeMillis();
    }

    public byte getWay() {
        return way;
    }

    public void setWay(byte way) {
        this.way = way;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
