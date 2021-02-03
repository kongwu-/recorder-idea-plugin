package com.github.kongwu.recorder.plugin.agent.result;

import com.github.kongwu.recorder.common.model.*;

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
        append(prefix,sb,node);
        List<TraceNode> children = node.children;
        if(children != null){
            for (TraceNode child : children) {
                recursive(prefix+"  ",sb,child);
            }
        }
    }

    private void append(String prefix,StringBuilder sb,TraceNode child){
        if(child instanceof ThreadNode){
            ThreadNode threadNode = (ThreadNode)child;
            sb.append(prefix+threadNode.getThreadName()+":"+threadNode.getTimestamp().getTime());
            sb.append("\n");
        }else
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
    }
}
