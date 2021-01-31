package com.github.kongwu.recorder.plugin.agent.model;

import com.github.kongwu.recorder.common.model.InvokeStack;
import com.github.kongwu.recorder.common.model.MethodDescription;

import java.util.ArrayList;
import java.util.List;

public class TraceResultConverter {
    public InvokeStack convert(TraceTree traceTree){
        TraceNode root = traceTree.getRoot();

        InvokeStack rootStack = new InvokeStack();
        recursiveChildren(rootStack,root);
        return rootStack;
    }

    private MethodDescription createMethodDesc(TraceNode node){
        if(node instanceof MethodNode){
            MethodNode methodNode = (MethodNode) node;
            MethodDescription methodDescription = new MethodDescription();
            methodDescription.setMethodName(methodNode.getMethodName());
            methodDescription.setClassName(methodNode.getClassName());
            methodDescription.setLineNumber(methodNode.getLineNumber());
            methodDescription.setMark(methodNode.getMark());
            methodDescription.setThrow(methodNode.getThrow());
            return methodDescription;
        }
        return null;
    }

    private void recursiveChildren(InvokeStack invokeStack,TraceNode node){

        MethodDescription methodDesc = createMethodDesc(node);
        invokeStack.setMethodDescription(methodDesc);

        List<TraceNode> children = node.children;

        List<InvokeStack> invokeStackList = new ArrayList<>();
        if(children != null){
            for (TraceNode child : children) {
                InvokeStack childStack = new InvokeStack();
                recursiveChildren(childStack,child);
                invokeStackList.add(childStack);
            }
        }
        invokeStack.setChildren(invokeStackList);
    }
}
