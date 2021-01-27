package com.github.kongwu;

import java.util.List;

public class TraceAdviceListener implements AdviceListener{

    protected final ThreadLocalWatch threadLocalWatch = new ThreadLocalWatch();

    protected final ThreadLocal<TraceEntity> threadBoundEntity = new ThreadLocal<TraceEntity>();

    @Override
    public long id() {
        return 0;
    }

    @Override
    public void create() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void before(Class<?> clazz, String methodName, String methodDesc, Object target, Object[] args) throws Throwable {
        TraceEntity traceEntity = threadLocalTraceEntity();

        traceEntity.deep++;
        traceEntity.tree.begin(clazz.getName(), methodName, -1, false);

        // 开始计算本次方法调用耗时
        threadLocalWatch.start();
    }

    @Override
    public void afterReturning(Class<?> clazz, String methodName, String methodDesc, Object target, Object[] args, Object returnObject) throws Throwable {
        System.out.println("after..."+threadLocalTraceEntity().deep);
        threadLocalTraceEntity().tree.end();
        finishing();
    }

    @Override
    public void afterThrowing(Class<?> clazz, String methodName, String methodDesc, Object target, Object[] args, Throwable throwable) throws Throwable {
        int lineNumber = throwable.getStackTrace()[0].getLineNumber();
        threadLocalTraceEntity().tree.end(throwable, lineNumber);
        finishing();
    }

    @Override
    public void invokeBeforeTracing(String tracingClassName, String tracingMethodName, String tracingMethodDesc, int tracingLineNumber) throws Throwable {
        TraceEntity traceEntity = threadLocalTraceEntity();

        traceEntity.deep++;
        traceEntity.tree.begin(tracingClassName, tracingMethodName, tracingLineNumber, false);

        // 开始计算本次方法调用耗时
        threadLocalWatch.start();
    }

    @Override
    public void invokeThrowTracing(String tracingClassName, String tracingMethodName, String tracingMethodDesc, int tracingLineNumber, Throwable throwable) throws Throwable {
        System.out.println("invokeThrowTracing..."+threadLocalTraceEntity().deep);
        int lineNumber = throwable.getStackTrace()[0].getLineNumber();
        threadLocalTraceEntity().tree.end(throwable,lineNumber);
        finishing();
    }

    @Override
    public void invokeAfterTracing(String tracingClassName, String tracingMethodName, String tracingMethodDesc, int tracingLineNumber) throws Throwable {
        System.out.println("invokeThrowTracing..."+threadLocalTraceEntity().deep);
        threadLocalTraceEntity().tree.end();
        finishing();
    }


    protected TraceEntity threadLocalTraceEntity() {
        TraceEntity traceEntity = threadBoundEntity.get();
        if (traceEntity == null) {
            traceEntity = new TraceEntity();
            threadBoundEntity.set(traceEntity);
        }
        return traceEntity;
    }


    private void finishing() {
        // 本次调用的耗时
        TraceEntity traceEntity = threadLocalTraceEntity();
        double cost = threadLocalWatch.costInMillis();
        //通过深度来判断，是否完全退出trace的入口方法
        if (--traceEntity.deep == 0) {
            try {
                dump();
            } catch (Throwable e) {
                e.printStackTrace();
//                logger.warn("trace failed.", e);
//                process.end(1, "trace failed, condition is: " + command.getConditionExpress() + ", " + e.getMessage()
//                        + ", visit " + LogUtil.loggingFile() + " for more details.");
            } finally {
                threadBoundEntity.remove();
            }
        }
    }

    private void dump(){
        TraceEntity traceEntity = threadLocalTraceEntity();
        TraceTree tree = traceEntity.tree;
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
