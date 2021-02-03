//package com.github.kongwu.recorder.plugin.action;
//
//import com.github.kongwu.recorder.common.model.InvokeStack;
//import com.github.kongwu.recorder.plugin.utils.PsiUtils;
//import com.intellij.openapi.actionSystem.ActionManager;
//import com.intellij.openapi.actionSystem.DefaultActionGroup;
//import com.intellij.ui.PopupHandler;
//import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.MouseEvent;
//
//public class TreePopupHandler extends PopupHandler {
//
//    private TraceContext context;
//
//    public TreePopupHandler(TraceContext context) {
//        this.context = context;
//    }
//
//    @Override
//    public void mousePressed(MouseEvent e) {
//        if(e.getClickCount() == 2){
//            DefaultMutableTreeTableNode selectedNode = context.getSelectedNode();
//
//            InvokeStack selectStack = (InvokeStack) selectedNode.getUserObject();
//
//            InvokeStack parentStack = (InvokeStack) selectedNode.getParent().getUserObject();
//
//            PsiUtils.openInEditor(parentStack.getMethodDescription().getClassName(),selectStack.getMethodDescription().getLineNumber(),0,context.getProject());
//        }
//        super.mousePressed(e);
//    }
//
//    @Override
//    public void invokePopup(Component comp, int x, int y) {
//        DefaultMutableTreeTableNode selectedNode = (DefaultMutableTreeTableNode) context.getTree().getLastSelectedPathComponent();
//        if(selectedNode == null){
//            return ;
//        }
//
//        final InvokeStack selectedInvokeStack = (InvokeStack) selectedNode.getUserObject();
//
//        DefaultActionGroup actionGroup = new DefaultActionGroup("TraceTreePopupMenu",true);
//
//        actionGroup.addAction(new JumpToSourceAction(context));
//        actionGroup.addAction(new CopyIdentifierAction(context));
//
//        JPopupMenu popupMenu = ActionManager.getInstance().createActionPopupMenu("",actionGroup).getComponent();
//
//        popupMenu.show(comp,x,y);
//    }
//}
