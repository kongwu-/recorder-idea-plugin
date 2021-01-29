package com.github.kongwu.recorder.plugin.command;

import com.github.kongwu.recorder.common.model.RequestPacket;
import io.netty.channel.Channel;


public class AgentTunnel {
    private Channel channel;

    public void sendCommand(RequestPacket requestPacket){
        channel.writeAndFlush(requestPacket);

    }
}
