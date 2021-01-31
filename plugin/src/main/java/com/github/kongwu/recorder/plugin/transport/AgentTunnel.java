package com.github.kongwu.recorder.plugin.transport;

import com.github.kongwu.recorder.common.logger.Logger;
import com.github.kongwu.recorder.common.model.RequestPacket;
import com.github.kongwu.recorder.common.model.ResponsePacket;
import com.github.kongwu.recorder.plugin.view.LogResultViewer;
import com.github.kongwu.recorder.plugin.view.ResultViewer;
import com.google.common.util.concurrent.SettableFuture;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.*;


public class AgentTunnel {

    private Logger logger = Logger.getLogger(AgentTunnel.class);

    private Channel channel;

    private int port;

    private static Map<Long, SettableFuture<ResponsePacket>> FUTURES = new ConcurrentHashMap<>();

    private ResultViewer resultViewer;

    public AgentTunnel(int port) {
        this.port = port;
        this.resultViewer = new LogResultViewer();
    }

    public synchronized void initializeAgentClient(){
        if(channel == null){
            AgentClient agentClient = new AgentClient(this);
            channel = agentClient.connect(port);
        }
    }

    public ResponsePacket sendCommand(RequestPacket requestPacket){
        channel.writeAndFlush(requestPacket);
        SettableFuture<ResponsePacket> future = SettableFuture.create();
        FUTURES.put(requestPacket.getId(),future);
        try {
            ResponsePacket responsePacket = future.get();
            logger.info("send command successful!");
            return responsePacket;
        } catch (Throwable e) {
            logger.info("get result timeout!",e);
        }
        return null;
    }

    public void received(ResponsePacket responsePacket){
        SettableFuture<ResponsePacket> responsePacketFuture = FUTURES.get(responsePacket.getId());
        responsePacketFuture.set(responsePacket);
    }

    public ResultViewer getResultViewer() {
        return resultViewer;
    }

    public void setResultViewer(ResultViewer resultViewer) {
        this.resultViewer = resultViewer;
    }

    public void renderResult(String body) {
        this.resultViewer.draw(body);
    }
}
