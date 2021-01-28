package com.github.kongwu.recorder.plugin.agent.model;



/**
 * 用于在ThreadLocal中传递的实体
 * @author ralf0131 2017-01-05 14:05.
 */
public class TraceEntity {

    public TraceTree tree;
    public int deep;

    public TraceEntity(ClassLoader loader) {
        this.tree = createTraceTree(loader);
        this.deep = 0;
    }

    public TraceEntity() {
        this.tree = createTraceTree(null);
        this.deep = 0;
    }

    public int getDeep() {
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    private TraceTree createTraceTree(ClassLoader loader) {
        return new TraceTree(new TraceNode("method"));
    }

    public TraceModel getModel() {
        tree.trim();
        return new TraceModel(tree.getRoot(), tree.getNodeCount());
    }
}
