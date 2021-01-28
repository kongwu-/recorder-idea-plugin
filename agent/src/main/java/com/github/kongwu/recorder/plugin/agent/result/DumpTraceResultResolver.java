package com.github.kongwu.recorder.plugin.agent.result;

import com.github.kongwu.recorder.plugin.agent.model.*;

import java.util.List;

public class DumpTraceResultResolver implements TraceResultResolver {
    @Override
    public void resolve(TraceTree traceTree) {
        dump(traceTree);
    }

    private void dump(TraceTree tree){
        TraceNode root = tree.getRoot();
        recursive("",root);
    }

    private void recursive(String prefix,TraceNode node){
        List<TraceNode> children = node.children;
        if(children != null){
            for (TraceNode child : children) {
                if(child instanceof MethodNode){
                    MethodNode methodNode = (MethodNode)child;
                    System.out.println(prefix+methodNode.getClassName()+":"+methodNode.getMethodName()+":"+methodNode.getLineNumber()+ (methodNode.getThrow()!=null&&methodNode.getThrow() ?" :throwExp":""));
                }else
                if(child instanceof ThrowNode){
                    ThrowNode throwNode = (ThrowNode)child;
                    System.out.println(prefix+throwNode.getMessage()+":"+throwNode.getLineNumber());
                }else{

                }
                recursive(prefix+"  ",child);
            }
        }
    }
}
