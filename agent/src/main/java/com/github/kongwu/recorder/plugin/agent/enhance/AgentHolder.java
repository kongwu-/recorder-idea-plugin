package com.github.kongwu.recorder.plugin.agent.enhance;

import java.lang.instrument.Instrumentation;

public class AgentHolder {

    private static Instrumentation instrumentation;

    public static Instrumentation getInstrumentation(){
        return instrumentation;
    }

    public static void setInstrumentation(Instrumentation instrumentation){
        AgentHolder.instrumentation = instrumentation;
    }
}
