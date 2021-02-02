package com.github.kongwu.recorder.plugin.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class TraceResultToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {


        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

        TraceResultPanel traceResultPanel = new TraceResultPanel();

        Content content = contentFactory.createContent(traceResultPanel.getRootPanel(), "list data", true);

//        DefaultListModel<String> listModel = new DefaultListModel<>();
//
//        JBList jbList = new JBList(listModel);
//
//        listModel.addElement("Hello");
//
//        Content content = contentFactory.createContent(jbList, "list data", true);

        toolWindow.getContentManager().addContent(content);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 100; i++) {
//                    listModel.addElement("index: "+i);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();

    }
}
