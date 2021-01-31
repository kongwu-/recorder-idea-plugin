package com.github.kongwu.recorder.plugin.transport;

import com.github.kongwu.recorder.common.model.Packet;
import com.github.kongwu.recorder.common.model.RequestPacket;
import com.github.kongwu.recorder.common.model.ResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class AgentClientHandler extends SimpleChannelInboundHandler<Packet> {

    private AgentTunnel agentTunnel;

    public AgentClientHandler(AgentTunnel agentTunnel) {
        this.agentTunnel = agentTunnel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
        if(packet instanceof ResponsePacket){
            agentTunnel.received((ResponsePacket) packet);
        }
        if(packet instanceof RequestPacket){
            RequestPacket requestPacket = (RequestPacket)packet;
            long id = requestPacket.getId();
            String body = requestPacket.getBody();
            agentTunnel.renderResult(body);
        }

    }
}
