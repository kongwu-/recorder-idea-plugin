package com.github.kongwu.recorder.plugin.action;

import com.github.kongwu.recorder.common.model.InvokeStack;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.ui.PopupHandler;
import com.intellij.ui.treeStructure.Tree;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.MouseEvent;

public class TreePopupHandler extends PopupHandler {

    private TraceTreeContext context;

    public TreePopupHandler(TraceTreeContext context) {
        this.context = context;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getClickCount() == 2){
            System.err.print("double click");
        }
        super.mousePressed(e);
    }

    @Override
    public void invokePopup(Component comp, int x, int y) {
        DefaultMutableTreeTableNode selectedNode = (DefaultMutableTreeTableNode) context.getTree().getLastSelectedPathComponent();
        if(selectedNode == null){
            return ;
        }

        final InvokeStack selectedInvokeStack = (InvokeStack) selectedNode.getUserObject();

        DefaultActionGroup actionGroup = new DefaultActionGroup("TraceTreePopupMenu",true);

        actionGroup.addAction(new JumpToSourceAction(context));
        actionGroup.addAction(new CopyIdentifierAction(context));

        JPopupMenu popupMenu = ActionManager.getInstance().createActionPopupMenu("",actionGroup).getComponent();

        popupMenu.show(comp,x,y);
    }
}
