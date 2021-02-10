package com.github.kongwu.recorder.plugin.agent;

import com.github.kongwu.recorder.common.logger.Logger;
import com.github.kongwu.recorder.plugin.agent.enhance.AgentHolder;
import com.github.kongwu.recorder.plugin.agent.enhance.Enhancer;
import com.github.kongwu.recorder.plugin.agent.transport.AgentServer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class AgentBootstrap {

    private static Logger logger = Logger.getLogger(AgentBootstrap.class);

    public static void premain(String args, Instrumentation instrumentation){
//        AgentServer agentServer = new AgentServer();
//        agentServer.start(Integer.parseInt(args));
        AgentHolder.setInstrumentation(instrumentation);
        System.err.println("pre main......");
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                try {
                    return Enhancer.enhance(loader,className,classfileBuffer);
                } catch (Throwable e) {
//                    e.printStackTrace();
                }
                return classfileBuffer;
            }
        });
//        logger.info("premain inject completed");
    }

    public static void agentmain(String args, Instrumentation instrumentation){
//        AgentServer agentServer = new AgentServer();
//        agentServer.start(Integer.parseInt(args));
        AgentHolder.setInstrumentation(instrumentation);
        logger.info("premain inject completed");
    }
}
