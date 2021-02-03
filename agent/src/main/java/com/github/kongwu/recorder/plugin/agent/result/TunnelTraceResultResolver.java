package com.github.kongwu.recorder.plugin.agent.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.github.kongwu.recorder.common.logger.Logger;
import com.github.kongwu.recorder.common.model.PacketConstant;
import com.github.kongwu.recorder.common.model.RequestPacket;
import com.github.kongwu.recorder.plugin.agent.model.TraceResultConverter;
import com.github.kongwu.recorder.common.model.TraceTree;
import io.netty.channel.Channel;

public class TunnelTraceResultResolver implements TraceResultResolver{

//    private ObjectMapper objectMapper = new ObjectMapper();

    private Logger logger = Logger.getLogger(TunnelTraceResultResolver.class);

    private Channel channel;

    private TraceResultConverter traceResultConverter = new TraceResultConverter();

    public TunnelTraceResultResolver(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void resolve(TraceTree traceTree) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder().build();
            objectMapper.activateDefaultTyping(ptv); // default to using DefaultTyping.OBJECT_AND_NON_CONCRETE
            objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);


            logger.info("resolving trace result");
            String body = objectMapper.writeValueAsString(traceTree.getRoot());
            channel.writeAndFlush(new RequestPacket(PacketConstant.EVENT_TRACE,body));
            //ignore reply...
        } catch (Exception e) {
            logger.error("resolve result failed!",e);
        }
    }
}
