package com.github.kongwu.recorder.plugin.view;

public class LogResultViewer implements ResultViewer{
    @Override
    public void draw(String body) {
        System.out.println(body);
    }
}
