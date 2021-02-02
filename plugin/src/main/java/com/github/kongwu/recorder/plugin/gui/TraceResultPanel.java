package com.github.kongwu.recorder.plugin.gui;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.OnePixelSplitter;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

public class TraceResultPanel implements Disposable {
    private JBPanel rootPanel;

    private JBScrollPane leftPane;

    private JBScrollPane rightPane;

    private JBList traceResultList;

    private Tree traceResultTree;

    private JBPanel toolbarPane;

    private JBSplitter splitter;

    public TraceResultPanel() {
        initializeGUI();
        mockData();
    }

    public void initializeGUI() {
        rootPanel = new JBPanel(new VerticalFlowLayout(VerticalFlowLayout.TOP, 0, 0, true, true));

        toolbarPane = new JBPanel<>(new VerticalFlowLayout());

        rootPanel.add(toolbarPane);

        splitter = new OnePixelSplitter(false, 0.35f);


        leftPane = new JBScrollPane();

        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < 100; i++) {
            listModel.addElement(i);
        }
        traceResultList = new JBList(listModel);

//        leftPane.add(traceResultList);

        //====
        rightPane = new JBScrollPane();

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        rootNode.setUserObject("root");

        rootNode.add(new DefaultMutableTreeNode("childa"));


        traceResultTree = new Tree(rootNode);

//        rightPane.add(traceResultTree);




        splitter.setFirstComponent(traceResultList);
        splitter.setSecondComponent(traceResultTree);

        rootPanel.add(splitter);
    }

    public void mockData(){

    }

    @Override
    public void dispose() {

    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

}
