package com.github.kongwu.recorder.plugin.gui;

import com.github.kongwu.recorder.common.model.ThreadNode;
import com.github.kongwu.recorder.plugin.action.TraceContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.OnePixelSplitter;
import com.intellij.ui.components.*;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.intellij.util.ui.components.BorderLayoutPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.*;

import static com.intellij.ui.SimpleTextAttributes.*;

public class TraceResultPanel implements Disposable {
    private BorderLayoutPanel rootPanel;

    private JBScrollPane leftPane;

    private JBScrollPane rightPane;

    private JBList traceResultList;

    private Tree traceResultTree;

    private JBPanel toolbarPane;

    private JBSplitter splitter;

    private final TraceContext traceContext;

    public TraceResultPanel(TraceContext traceContext) {
        this.traceContext = traceContext;
        initializeGUI();
        mockData();
    }

    public void initializeGUI() {
        rootPanel = new BorderLayoutPanel();

        toolbarPane = new JBPanel<>(new VerticalFlowLayout());

        toolbarPane.add(new JBTextField("typing..."));
        rootPanel.addToTop(toolbarPane);

        splitter = new OnePixelSplitter(false, 0.35f);


        leftPane = new JBScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        leftPane.setViewportBorder(JBUI.Borders.empty());
        leftPane.setBorder(JBUI.Borders.empty());

        DefaultListModel listModel =  new DefaultListModel();
        for (int i = 0; i < 100; i++) {
            listModel.addElement("io.netty.xxx.XXClass:MethodA()");
        }


        traceResultList = new JBList(traceContext.getInvokeStackListModel());

        traceResultList.setCellRenderer(new ListCellRenderer<ThreadNode>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends ThreadNode> list, ThreadNode value, int index, boolean selected, boolean cellHasFocus) {
                BorderLayoutPanel cellPanel = new BorderLayoutPanel();

                if (UIUtil.isUnderWin10LookAndFeel()) {
                    cellPanel.setBackground(selected ? list.getSelectionBackground() : list.getBackground());
                } else {
                    cellPanel.setBackground(selected ? list.getSelectionBackground() : null);
                }
                cellPanel.addToLeft(new ColoredListCellRenderer<ThreadNode>() {
                    @Override
                    protected void customizeCellRenderer(@NotNull JList<? extends ThreadNode> list, ThreadNode value, int index, boolean selected, boolean hasFocus) {
                        append(value.getTimestamp().getTime()+"", REGULAR_ATTRIBUTES, 50, 2);
                    }
                }.getListCellRendererComponent(list, value, index, selected, cellHasFocus));
                cellPanel.addToCenter(new ColoredListCellRenderer<ThreadNode>() {
                    @Override
                    protected void customizeCellRenderer(@NotNull JList<? extends ThreadNode> list, ThreadNode value, int index, boolean selected, boolean hasFocus) {
                        append(value.getThreadName(), REGULAR_BOLD_ATTRIBUTES, 50, 2);
                    }
                }.getListCellRendererComponent(list, value, index, selected, cellHasFocus));
                cellPanel.addToRight(new ColoredListCellRenderer<ThreadNode>() {
                    @Override
                    protected void customizeCellRenderer(@NotNull JList<? extends ThreadNode> list, ThreadNode value, int index, boolean selected, boolean hasFocus) {
                        append(value.getMark(), GRAYED_ATTRIBUTES, 50, 4);
                    }
                }.getListCellRendererComponent(list, value, index, selected, cellHasFocus));
                return cellPanel;
            }
        });

        leftPane.setViewportView(traceResultList);

        //====
        rightPane = new JBScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        rightPane.setViewportBorder(JBUI.Borders.empty());
        rightPane.setBorder(JBUI.Borders.empty());

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        rootNode.setUserObject("root");

        rootNode.add(new DefaultMutableTreeNode("childa"));

        traceResultTree = new Tree(rootNode);

        rightPane.setViewportView(traceResultTree);



        splitter.setFirstComponent(leftPane);
        splitter.setSecondComponent(rightPane);





//        rootPanel.add(toolbarPane);

        rootPanel.addToCenter(splitter);
//        rootPanel.add(rightContentPanel);
//        rootPanel.add(new JButton("xxx"));

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
