package com.github.kongwu;

import com.alibaba.bytekit.asm.MethodProcessor;
import com.alibaba.bytekit.asm.interceptor.InterceptorProcessor;
import com.alibaba.bytekit.asm.interceptor.parser.DefaultInterceptorClassParser;
import com.alibaba.bytekit.asm.location.Location;
import com.alibaba.bytekit.asm.location.LocationType;
import com.alibaba.bytekit.asm.location.MethodInsnNodeWare;
import com.alibaba.bytekit.asm.location.filter.GroupLocationFilter;
import com.alibaba.bytekit.asm.location.filter.InvokeCheckLocationFilter;
import com.alibaba.bytekit.asm.location.filter.InvokeContainLocationFilter;
import com.alibaba.bytekit.asm.location.filter.LocationFilter;
import com.alibaba.bytekit.utils.AgentUtils;
import com.alibaba.bytekit.utils.AsmOpUtils;
import com.alibaba.bytekit.utils.AsmUtils;
import com.alibaba.deps.org.objectweb.asm.ClassReader;
import com.alibaba.deps.org.objectweb.asm.Opcodes;
import com.alibaba.deps.org.objectweb.asm.Type;
import com.alibaba.deps.org.objectweb.asm.tree.AbstractInsnNode;
import com.alibaba.deps.org.objectweb.asm.tree.ClassNode;
import com.alibaba.deps.org.objectweb.asm.tree.MethodInsnNode;
import com.alibaba.deps.org.objectweb.asm.tree.MethodNode;
import com.github.kongwu.test.Sample;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class Enhancer implements ClassFileTransformer {

    private static final Map<Class,Boolean> enhancedClasses = new ConcurrentHashMap<>();

    private String className;

    static {
        SpyAPI.setSpy(new SpyImpl());
    }

    private boolean bootstrap;

    @Override
    public byte[] transform(ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if(!className.equals(this.className)){
            return null;
        }
        ClassNode classNode = new ClassNode(Opcodes.ASM9);
        ClassReader classReader = AsmUtils.toClassNode(classfileBuffer, classNode);
        // remove JSR https://github.com/alibaba/arthas/issues/1304
        classNode = AsmUtils.removeJSRInstructions(classNode);

        // 生成增强字节码
        DefaultInterceptorClassParser defaultInterceptorClassParser = new DefaultInterceptorClassParser();

        final List<InterceptorProcessor> interceptorProcessors = new ArrayList<InterceptorProcessor>();

        if(bootstrap){
            interceptorProcessors.addAll(defaultInterceptorClassParser.parse(SpyInterceptors.SpyInterceptor1.class));
            interceptorProcessors.addAll(defaultInterceptorClassParser.parse(SpyInterceptors.SpyInterceptor2.class));
            interceptorProcessors.addAll(defaultInterceptorClassParser.parse(SpyInterceptors.SpyInterceptor3.class));
        }
            interceptorProcessors.addAll(defaultInterceptorClassParser.parse(SpyInterceptors.SpyTraceExcludeJDKInterceptor1.class));
            interceptorProcessors.addAll(defaultInterceptorClassParser.parse(SpyInterceptors.SpyTraceExcludeJDKInterceptor2.class));
            interceptorProcessors.addAll(defaultInterceptorClassParser.parse(SpyInterceptors.SpyTraceExcludeJDKInterceptor3.class));

        List<MethodNode> matchedMethods = new ArrayList<com.alibaba.deps.org.objectweb.asm.tree.MethodNode>();
        for (MethodNode methodNode : classNode.methods) {
            if (!isIgnore(methodNode)) {
                matchedMethods.add(methodNode);
            }
        }

        // 用于检查是否已插入了 spy函数，如果已有则不重复处理
        GroupLocationFilter groupLocationFilter = new GroupLocationFilter();

        LocationFilter enterFilter = new InvokeContainLocationFilter(Type.getInternalName(SpyAPI.class), "atEnter",
                LocationType.ENTER);
        LocationFilter existFilter = new InvokeContainLocationFilter(Type.getInternalName(SpyAPI.class), "atExit",
                LocationType.EXIT);
        LocationFilter exceptionFilter = new InvokeContainLocationFilter(Type.getInternalName(SpyAPI.class),
                "atExceptionExit", LocationType.EXCEPTION_EXIT);

        groupLocationFilter.addFilter(enterFilter);
        groupLocationFilter.addFilter(existFilter);
        groupLocationFilter.addFilter(exceptionFilter);

        LocationFilter invokeBeforeFilter = new InvokeCheckLocationFilter(Type.getInternalName(SpyAPI.class),
                "atBeforeInvoke", LocationType.INVOKE);
        LocationFilter invokeAfterFilter = new InvokeCheckLocationFilter(Type.getInternalName(SpyAPI.class),
                "atInvokeException", LocationType.INVOKE_COMPLETED);
        LocationFilter invokeExceptionFilter = new InvokeCheckLocationFilter(Type.getInternalName(SpyAPI.class),
                "atInvokeException", LocationType.INVOKE_EXCEPTION_EXIT);
        groupLocationFilter.addFilter(invokeBeforeFilter);
        groupLocationFilter.addFilter(invokeAfterFilter);
        groupLocationFilter.addFilter(invokeExceptionFilter);

        for (MethodNode methodNode : matchedMethods) {
            if (AsmUtils.isNative(methodNode)) {
//                System.out.println("ignore native method: {}",
//                        AsmUtils.methodDeclaration(Type.getObjectType(classNode.name), methodNode));
                continue;
            }
            // 先查找是否有 atBeforeInvoke 函数，如果有，则说明已经有trace了，则直接不再尝试增强，直接插入 listener
//            if(AsmUtils.containsMethodInsnNode(methodNode, Type.getInternalName(SpyAPI.class), "atBeforeInvoke")) {
//                for (AbstractInsnNode insnNode = methodNode.instructions.getFirst(); insnNode != null; insnNode = insnNode
//                        .getNext()) {
//                    if (insnNode instanceof MethodInsnNode) {
//                        final MethodInsnNode methodInsnNode = (MethodInsnNode) insnNode;
//                        if(methodInsnNode.owner.startsWith("java/")) {
//                            continue;
//                        }
//                        // 原始类型的box类型相关的都跳过
//                        if(AsmOpUtils.isBoxType(Type.getObjectType(methodInsnNode.owner))) {
//                            continue;
//                        }
//                        AdviceListenerManager.registerTraceAdviceListener(inClassLoader, className,
//                                methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc, listener);
//                    }
//                }
//            }else {
                MethodProcessor methodProcessor = new MethodProcessor(classNode, methodNode,groupLocationFilter);
                for (InterceptorProcessor interceptor : interceptorProcessors) {
                    try {
                        System.out.println("method: "+methodNode.name);
                        List<Location> locations = interceptor.process(methodProcessor);
//                        for (Location location : locations) {
//                            if (location instanceof MethodInsnNodeWare) {
//                                MethodInsnNodeWare methodInsnNodeWare = (MethodInsnNodeWare) location;
//                                MethodInsnNode methodInsnNode = methodInsnNodeWare.methodInsnNode();
//
//                                AdviceListenerManager.registerTraceAdviceListener(inClassLoader, className,
//                                        methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc, listener);
//                            }
//                        }

                    } catch (Throwable e) {
                        System.out.println("baocuole");
                        e.printStackTrace();
//                        logger.error("enhancer error, class: {}, method: {}, interceptor: {}", classNode.name, methodNode.name, interceptor.getClass().getName(), e);
                    }
                }
//            }

//            // enter/exist 总是要插入 listener
//            AdviceListenerManager.registerAdviceListener(classLoader, className, methodNode.name, methodNode.desc,
//                    listener);
//            affect.addMethodAndCount(inClassLoader, className, methodNode.name, methodNode.desc);
        }

        // https://github.com/alibaba/arthas/issues/1223 , V1_5 的major version是49
        if (AsmUtils.getMajorVersion(classNode.version) < 49) {
            classNode.version = AsmUtils.setMajorVersion(classNode.version, 49);
        }

        byte[] enhanceClassByteArray = AsmUtils.toBytes(classNode, classLoader, classReader);
        return enhanceClassByteArray;
    }

    public static synchronized void enhance(ClassLoader classLoader,Class clazz,boolean bootstrap){
        System.out.println("enhancing class: "+clazz.getName());
        if(enhancedClasses.containsKey(clazz)){
            System.out.println("Skip repetitive enhancement!"+clazz.getName());
        }

        Enhancer enhancer = new Enhancer();
        enhancer.className = clazz.getName().replace(".","/");
        enhancer.bootstrap = bootstrap;
        Instrumentation instrumentation = AgentUtils.install();


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 100; i++) {
//                    System.out.println("$$$  "+sample.randomStr(10));
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();


        instrumentation.addTransformer(enhancer,true);
        try {
            instrumentation.retransformClasses(clazz);
        } catch (UnmodifiableClassException e) {
            e.printStackTrace();
        }
        enhancedClasses.put(clazz,true);
        instrumentation.removeTransformer(enhancer);

    }

    public static void main(String[] args) throws Exception {
        Sample sample = new Sample();
        enhance(Thread.currentThread().getContextClassLoader(),Sample.class,true);
        sample.putCache("aa","bb");
//
//        // 启动Sample，不断执行
//        final Sample sample = new Sample();
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 100; ++i) {
//                    try {
//                        TimeUnit.SECONDS.sleep(3);
//                        String result = sample.randomStr(11);
//                        System.out.println("call hello result: " + result);
//                    } catch (Throwable e) {
//                        // ignore
//                        System.out.println("call hello exception: " + e.getMessage());
//                    }
//                }
//            }
//        });
//        t.start();
//
//        // 解析定义的 Interceptor类 和相关的注解
//        DefaultInterceptorClassParser interceptorClassParser = new DefaultInterceptorClassParser();
//        List<InterceptorProcessor> processors = interceptorClassParser.parse(SpyInterceptors.SpyInterceptor1.class);
//
//        // 加载字节码
//        ClassNode classNode = AsmUtils.loadClass(Sample.class);
//
//        // 对加载到的字节码做增强处理
//        for (MethodNode methodNode : classNode.methods) {
//                MethodProcessor methodProcessor = new MethodProcessor(classNode, methodNode);
//                for (InterceptorProcessor interceptor : processors) {
//                    interceptor.process(methodProcessor);
//                }
//        }
//
//        // 获取增强后的字节码
//        byte[] bytes = AsmUtils.toBytes(classNode);
//
////        System.out.println(Decompiler.decompile(bytes));
//
//        // 等待，查看未增强里的输出结果
//        TimeUnit.SECONDS.sleep(10);
//
//        // 通过 reTransform 增强类
//        AgentUtils.reTransform(Sample.class, bytes);
//        System.in.read();
    }

    private boolean isIgnore(MethodNode methodNode) {
        //add method chooser
        return null == methodNode || isAbstract(methodNode.access)
                || methodNode.name.equals("<clinit>");
    }

    /**
     * 是否抽象属性
     */
    private boolean isAbstract(int access) {
        return (Opcodes.ACC_ABSTRACT & access) == Opcodes.ACC_ABSTRACT;
    }
}
