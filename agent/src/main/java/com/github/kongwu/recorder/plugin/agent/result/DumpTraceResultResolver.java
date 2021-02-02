package com.github.kongwu.recorder.plugin.agent.result;

import com.github.kongwu.recorder.common.model.MethodNode;
import com.github.kongwu.recorder.common.model.ThrowNode;
import com.github.kongwu.recorder.common.model.TraceNode;
import com.github.kongwu.recorder.common.model.TraceTree;

import java.util.List;

public class DumpTraceResultResolver implements TraceResultResolver {
    @Override
    public void resolve(TraceTree traceTree) {
        dump(traceTree);
    }

    private void dump(TraceTree tree){
        TraceNode root = tree.getRoot();
        StringBuilder sb = new StringBuilder();
        recursive("",sb,root);
        System.err.println(sb);
    }

    private void recursive(String prefix,StringBuilder sb,TraceNode node){
        List<TraceNode> children = node.children;
        if(children != null){
            for (TraceNode child : children) {
                if(child instanceof MethodNode){
                    MethodNode methodNode = (MethodNode)child;
                    sb.append(prefix+methodNode.getClassName()+":"+methodNode.getMethodName()+":"+methodNode.getLineNumber()+ (methodNode.getThrow()!=null&&methodNode.getThrow() ?" :throwExp":""));
                    sb.append("\n");
                }else
                if(child instanceof ThrowNode){
                    ThrowNode throwNode = (ThrowNode)child;
                    sb.append(prefix+throwNode.getMessage()+":"+throwNode.getLineNumber());
                    sb.append("\n");
                }
                recursive(prefix+"  ",sb,child);
            }
        }
    }
}
