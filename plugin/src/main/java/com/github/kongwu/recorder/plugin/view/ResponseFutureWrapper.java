package com.github.kongwu.recorder.plugin.view;

import com.github.kongwu.recorder.common.model.ResponsePacket;
import com.google.common.util.concurrent.SettableFuture;
import com.intellij.openapi.project.Project;

public class ResponseFutureWrapper {
    private Project project;
    private SettableFuture<ResponsePacket> future;

    public ResponseFutureWrapper(Project project, SettableFuture<ResponsePacket> future) {
        this.project = project;
        this.future = future;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public SettableFuture<ResponsePacket> getFuture() {
        return future;
    }

    public void setFuture(SettableFuture<ResponsePacket> future) {
        this.future = future;
    }
}
