package com.github.kongwu.recorder.plugin.agent.advice;

import com.github.kongwu.recorder.common.logger.Logger;
import com.github.kongwu.recorder.common.model.TraceEntity;
import com.github.kongwu.recorder.plugin.agent.result.DumpTraceResultResolver;
import com.github.kongwu.recorder.plugin.agent.result.TraceResultResolver;
import com.github.kongwu.recorder.plugin.agent.utils.ThreadLocalWatch;

public class TraceAdviceListener implements AdviceListener{

    private static Logger logger = Logger.getLogger(TraceAdviceListener.class);

    protected final ThreadLocalWatch threadLocalWatch = new ThreadLocalWatch();

    protected final ThreadLocal<TraceEntity> threadBoundEntity = new ThreadLocal<TraceEntity>();

    protected TraceResultResolver traceResultResolver;

    public TraceResultResolver getTraceResultResolver() {
        return traceResultResolver;
    }

    public void setTraceResultResolver(TraceResultResolver traceResultResolver) {
        this.traceResultResolver = traceResultResolver;
    }

    public TraceAdviceListener() {
        this.traceResultResolver = new DumpTraceResultResolver();
    }

    public TraceAdviceListener(TraceResultResolver traceResultResolver) {
        this.traceResultResolver = traceResultResolver;
    }

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
        logger.info("invokeBeforeTracing: "+tracingMethodName);
        TraceEntity traceEntity = threadLocalTraceEntity();

        traceEntity.deep++;
        traceEntity.tree.begin(tracingClassName, tracingMethodName, tracingLineNumber, false);

        // 开始计算本次方法调用耗时
        threadLocalWatch.start();
    }

    @Override
    public void invokeThrowTracing(String tracingClassName, String tracingMethodName, String tracingMethodDesc, int tracingLineNumber, Throwable throwable) throws Throwable {
        int lineNumber = throwable.getStackTrace()[0].getLineNumber();
        threadLocalTraceEntity().tree.end(throwable,lineNumber);
        finishing();
    }

    @Override
    public void invokeAfterTracing(String tracingClassName, String tracingMethodName, String tracingMethodDesc, int tracingLineNumber) throws Throwable {
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
                traceEntity.tree.trim();
                traceResultResolver.resolve(traceEntity.tree);
            } catch (Throwable e) {
                logger.error("resolve result failed!",e);
            } finally {
                threadBoundEntity.remove();
            }
        }
    }
}
