package com.github.kongwu.recorder.plugin.agent.transport;

import com.github.kongwu.recorder.common.constant.Constants;
import com.github.kongwu.recorder.common.model.ResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ResponseEncoder extends MessageToByteEncoder<ResponsePacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ResponsePacket msg, ByteBuf out) throws Exception {
        byte state = msg.getType();
        byte type = msg.getType();
        String body = msg.getBody();
        out.writeByte(state);
        out.writeByte(type);
        out.writeBytes(body.getBytes(Constants.ENCODING));
    }
}
