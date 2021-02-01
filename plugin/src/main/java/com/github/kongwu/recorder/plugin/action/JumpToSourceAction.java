package com.github.kongwu.recorder.plugin.action;

import com.github.kongwu.recorder.common.model.InvokeStack;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.GlobalSearchScopes;
import com.intellij.psi.search.ProjectScope;
import com.intellij.psi.search.searches.AllClassesSearch;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.Query;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jetbrains.annotations.NotNull;

public class JumpToSourceAction extends TreeContextBaseAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        Project project = e.getProject();

        DefaultMutableTreeTableNode selectedNode = context.getSelectedNode();
        InvokeStack selectStaCK = (InvokeStack) selectedNode.getUserObject();

        Query<PsiClass> query = AllClassesSearch.search(GlobalSearchScope.allScope(project), project);
        PsiClass matchedClass = query.filtering(psiClass -> selectStaCK.getMethodDescription().getClassName().equals(psiClass.getQualifiedName())).findFirst();
        if(matchedClass !=null){
            FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);

            OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, matchedClass.getNavigationElement().getContainingFile().getVirtualFile(),selectStaCK.getMethodDescription().getLineNumber()-1,0);

            fileEditorManager.openTextEditor(openFileDescriptor,true);
        }

    }

    public JumpToSourceAction(TraceTreeContext context) {
        super("Jump to Source", context);
    }

}
