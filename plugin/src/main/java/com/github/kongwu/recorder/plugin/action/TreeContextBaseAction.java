package com.github.kongwu.recorder.plugin.action;

import com.intellij.openapi.project.DumbAwareAction;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

public abstract class TreeContextBaseAction extends DumbAwareAction {

    protected final TraceContext context;

    public TreeContextBaseAction(String text, TraceContext context) {
        super(text);
        this.context = context;
    }

}
