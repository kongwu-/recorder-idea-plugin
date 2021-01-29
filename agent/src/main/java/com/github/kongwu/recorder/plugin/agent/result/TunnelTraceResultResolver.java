package com.github.kongwu.recorder.plugin.agent.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kongwu.recorder.common.logger.Logger;
import com.github.kongwu.recorder.common.model.PacketType;
import com.github.kongwu.recorder.common.model.ResponsePacket;
import com.github.kongwu.recorder.plugin.agent.model.TraceTree;
import io.netty.channel.Channel;

public class TunnelTraceResultResolver implements TraceResultResolver{

    private ObjectMapper objectMapper = new ObjectMapper();

    private Logger logger = Logger.getLogger(TunnelTraceResultResolver.class);

    private Channel channel;

    public TunnelTraceResultResolver(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void resolve(TraceTree traceTree) {
        try {
            String body = objectMapper.writeValueAsString(traceTree);
            channel.writeAndFlush(new ResponsePacket(PacketType.RESPONSE_STATE_OK,PacketType.RESPONSE_TRACE,body));
        } catch (Exception e) {
            logger.info("resolve result failed!",e);
        }
    }
}
