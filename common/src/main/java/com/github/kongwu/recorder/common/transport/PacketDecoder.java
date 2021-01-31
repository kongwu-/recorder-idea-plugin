package com.github.kongwu.recorder.common.transport;

import com.github.kongwu.recorder.common.constant.Constants;
import com.github.kongwu.recorder.common.model.PacketConstant;
import com.github.kongwu.recorder.common.model.RequestPacket;
import com.github.kongwu.recorder.common.model.ResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(in.readableBytes() < 13) return;

        byte way = in.readByte();

        long id = in.readLong();

        int length = in.readInt();

        if(way == PacketConstant.WAY_REQUEST){

            byte event = in.readByte();

            byte[] bodyBytes = new byte[length-1];

            if(in.readableBytes() >= bodyBytes.length){
                in.readBytes(bodyBytes);

                String body = new String(bodyBytes, Constants.ENCODING);
                out.add(new RequestPacket(id,event,body));
            }else{
                in.resetReaderIndex();
            }

        }else{
            //response

            byte state = in.readByte();

            byte[] bodyBytes = new byte[length-1];
            if(in.readableBytes() >= bodyBytes.length){
                in.readBytes(bodyBytes);
                String body = new String(bodyBytes, Constants.ENCODING);
                out.add(new ResponsePacket(id,state,body));
            }else{
                in.resetReaderIndex();
            }
        }
    }
}
