package com.github.kongwu.recorder.plugin.action;

import com.github.kongwu.recorder.common.model.InvokeStack;
import com.github.kongwu.recorder.common.model.TraceNode;
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

import javax.swing.*;
import java.util.List;
import java.util.Vector;

public class TraceContext {
    private DefaultListModel<TraceNode> invokeStackListModel;
    private Project project;

    public TraceContext(Project project) {
        this.project = project;
        this.invokeStackListModel = new DefaultListModel();
    }

    public void addInvokeStack(TraceNode invokeStack){
        invokeStackListModel.addElement(invokeStack);
    }

    public DefaultListModel<TraceNode> getInvokeStackListModel() {
        return invokeStackListModel;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
