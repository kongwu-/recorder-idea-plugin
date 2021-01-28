package com.github.kongwu.recorder.plugin.agent.enhance;

import com.github.kongwu.recorder.plugin.agent.advice.TraceAdviceListener;

import java.util.regex.Pattern;

/**
 * <pre>
 * 怎么从 className|methodDesc 到 id 对应起来？？
 * 当id少时，可以id自己来判断是否符合？
 * 
 * 如果是每个 className|methodDesc 为 key ，是否
 * </pre>
 * 
 * @author hengyunabc 2020-04-24
 *
 */
public class SpyImpl extends SpyAPI.AbstractSpy {

    private TraceAdviceListener traceAdviceListener = new TraceAdviceListener();

    @Override
    public void atEnter(Class<?> clazz, String methodName, String methodDesc, Object target, Object[] args) {
        ClassLoader classLoader = clazz.getClassLoader();

        try {
            traceAdviceListener.before(clazz,methodName,methodDesc,target,args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


    }

    @Override
    public void atExit(Class<?> clazz, String methodInfo, Object target, Object[] args, Object returnObject) {
        ClassLoader classLoader = clazz.getClassLoader();

        String[] info = splitMethodInfo(methodInfo);
        String methodName = info[0];
        String methodDesc = info[1];

        try {
            traceAdviceListener.afterReturning(clazz, methodName, methodDesc, target, args, returnObject);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atExceptionExit(Class<?> clazz, String methodInfo, Object target, Object[] args, Throwable throwable) {
        ClassLoader classLoader = clazz.getClassLoader();

        String[] info = splitMethodInfo(methodInfo);
        String methodName = info[0];
        String methodDesc = info[1];


        try {
            traceAdviceListener.afterThrowing(clazz, methodName, methodDesc, target, args, throwable);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void atBeforeInvoke(Class<?> clazz, String invokeInfo, Object target) {
//        traceAdviceListener.before(clazz,methodName,methodDesc,target,args);
        String[] splitInvokeInfo = invokeInfo.split(Pattern.quote("|"));
        if(splitInvokeInfo[0].equals("com/github/kongwu/recorder/plugin/agent/SpyAPI")){
            return;
        }
        try {
            Enhancer.enhance(clazz.getClassLoader(),Class.forName(splitInvokeInfo[0].replace("/",".")),false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            traceAdviceListener.invokeBeforeTracing(splitInvokeInfo[0],splitInvokeInfo[1],splitInvokeInfo[2],Integer.parseInt(splitInvokeInfo[3]));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void atAfterInvoke(Class<?> clazz, String invokeInfo, Object target) {
        String[] splitInvokeInfo = invokeInfo.split(Pattern.quote("|"));
        try {
            traceAdviceListener.invokeAfterTracing(splitInvokeInfo[0],splitInvokeInfo[1],splitInvokeInfo[2],Integer.parseInt(splitInvokeInfo[3]));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void atInvokeException(Class<?> clazz, String invokeInfo, Object target, Throwable throwable) {
        String[] splitInvokeInfo = invokeInfo.split(Pattern.quote("|"));
        try {
            traceAdviceListener.invokeThrowTracing(splitInvokeInfo[0],splitInvokeInfo[1],splitInvokeInfo[2],Integer.parseInt(splitInvokeInfo[3]),throwable);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    private String[] splitMethodInfo(String methodInfo) {
        return methodInfo.split(Pattern.quote("|"));
    }

    private String[] splitInvokeInfo(String invokeInfo) {
        return invokeInfo.split(Pattern.quote("|"));
    }

}