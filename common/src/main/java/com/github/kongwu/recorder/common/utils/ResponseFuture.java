package com.github.kongwu.recorder.common.utils;

public class ResponseFuture {
    private Object rt;

    private volatile boolean done;

    public synchronized void done(Object rt){
        this.rt = rt;
        this.notify();
    }

    public synchronized Object get() throws InterruptedException {
        if(rt == null){
            this.wait();
        }
        return rt;
    }
}
