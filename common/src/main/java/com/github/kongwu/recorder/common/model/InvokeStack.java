package com.github.kongwu.recorder.common.model;

import java.util.List;

public class InvokeStack {

    private MethodDescription methodDescription;

    private List<InvokeStack> children;

    public InvokeStack() {
    }

    public InvokeStack(MethodDescription methodDescription, List<InvokeStack> children) {
        this.methodDescription = methodDescription;
        this.children = children;
    }

    public MethodDescription getMethodDescription() {
        return methodDescription;
    }

    public void setMethodDescription(MethodDescription methodDescription) {
        this.methodDescription = methodDescription;
    }

    public List<InvokeStack> getChildren() {
        return children;
    }

    public void setChildren(List<InvokeStack> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        if(methodDescription == null){
            return "root";
        }else{
            return methodDescription.toString();
        }
    }
}
