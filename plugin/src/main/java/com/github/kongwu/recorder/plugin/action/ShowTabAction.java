package com.github.kongwu.recorder.plugin.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kongwu.recorder.common.model.InvokeStack;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.wm.RegisterToolWindowTask;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.treeStructure.Tree;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

public class ShowTabAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
//        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        InvokeStack invokeStack = null;
//        try {
//            invokeStack = objectMapper.readValue("{\"methodDescription\":null,\"children\":[{\"methodDescription\":{\"className\":\"com.github.kongwu.recorder.plugin.agent.Sample\",\"methodName\":\"randomStr\",\"lineNumber\":-1,\"throwExp\":null,\"mark\":null,\"hashOfCallStack\":0,\"line\":0,\"highlighted\":false,\"throw\":null},\"children\":[{\"methodDescription\":{\"className\":\"org/apache/commons/lang3/RandomStringUtils\",\"methodName\":\"random\",\"lineNumber\":7,\"throwExp\":null,\"mark\":null,\"hashOfCallStack\":0,\"line\":0,\"highlighted\":false,\"throw\":null},\"children\":[{\"methodDescription\":{\"className\":\"org/apache/commons/lang3/RandomStringUtils\",\"methodName\":\"random\",\"lineNumber\":82,\"throwExp\":null,\"mark\":null,\"hashOfCallStack\":0,\"line\":0,\"highlighted\":false,\"throw\":null},\"children\":[{\"methodDescription\":{\"className\":\"org/apache/commons/lang3/RandomStringUtils\",\"methodName\":\"random\",\"lineNumber\":278,\"throwExp\":null,\"mark\":null,\"hashOfCallStack\":0,\"line\":0,\"highlighted\":false,\"throw\":null},\"children\":[{\"methodDescription\":{\"className\":\"org/apache/commons/lang3/RandomStringUtils\",\"methodName\":\"random\",\"lineNumber\":298,\"throwExp\":null,\"mark\":null,\"hashOfCallStack\":0,\"line\":0,\"highlighted\":false,\"throw\":null},\"children\":[]}]}]}]}]}]}", InvokeStack.class);
//        } catch (Throwable jsonProcessingException) {
//            jsonProcessingException.printStackTrace();
//        }
//
//        DefaultMutableTreeTableNode stackNode = createTreeNode(invokeStack);
//
//        Tree tree = new Tree(stackNode);
//
//        tree.addMouseListener(new TreePopupHandler(tree));
//        // disable double-click to expansion
//        tree.setToggleClickCount(-1);
//
//
////
////        //单元格渲染，或者叫行渲染方式？
////        tree.setCellRenderer();
//
//        Content content = contentFactory.createContent(tree,"Trace result tree",false);
//
//        ToolWindow toolWindow = ToolWindowManager.getInstance(e.getProject()).getToolWindow("TraceResultToolWindow");
//
//        ContentManager contentManager = toolWindow.getContentManager();
//
//        contentManager.addContent(content);
//
//        contentManager.setSelectedContent(content);
//
//        toolWindow.show();
//
//        int rowCount = tree.getRowCount();
//        //注意，这里rowCount必须每次获取，因为展开后rowCount是变化的，row count 不是 node count
//        for (int i = 0; i < tree.getRowCount(); i++) {
//            tree.expandRow(i);
//        }
    }

    private DefaultMutableTreeTableNode createTreeNode(InvokeStack invokeStack) {
        DefaultMutableTreeTableNode rootNode = new DefaultMutableTreeTableNode();

        recursiveChildren(rootNode,invokeStack);

        return rootNode;
    }

    private void recursiveChildren(DefaultMutableTreeTableNode node,InvokeStack invokeStack){

        node.setUserObject(invokeStack);

        List<InvokeStack> children = invokeStack.getChildren();

        if(children != null){
            for (InvokeStack stack : children) {

                DefaultMutableTreeTableNode childNode = new DefaultMutableTreeTableNode();

                recursiveChildren(childNode,stack);

                node.add(childNode);
            }
        }
    }

}
