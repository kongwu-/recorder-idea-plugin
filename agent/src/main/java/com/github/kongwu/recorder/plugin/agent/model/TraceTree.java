package com.github.kongwu.recorder.plugin.agent.model;



import com.github.kongwu.recorder.plugin.agent.utils.StringUtils;

import java.util.List;

/**
 * Tree model of TraceCommand
 * @author gongdewei 2020/4/28
 */
public class TraceTree {
    private TraceNode root;

    private TraceNode current;
    private int nodeCount = 0;

    public TraceTree(TraceNode root) {
        this.root = root;
        this.current = root;
    }

    public TraceTree() {

    }

    /**
     * Begin a new method call
     * @param className className of method
     * @param methodName method name of the call
     * @param lineNumber line number of invoke point
     * @param isInvoking Whether to invoke this method in other classes
     */
    public void begin(String className, String methodName, int lineNumber, boolean isInvoking) {
        TraceNode child = findChild(current, className, methodName, lineNumber);
        if (child == null) {
            child = new MethodNode(className, methodName, lineNumber, isInvoking);
            current.addChild(child);
        }
        child.begin();
        current = child;
        nodeCount += 1;
    }

    private TraceNode findChild(TraceNode node, String className, String methodName, int lineNumber) {
        List<TraceNode> childList = node.getChildren();
        if (childList != null) {
            //less memory than foreach/iterator
            for (int i = 0; i < childList.size(); i++) {
                TraceNode child = childList.get(i);
                if (matchNode(child, className, methodName, lineNumber)) {
                    return child;
                }
            }
        }
        return null;
    }

    private boolean matchNode(TraceNode node, String className, String methodName, int lineNumber) {
        if (node instanceof MethodNode) {
            MethodNode methodNode = (MethodNode) node;
            if (lineNumber != methodNode.getLineNumber()) return false;
            if (className != null ? !className.equals(methodNode.getClassName()) : methodNode.getClassName() != null) return false;
            return methodName != null ? methodName.equals(methodNode.getMethodName()) : methodNode.getMethodName() == null;
        }
        return false;
    }

    public void end() {
        current.end();
        if (current.parent() != null) {
            //结束之后，将当前节点恢复为上一个，简单的说就是出栈
            current = current.parent();
        }
    }

    public void end(Throwable throwable, int lineNumber) {
//        ThrowNode throwNode = new ThrowNode();
//        throwNode.setException(throwable.getClass().getName());
//        throwNode.setMessage(throwable.getMessage());
//        throwNode.setLineNumber(lineNumber);
//        current.addChild(throwNode);
        this.end(true);
    }

    public void end(boolean isThrow) {
        if (isThrow) {
            current.setMark("throws Exception");
            if (current instanceof MethodNode) {
                MethodNode methodNode = (MethodNode) current;
                methodNode.setThrow(true);
            }
        }
        this.end();
    }

    /**
     * 修整树结点
     */
    public void trim() {
        this.normalizeClassName(root);
    }

    /**
     * 转换标准类名，放在trace结束后统一转换，减少重复操作
     * @param node
     */
    private void normalizeClassName(TraceNode node) {
        if (node instanceof MethodNode) {
            MethodNode methodNode = (MethodNode) node;
            String nodeClassName = methodNode.getClassName();
            String normalizeClassName = StringUtils.normalizeClassName(nodeClassName);
            methodNode.setClassName(normalizeClassName);
        }
        List<TraceNode> children = node.getChildren();
        if (children != null) {
            //less memory fragment than foreach
            for (int i = 0; i < children.size(); i++) {
                TraceNode child = children.get(i);
                normalizeClassName(child);
            }
        }
    }

    public TraceNode getRoot() {
        return root;
    }

    public TraceNode current() {
        return current;
    }

    public int getNodeCount() {
        return nodeCount;
    }
}
