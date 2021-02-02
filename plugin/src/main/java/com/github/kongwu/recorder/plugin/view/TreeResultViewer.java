package com.github.kongwu.recorder.plugin.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kongwu.recorder.common.model.InvokeStack;
import com.github.kongwu.recorder.plugin.action.TraceTreeContext;
import com.github.kongwu.recorder.plugin.action.TreePopupHandler;
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

    public TreeResultViewer(Project project) {
        this.project = project;
    }

    @Override
    public void draw(String body) {


        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {

                InvokeStack invokeStack = null;
                try {
                    invokeStack = objectMapper.readValue(body, InvokeStack.class);
                } catch (JsonProcessingException e) {
                    logger.error(e);
                }

                DefaultMutableTreeTableNode stackNode = createTreeNode(invokeStack);

                Tree tree = new Tree(stackNode);

                TraceTreeContext context = new TraceTreeContext(tree);

                    tree.addMouseListener(new TreePopupHandler(context));
                    // disable double-click to expansion
                    tree.setToggleClickCount(-1);

                ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();

                Content content = contentFactory.createContent(tree,"Trace result tree",false);

                ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("TraceResultToolWindow");

                ContentManager contentManager = toolWindow.getContentManager();

                contentManager.addContent(content);

                contentManager.setSelectedContent(content);

                toolWindow.show();

                //注意，这里rowCount必须每次获取，因为展开后rowCount是变化的，row count 不是 node count
                for (int i = 0; i < tree.getRowCount(); i++) {
                    tree.expandRow(i);
                }
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
