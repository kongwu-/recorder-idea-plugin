package com.github.kongwu.recorder.plugin.agent.advice;

import com.github.kongwu.recorder.plugin.agent.result.TunnelTraceResultResolver;
import io.netty.channel.Channel;

public class AdviceListenerManager {

    private static AdviceListener adviceListener;

    public static AdviceListener getAdviceListener(){
        return adviceListener;
    }

    public static void set(AdviceListener adviceListener){
        AdviceListenerManager.adviceListener = adviceListener;
    }

    public static void inject(Channel channel){
        TraceAdviceListener traceAdviceListener = new TraceAdviceListener();
        traceAdviceListener.setTraceResultResolver(new TunnelTraceResultResolver(channel));
        AdviceListenerManager.adviceListener = traceAdviceListener;
    }
}
