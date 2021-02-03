//package com.github.kongwu.recorder.plugin.action;
//
//import com.github.kongwu.recorder.common.model.InvokeStack;
//import com.github.kongwu.recorder.plugin.utils.PsiUtils;
//import com.intellij.openapi.actionSystem.AnActionEvent;
//import com.intellij.openapi.project.Project;
//import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
//import org.jetbrains.annotations.NotNull;
//
//public class JumpToSourceAction extends TreeContextBaseAction {
//
//    @Override
//    public void actionPerformed(@NotNull AnActionEvent e) {
//
//        Project project = e.getProject();
//
//        DefaultMutableTreeTableNode selectedNode = context.getSelectedNode();
//
//        InvokeStack selectStack = (InvokeStack) selectedNode.getUserObject();
//
//        InvokeStack parentStack = (InvokeStack) selectedNode.getParent().getUserObject();
//
//        PsiUtils.openInEditor(parentStack.getMethodDescription().getClassName(),selectStack.getMethodDescription().getLineNumber(),0,project);
//    }
//
//    public JumpToSourceAction(TraceContext context) {
//        super("Jump to Source", context);
//    }
//
//}
