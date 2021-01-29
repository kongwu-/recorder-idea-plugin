package com.github.kongwu.recorder.plugin.agent.transport;

import com.github.kongwu.recorder.common.model.PacketType;
import com.github.kongwu.recorder.common.model.RequestPacket;
import com.github.kongwu.recorder.plugin.agent.advice.AdviceListenerManager;
import com.github.kongwu.recorder.plugin.agent.advice.TraceAdviceListener;
import com.github.kongwu.recorder.plugin.agent.enhance.Enhancer;
import com.github.kongwu.recorder.plugin.agent.result.DumpTraceResultResolver;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class AgentServerHandler extends SimpleChannelInboundHandler<RequestPacket> {

    private AgentServer agentServer;

    public AgentServerHandler(AgentServer agentServer) {
        this.agentServer = agentServer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestPacket msg) throws Exception {
        if(msg.getType() == PacketType.REQUEST_SHUTDOWN){
            agentServer.close();
        }
        if(msg.getType() == PacketType.REQUEST_TRACE){
            Enhancer.enhance(msg.getBody(),true);
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        AdviceListenerManager.set(new TraceAdviceListener(new DumpTraceResultResolver()));
    }
}
