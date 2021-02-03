package com.github.kongwu.recorder.plugin.agent;

import com.alibaba.bytekit.utils.AgentUtils;
import com.github.kongwu.recorder.plugin.agent.advice.AdviceListenerManager;
import com.github.kongwu.recorder.plugin.agent.advice.TraceAdviceListener;
import com.github.kongwu.recorder.plugin.agent.enhance.AgentHolder;
import com.github.kongwu.recorder.plugin.agent.enhance.Enhancer;
import com.github.kongwu.recorder.plugin.agent.result.DumpTraceResultResolver;

import java.lang.instrument.Instrumentation;

public class TraceTest {
    public static void main(String[] args) throws ClassNotFoundException {
        Instrumentation instrumentation = AgentUtils.install();
        AdviceListenerManager.set(new TraceAdviceListener(new DumpTraceResultResolver()));
        AgentHolder.setInstrumentation(instrumentation);



        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    Sample sample = new Sample();
                    System.out.println(sample.randomStr(10));
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        Enhancer.enhance("com.github.kongwu.recorder.plugin.agent.Sample",true);


    }
}
