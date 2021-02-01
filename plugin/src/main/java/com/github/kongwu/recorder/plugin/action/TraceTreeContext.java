package com.github.kongwu.recorder.plugin.action;

import com.intellij.find.usages.impl.AllSearchOptions;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopupStep;
import com.intellij.openapi.ui.popup.util.BaseListPopupStep;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.AllClassesSearch;
import com.intellij.ui.treeStructure.Tree;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

public class TraceTreeContext {
    private final Tree tree;
    private Project project;

    public TraceTreeContext(Tree tree, Project project) {
        this.tree = tree;
        this.project = project;
    }

    protected DefaultMutableTreeTableNode getSelectedNode(){
        return (DefaultMutableTreeTableNode) tree.getLastSelectedPathComponent();
    }

    public Tree getTree() {
        return tree;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
