package com.github.kongwu.recorder.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MethodDescription implements Serializable {
    private static final long serialVersionUID = -4803677598173857106L;
//    private ClassDescription _classDescription;

    private String className;
    private String methodName;
    private int lineNumber;
    private Boolean isThrow;
    private String throwExp;
    private String mark;

    private List<String> _attributes;
    private List<String> _argNames;
    private List<String> _argTypes;
    private String _returnType;
    private int _hashCode = -1;
    private int hashOfCallStack;
    private int line;
    private boolean highlighted;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Boolean getThrow() {
        return isThrow;
    }

    public void setThrow(Boolean aThrow) {
        isThrow = aThrow;
    }

    public String getThrowExp() {
        return throwExp;
    }

    public void setThrowExp(String throwExp) {
        this.throwExp = throwExp;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public int getHashOfCallStack() {
        return hashOfCallStack;
    }

    public void setHashOfCallStack(int hashOfCallStack) {
        this.hashOfCallStack = hashOfCallStack;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    @Override
    public String toString(){
        return className+"."+methodName+"#"+lineNumber;
    }
}
