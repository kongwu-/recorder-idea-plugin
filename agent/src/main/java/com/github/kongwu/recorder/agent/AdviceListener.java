package com.github.kongwu;

/**
 * 通知监听器<br/>
 * Created by vlinux on 15/5/17.
 */
public interface AdviceListener {

    long id();

    /**
     * 监听器创建<br/>
     * 监听器被注册时触发
     */
    void create();

    /**
     * 监听器销毁<br/>
     * 监听器被销毁时触发
     */
    void destroy();

    /**
     * 前置通知
     *
     * @param loader     类加载器
     * @param className  类名
     * @param methodName 方法名
     * @param methodDesc 方法描述
     * @param target     目标类实例
     *                   若目标为静态方法,则为null
     * @param args       参数列表
     * @throws Throwable 通知过程出错
     */
    void before(
            Class<?> clazz, String methodName, String methodDesc,
            Object target, Object[] args) throws Throwable;

    /**
     * 返回通知
     *
     * @param loader       类加载器
     * @param className    类名
     * @param methodName   方法名
     * @param methodDesc   方法描述
     * @param target       目标类实例
     *                     若目标为静态方法,则为null
     * @param args         参数列表
     * @param returnObject 返回结果
     *                     若为无返回值方法(void),则为null
     * @throws Throwable 通知过程出错
     */
    void afterReturning(
            Class<?> clazz, String methodName, String methodDesc,
            Object target, Object[] args,
            Object returnObject) throws Throwable;

    /**
     * 异常通知
     *
     * @param loader     类加载器
     * @param className  类名
     * @param methodName 方法名
     * @param methodDesc 方法描述
     * @param target     目标类实例
     *                   若目标为静态方法,则为null
     * @param args       参数列表
     * @param throwable  目标异常
     * @throws Throwable 通知过程出错
     */
    void afterThrowing(
            Class<?> clazz, String methodName, String methodDesc,
            Object target, Object[] args,
            Throwable throwable) throws Throwable;

    /**
     * 调用之前跟踪
     *
     * @param tracingClassName  调用类名
     * @param tracingMethodName 调用方法名
     * @param tracingMethodDesc 调用方法描述
     * @param tracingLineNumber 执行调用行数
     * @throws Throwable 通知过程出错
     */
    void invokeBeforeTracing(
            String tracingClassName,
            String tracingMethodName,
            String tracingMethodDesc,
            int tracingLineNumber) throws Throwable;

    /**
     * 抛异常后跟踪
     *
     * @param tracingClassName  调用类名
     * @param tracingMethodName 调用方法名
     * @param tracingMethodDesc 调用方法描述
     * @param tracingLineNumber 执行调用行数
     * @param throwable
     * @throws Throwable 通知过程出错
     */
    void invokeThrowTracing(
            String tracingClassName,
            String tracingMethodName,
            String tracingMethodDesc,
            int tracingLineNumber, Throwable throwable) throws Throwable;


    /**
     * 调用之后跟踪
     *
     * @param tracingClassName  调用类名
     * @param tracingMethodName 调用方法名
     * @param tracingMethodDesc 调用方法描述
     * @param tracingLineNumber 执行调用行数
     * @throws Throwable 通知过程出错
     */
    void invokeAfterTracing(
            String tracingClassName,
            String tracingMethodName,
            String tracingMethodDesc,
            int tracingLineNumber) throws Throwable;


}