package com.github.kongwu.recorder.plugin.agent;

import com.alibaba.bytekit.utils.AgentUtils;
import com.github.kongwu.recorder.plugin.agent.enhance.AgentHolder;
import com.github.kongwu.recorder.plugin.agent.enhance.Enhancer;

import java.lang.instrument.Instrumentation;

public class TraceTest {
    public static void main(String[] args) throws ClassNotFoundException {
        Instrumentation instrumentation = AgentUtils.install();
        AgentHolder.setInstrumentation(instrumentation);

        Enhancer.enhance("com.github.kongwu.recorder.plugin.agent.Sample",true);


    }
}
