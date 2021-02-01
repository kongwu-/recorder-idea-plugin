package com.github.kongwu.recorder.plugin;

import com.github.kongwu.recorder.common.model.PacketConstant;
import com.github.kongwu.recorder.common.model.RequestPacket;
import com.github.kongwu.recorder.plugin.transport.AgentTunnel;

public class AgentTunnelTest {

    private static final int PORT = 10086;

    public static void main(String[] args) {
        AgentTunnel agentTunnel = new AgentTunnel(PORT, null);

        agentTunnel.initializeAgentClient();

//        agentTunnel.sendCommand(new RequestPacket(PacketConstant.EVENT_TRACE,"com.github.kongwu.recorder.plugin.agent.Sample"));
        agentTunnel.sendCommand(new RequestPacket(PacketConstant.EVENT_TRACE,"com.github.kongwu.recorder.plugin.agent.Sample"));
    }
}
