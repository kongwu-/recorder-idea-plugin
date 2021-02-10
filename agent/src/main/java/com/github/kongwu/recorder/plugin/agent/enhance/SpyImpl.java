package com.github.kongwu.recorder.plugin.agent.enhance;

import com.github.kongwu.recorder.common.logger.Logger;
import com.github.kongwu.recorder.plugin.agent.advice.AdviceListener;
import com.github.kongwu.recorder.plugin.agent.advice.AdviceListenerManager;

import java.util.regex.Pattern;

public class SpyImpl extends SpyAPI.AbstractSpy {

    private Logger logger = Logger.getLogger(SpyImpl.class);

    @Override
    public void atEnter(Class<?> clazz, String methodName, String methodDesc, Object target, Object[] args) {
        try {
            getAdviceListener().before(clazz,methodName,methodDesc,target,args);
        } catch (Throwable e) {
            logger.error("before failed!",e);
        }
    }

    @Override
    public void atExit(Class<?> clazz, String methodInfo, Object target, Object[] args, Object returnObject) {
        String[] info = splitMethodInfo(methodInfo);
        String methodName = info[0];
        String methodDesc = info[1];

        try {
            getAdviceListener().afterReturning(clazz, methodName, methodDesc, target, args, returnObject);
        } catch (Throwable e) {
            logger.error("afterReturning failed!",e);
        }
    }

    @Override
    public void atExceptionExit(Class<?> clazz, String methodInfo, Object target, Object[] args, Throwable throwable) {
        String[] info = splitMethodInfo(methodInfo);
        String methodName = info[0];
        String methodDesc = info[1];

        try {
            getAdviceListener().afterThrowing(clazz, methodName, methodDesc, target, args, throwable);
        } catch (Throwable e) {
            logger.error("afterThrowing failed!",e);
        }
    }

    @Override
    public void atBeforeInvoke(Class<?> clazz, String invokeInfo, Object target) {
        System.out.println("###  "+invokeInfo+"：：：target="+target.getClass());
        String[] splitInvokeInfo = invokeInfo.split(Pattern.quote("|"));
        try {
//            Enhancer.enhance(splitInvokeInfo[0].replace("/","."),false);
            getAdviceListener().invokeBeforeTracing(splitInvokeInfo[0],splitInvokeInfo[1],splitInvokeInfo[2],Integer.parseInt(splitInvokeInfo[3]));
        } catch (Throwable e) {
            logger.error("invokeBeforeTracing failed!",e);
        }
    }

    @Override
    public void atAfterInvoke(Class<?> clazz, String invokeInfo, Object target) {
        String[] splitInvokeInfo = invokeInfo.split(Pattern.quote("|"));
        try {
            getAdviceListener().invokeAfterTracing(splitInvokeInfo[0],splitInvokeInfo[1],splitInvokeInfo[2],Integer.parseInt(splitInvokeInfo[3]));
        } catch (Throwable e) {
            logger.error("invokeAfterTracing failed!",e);
        }
    }

    @Override
    public void atInvokeException(Class<?> clazz, String invokeInfo, Object target, Throwable throwable) {
        String[] splitInvokeInfo = invokeInfo.split(Pattern.quote("|"));
        try {
            getAdviceListener().invokeThrowTracing(splitInvokeInfo[0],splitInvokeInfo[1],splitInvokeInfo[2],Integer.parseInt(splitInvokeInfo[3]),throwable);
        } catch (Throwable e) {
            logger.error("invokeThrowTracing failed!",e);
        }
    }

    private AdviceListener getAdviceListener(){
        return AdviceListenerManager.getAdviceListener();
    }

    private String[] splitMethodInfo(String methodInfo) {
        return methodInfo.split(Pattern.quote("|"));
    }

    private String[] splitInvokeInfo(String invokeInfo) {
        return invokeInfo.split(Pattern.quote("|"));
    }

}