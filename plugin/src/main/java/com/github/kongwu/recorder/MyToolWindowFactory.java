package com.github.kongwu.recorder;

import com.intellij.execution.impl.ExecutionManagerImpl;
import com.intellij.execution.process.KillableColoredProcessHandler;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.RunContentManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Conditions;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyToolWindowFactory extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        PsiElement sourceElement = JavaPsiFacade.getInstance(project).findClass("java.util.ArrayList", GlobalSearchScope.allScope(project)).getSourceElement();
        OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project, sourceElement.getContainingFile().getVirtualFile());
        fileEditorManager.openTextEditor(openFileDescriptor,true);
//
//        final ExecutionManagerImpl executionManager = ExecutionManagerImpl.getInstance(project);
//        List<RunContentDescriptor> runningDescriptors = executionManager.getRunningDescriptors(Conditions.alwaysTrue());
//        System.out.println("正在运行的进程有……"+runningDescriptors.size());
//        RunContentDescriptor selectedContent = RunContentManager.getInstance(project).getSelectedContent();
//        KillableColoredProcessHandler processHandler = (KillableColoredProcessHandler)selectedContent.getProcessHandler();
//                    Process process = processHandler.getProcess();
//        System.out.println(process.info());
//        for (RunContentDescriptor runningDescriptor : runningDescriptors) {
//            KillableColoredProcessHandler processHandler = (KillableColoredProcessHandler) runningDescriptor.getProcessHandler();
//            Process process = processHandler.getProcess();
//            System.out.println(process.info());
//        }
    }


    @Override
    public void update(@NotNull AnActionEvent e) {
        PsiElement psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);

        System.out.println(psiElement.getClass().getName());
        e.getPresentation().setEnabledAndVisible(psiElement instanceof PsiMethod);

    }
}