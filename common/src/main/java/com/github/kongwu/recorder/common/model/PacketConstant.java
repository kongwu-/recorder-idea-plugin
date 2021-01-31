package com.github.kongwu.recorder.common.model;

public class PacketConstant {
    /* request */

    public static final byte WAY_REQUEST = 0x01;
    public static final byte WAY_RESPONSE = 0x02;

    public static final byte EVENT_TRACE = 0x01;
    public static final byte EVENT_SHUTDOWN = 0x02;

    public static final byte STATE_OK = 1;

    public static final byte STATE_FAIL = 0;

    /* response */
    public static final byte RESPONSE_TRACE = 1;
}
