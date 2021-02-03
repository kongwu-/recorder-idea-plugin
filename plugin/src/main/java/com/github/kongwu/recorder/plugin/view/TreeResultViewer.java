package com.github.kongwu.recorder.plugin.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.github.kongwu.recorder.common.model.InvokeStack;
import com.github.kongwu.recorder.common.model.TraceNode;
import com.github.kongwu.recorder.plugin.action.TraceContext;
import com.github.kongwu.recorder.plugin.gui.TraceResultPanel;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.treeStructure.Tree;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

import java.util.List;

public class TreeResultViewer implements ResultViewer{

    private Project project;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Logger logger = Logger.getInstance(TreeResultViewer.class);

    private TraceContext traceContext;

    private volatile boolean contentCreated = false;

    public TreeResultViewer(Project project) {
        this.project = project;
    }

    @Override
    public synchronized void draw(String body) {

        if(traceContext == null){
            traceContext = new TraceContext(project);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder().build();
        objectMapper.activateDefaultTyping(ptv); // default to using DefaultTyping.OBJECT_AND_NON_CONCRETE
        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
        ApplicationManager.getApplication().invokeLater(() -> {

            TraceNode traceNode = null;
            try {
                traceNode = objectMapper.readValue(body, TraceNode.class);
            } catch (JsonProcessingException e) {
                logger.error(e);
            }

            traceContext.addInvokeStack(traceNode);

            ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

            if(!contentCreated){
                Content content = contentFactory.createContent(new TraceResultPanel(traceContext).getRootPanel(),"Trace result tree",false);

                ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("TraceResultToolWindow");

                ContentManager contentManager = toolWindow.getContentManager();

                contentManager.addContent(content);

                contentManager.setSelectedContent(content);

                toolWindow.show();

                contentCreated = true;
            }
        });
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
