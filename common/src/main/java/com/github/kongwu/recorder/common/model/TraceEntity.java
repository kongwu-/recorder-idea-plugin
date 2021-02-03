package com.github.kongwu.recorder.common.model;



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
        return new TraceTree(getThreadNode(loader,Thread.currentThread()));
    }

    public static ThreadNode getThreadNode(ClassLoader loader, Thread currentThread) {
        ThreadNode threadNode = new ThreadNode();
        threadNode.setThreadId(currentThread.getId());
        threadNode.setThreadName(currentThread.getName());
        threadNode.setDaemon(currentThread.isDaemon());
        threadNode.setPriority(currentThread.getPriority());
//        threadNode.setClassloader(getTCCL(currentThread));

        //trace_id
//        StackModel stackModel = new StackModel();
//        getEagleeyeTraceInfo(loader, currentThread, stackModel);
//        threadNode.setTraceId(stackModel.getTraceId());
//        threadNode.setRpcId(stackModel.getRpcId());
        return threadNode;
    }

    public TraceModel getModel() {
        tree.trim();
        return new TraceModel(tree.getRoot(), tree.getNodeCount());
    }
}
