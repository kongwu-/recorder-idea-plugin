package com.github.kongwu.recorder.plugin.agent.result;

import com.github.kongwu.recorder.common.model.TraceTree;

public interface TraceResultResolver {
    void resolve(TraceTree traceTree);
}
