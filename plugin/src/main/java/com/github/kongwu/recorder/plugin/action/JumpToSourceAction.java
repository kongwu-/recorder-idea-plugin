package com.github.kongwu.recorder.plugin.action;

import com.github.kongwu.recorder.common.model.InvokeStack;
import com.github.kongwu.recorder.plugin.utils.PsiUtils;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopes;
import com.intellij.psi.search.ProjectScope;
import com.intellij.psi.search.searches.AllClassesSearch;
import com.intellij.psi.util.ClassUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.Query;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jetbrains.annotations.NotNull;

public class JumpToSourceAction extends TreeContextBaseAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        Project project = e.getProject();

        DefaultMutableTreeTableNode selectedNode = context.getSelectedNode();

        InvokeStack selectStack = (InvokeStack) selectedNode.getUserObject();

        InvokeStack parentStack = (InvokeStack) selectedNode.getParent().getUserObject();

        PsiUtils.openInEditor(parentStack.getMethodDescription().getClassName(),selectStack.getMethodDescription().getLineNumber(),0,project);
    }

    public JumpToSourceAction(TraceTreeContext context) {
        super("Jump to Source", context);
    }

}
