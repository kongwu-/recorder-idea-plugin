package com.github.kongwu.recorder.plugin.action;

import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.ui.treeStructure.Tree;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

public abstract class TreeContextBaseAction extends DumbAwareAction {

    protected final TraceTreeContext context;

    public TreeContextBaseAction(String text,TraceTreeContext context) {
        super(text);
        this.context = context;
    }

    protected DefaultMutableTreeTableNode getSelectedNode(){
        return (DefaultMutableTreeTableNode) context.getTree().getLastSelectedPathComponent();
    }


}
