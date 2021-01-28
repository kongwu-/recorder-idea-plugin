package com.github.kongwu.recorder.plugin.agent.test;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

public class Sample {

    public String randomStr(int length){
        prepare(length);
        return doRandomStr(length);
    }

    public void prepare(int length){
        if(length >= Integer.MAX_VALUE){
            throw new RuntimeException("overflow!");
        }
    }

    public String doRandomStr(int length){

        return RandomStringUtils.random(length);
    }

    public void putCache(String key,String value){
        Cache<Object, Object> cache = CacheBuilder.newBuilder()
//                .maximumWeight(100000)
                .build();
        cache.put(key,value);
//        randomStr(10);
    }
//
//    public static void main(String[] args) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true){
//                    new Sample().randomStr(100);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//    }
}
