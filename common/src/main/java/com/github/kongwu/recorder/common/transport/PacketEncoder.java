package com.github.kongwu.recorder.common.transport;

import com.github.kongwu.recorder.common.constant.Constants;
import com.github.kongwu.recorder.common.model.Packet;
import com.github.kongwu.recorder.common.model.RequestPacket;
import com.github.kongwu.recorder.common.model.ResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf buffer) throws Exception {
        buffer.writeByte(packet.getWay());
        buffer.writeLong(packet.getId());

        if(packet instanceof RequestPacket){

            RequestPacket requestPacket = (RequestPacket) packet;
            byte[] bodyBytes = requestPacket.getBody().getBytes(Constants.ENCODING);
            int length = bodyBytes.length + 1;

            buffer.writeInt(length);
            buffer.writeByte(requestPacket.getEvent());
            buffer.writeBytes(bodyBytes);

        }else if(packet instanceof ResponsePacket){

            ResponsePacket responsePacket = (ResponsePacket) packet;
            String body = responsePacket.getBody();
            byte[] bodyBytes = null;
            int bodyLength = 0;

            if(body != null){
                bodyBytes = responsePacket.getBody().getBytes(Constants.ENCODING);
                bodyLength = bodyBytes.length;
            }
            int length = bodyLength + 1;

            buffer.writeInt(length);
            buffer.writeByte(responsePacket.getState());

            if(bodyBytes != null){
                buffer.writeBytes(bodyBytes);
            }
        }
    }
}
