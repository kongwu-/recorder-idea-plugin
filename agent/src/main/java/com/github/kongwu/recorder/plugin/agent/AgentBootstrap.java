package com.github.kongwu.recorder.plugin.agent;

import com.github.kongwu.recorder.common.logger.Logger;
import com.github.kongwu.recorder.plugin.agent.enhance.AgentHolder;
import com.github.kongwu.recorder.plugin.agent.transport.AgentServer;

import java.lang.instrument.Instrumentation;

public class AgentBootstrap {

    private static Logger logger = Logger.getLogger(AgentBootstrap.class);

    public static void premain(String args, Instrumentation instrumentation){
        AgentServer agentServer = new AgentServer();
        agentServer.start(Integer.parseInt(args));
        AgentHolder.setInstrumentation(instrumentation);
        logger.info("premain inject completed");
    }

    public static void agentmain(String args, Instrumentation instrumentation){
        AgentServer agentServer = new AgentServer();
        agentServer.start(Integer.parseInt(args));
        AgentHolder.setInstrumentation(instrumentation);
        logger.info("premain inject completed");
    }
}
