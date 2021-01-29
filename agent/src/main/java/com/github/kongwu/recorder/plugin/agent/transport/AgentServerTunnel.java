package com.github.kongwu.recorder.plugin.agent.transport;

import io.netty.channel.Channel;

public class AgentServerTunnel {
    private static Channel tunnelChannel;

    public static Channel getChannel(){
        return tunnelChannel;
    }
}
