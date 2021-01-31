package com.github.kongwu.recorder.plugin.agent.transport;

import com.github.kongwu.recorder.common.model.PacketConstant;
import com.github.kongwu.recorder.common.model.RequestPacket;
import com.github.kongwu.recorder.common.model.ResponsePacket;
import com.github.kongwu.recorder.plugin.agent.advice.AdviceListenerManager;
import com.github.kongwu.recorder.plugin.agent.advice.TraceAdviceListener;
import com.github.kongwu.recorder.plugin.agent.enhance.Enhancer;
import com.github.kongwu.recorder.plugin.agent.result.DumpTraceResultResolver;
import com.github.kongwu.recorder.plugin.agent.result.TunnelTraceResultResolver;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class AgentServerHandler extends SimpleChannelInboundHandler<RequestPacket> {

    private AgentServer agentServer;

    public AgentServerHandler(AgentServer agentServer) {
        this.agentServer = agentServer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestPacket msg) throws Exception {
        if(msg.getEvent() == PacketConstant.EVENT_SHUTDOWN){
            agentServer.close();
        }
        if(msg.getEvent() == PacketConstant.EVENT_TRACE){
            try {
                Enhancer.enhance(msg.getBody(),true);

                ctx.channel().writeAndFlush(new ResponsePacket(msg.getId(),PacketConstant.STATE_OK,null));
            }catch (ClassNotFoundException e){
                ctx.channel().writeAndFlush(new ResponsePacket(msg.getId(),PacketConstant.STATE_FAIL,msg.getBody() +" Not found in current VM"));
            }

        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        AdviceListenerManager.set(new TraceAdviceListener(new TunnelTraceResultResolver(ctx.channel())));
    }
}
