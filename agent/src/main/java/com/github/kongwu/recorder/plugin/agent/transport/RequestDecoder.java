package com.github.kongwu.recorder.plugin.agent.transport;

import com.github.kongwu.recorder.common.constant.Constants;
import com.github.kongwu.recorder.common.model.RequestPacket;
import com.github.kongwu.recorder.plugin.agent.advice.AdviceListenerManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RequestDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte type = in.readByte();
        int length = in.readableBytes();
        byte[] bodyBytes = new byte[length];
        in.readBytes(bodyBytes);
        String body = new String(bodyBytes, Constants.ENCODING);
        out.add(new RequestPacket(type,body));
    }
}
