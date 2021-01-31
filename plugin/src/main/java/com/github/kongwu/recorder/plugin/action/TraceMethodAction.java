package com.github.kongwu.recorder.plugin.action;

import com.github.kongwu.recorder.common.constant.Constants;
import com.github.kongwu.recorder.plugin.service.AgentService;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public class TraceMethodAction extends AnAction {
    private Logger logger = Logger.getInstance(TraceMethodAction.class);

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        AgentService agentService = project.getService(AgentService.class);
        PsiElement psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);


        IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin(PluginId.getId(Constants.PLUGIN_NAME));
        Path pluginPath = plugin.getPluginPath();


        if(psiElement instanceof PsiMethod){
            agentService.trace((PsiMethod) psiElement);
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {

        PsiElement psiElement = e.getData(CommonDataKeys.PSI_ELEMENT);
        boolean visible = false;
        if(psiElement instanceof PsiMethod){
            //暂时不支持抽象类和接口中的方法，后面可能考虑弄个显示子类列表的功能，选择某个子类或者全部子类
            PsiClass ownerClass = ((PsiMethod) psiElement).getContainingClass();
            visible = !ownerClass.isInterface() && !PsiUtil.isAbstractClass(ownerClass);
        }
        e.getPresentation().setEnabledAndVisible(visible);
    }
}
