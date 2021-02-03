//package com.github.kongwu.recorder.plugin.action;
//
//import com.intellij.openapi.actionSystem.AnActionEvent;
//import org.jetbrains.annotations.NotNull;
//
//import java.awt.*;
//import java.awt.datatransfer.Clipboard;
//import java.awt.datatransfer.StringSelection;
//import java.awt.datatransfer.Transferable;
//
//public class CopyIdentifierAction extends TreeContextBaseAction {
//
//    @Override
//    public void actionPerformed(@NotNull AnActionEvent e) {
//        Object lastSelectedPathComponent = context.getTree().getLastSelectedPathComponent();
//        System.out.println(lastSelectedPathComponent);
//
//        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//        // 封装文本内容
//        Transferable trans = new StringSelection(lastSelectedPathComponent.toString());
//        // 把文本内容设置到系统剪贴板
//        clipboard.setContents(trans, null);
//    }
//
//    public CopyIdentifierAction(TraceContext context) {
//        super("Copy Identifier", context);
//    }
//}
