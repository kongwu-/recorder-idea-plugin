package com.github.kongwu.recorder.plugin.agent;

import com.alibaba.bytekit.utils.AgentUtils;
import com.github.kongwu.recorder.plugin.agent.enhance.AgentHolder;
import com.github.kongwu.recorder.plugin.agent.transport.AgentServer;

import java.lang.instrument.Instrumentation;

public class AgentServerTest {

    private static final int PORT = 10086;

    public static void main(String[] args) {
        Instrumentation instrumentation = AgentUtils.install();
        AgentHolder.setInstrumentation(instrumentation);

        AgentServer agentServer = new AgentServer();
        agentServer.start(PORT);
        Sample sample = new Sample();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    System.out.println(sample.randomStr(i));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
