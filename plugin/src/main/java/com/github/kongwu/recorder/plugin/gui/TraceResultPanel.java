package com.github.kongwu.recorder.plugin.gui;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.OnePixelSplitter;
import com.intellij.ui.components.*;
import com.intellij.ui.treeStructure.Tree;
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


    public TraceResultPanel() {
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

        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < 100; i++) {
            listModel.addElement(i+"");
        }
        traceResultList = new JBList(listModel);
//        traceResultList.setCellRenderer(new ListCellRenderer() {
//            @Override
//            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//                JBPanel cellPanel = new JBPanel(new GridBagLayout());
//
//                cellPanel.add(new JBLabel(value.toString()),new GridBagConstraints(50,50,1,1,1,1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(50, 50, 50, 0),50,0));
//                cellPanel.add(new JBLabel(value.toString()),new GridBagConstraints(1,50,1,1,1,1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(50, 50, 50, 0),50,0));
//                cellPanel.add(new JBLabel(value.toString()),new GridBagConstraints(2,50,1,1,1,1, GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,new Insets(50, 50, 50, 0),50,0));
//                return cellPanel;
//            }
//        });
        traceResultList.setCellRenderer(new ColoredListCellRenderer() {
            @Override
            protected void customizeCellRenderer(@NotNull JList list, Object value, int index, boolean selected, boolean hasFocus) {
                    append(String.valueOf(value),REGULAR_ATTRIBUTES,50,2);
                    append(String.valueOf(value),SYNTHETIC_ATTRIBUTES,100,2);
                    append(String.valueOf(value),GRAYED_ATTRIBUTES,150,2);
                    append(String.valueOf(value),ERROR_ATTRIBUTES,200,2);
//                    append(String.valueOf(value),DARK_TEXT,50,4/*);
//                    append(String.valueOf(value),GRAYED_SMALL_ATTRIBUTES,50,4);
//                    append(String.valueOf(value),DARK_TEXT,50,4);
//                    append(String.valueOf(value),SIMPLE_CELL_ATTRIBUTES,50,4);
//                    append(String.valueOf(value),EXCLUDED_ATTRIBUTES,50,4);
                    append(String.valueOf(value),LINK_ATTRIBUTES,true);
            }
        });

        leftPane.setViewportView(traceResultList);

        //====
        rightPane = new JBScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

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
