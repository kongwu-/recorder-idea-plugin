package com.github.kongwu.recorder.plugin.utils;

import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.ClassUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class PsiUtils {

    private PsiUtils() {
    }

    public static PsiMethod getEnclosingMethod(PsiFile psiFile, int position) {
        PsiElement psiElement = psiFile.findElementAt(position);
        while (psiElement != null) {
            if (psiElement instanceof PsiMethod)
                return (PsiMethod) psiElement;
            psiElement = psiElement.getContext();
        }
        return null;
    }

    public static boolean isInClassFile(PsiElement psiElement) {
        VirtualFile virtualFile = psiElement.getContainingFile().getVirtualFile();
        return virtualFile == null || virtualFile.getName().endsWith(".class");
    }

    public static boolean isInJarFileSystem(PsiElement psiElement) {
        if (psiElement == null)
            return false;
        VirtualFile virtualFile = psiElement.getContainingFile().getVirtualFile();
        if (virtualFile == null)
            return true;
        String protocol = virtualFile.getFileSystem().getProtocol();
        return protocol.equalsIgnoreCase("jar") || protocol.equalsIgnoreCase("zip");
    }

    public static VirtualFile findVirtualFile(PsiClass psiClass) {
        return psiClass.getNavigationElement().getContainingFile().getVirtualFile();
    }

    public static PsiMethod findPsiMethod(PsiMethod[] psiMethods, String methodName, List<String> argTypes) {
        for (PsiMethod psiMethod : psiMethods) {
            if (PsiUtils.isMethod(psiMethod, methodName, argTypes))
                return psiMethod;
        }
        return null;
    }

    public static boolean isMethod(PsiMethod psiMethod, String methodName, List<String> argTypes) {
        if (!psiMethod.getName().equals(methodName))
            return false;
        PsiParameter[] psiParameters = psiMethod.getParameterList().getParameters();
        return parameterEquals(psiParameters, argTypes);
    }

    public static boolean isAbstract(PsiClass psiClass) {
        return psiClass != null
                && (psiClass.isInterface()
                        || psiClass.getModifierList() != null
                        && psiClass.getModifierList().hasModifierProperty(PsiModifier.ABSTRACT)
        );
    }

    public static boolean isExternal(PsiClass psiClass) {
        return isInClassFile(psiClass) || isInJarFileSystem(psiClass);
    }

    public static PsiClass findPsiClass(PsiManager psiManager, String className) {
        return ClassUtil.findPsiClass(psiManager, className);
    }

    public static PsiMethod findPsiMethod(PsiManager psiManager,
                                          final String className, String methodName, List<String> argTypes) {
        PsiClass psiClass = ClassUtil.findPsiClass(psiManager, className);
        if (psiClass == null)
            return null;
        return findPsiMethod(psiClass, methodName, argTypes);
    }

    @Nullable
    public static PsiMethod findPsiMethod(PsiClass psiClass, String methodName, List<String> argTypes) {
        PsiMethod[] psiMethods = psiClass.findMethodsByName(methodName, false);
        if (psiMethods.length == 0)
            return null;
        return PsiUtils.findPsiMethod(psiMethods, methodName, argTypes);
    }

    public static String getPackageName(PsiMethod psiMethod) {
        PsiElement psiElement = psiMethod.getParent();
        while (psiElement != null) {
            if (psiElement instanceof PsiJavaFile) {
                PsiJavaFile psiJavaFile = (PsiJavaFile) psiElement;
                return psiJavaFile.getPackageName();
            }
            psiElement = psiElement.getParent();
        }
        return null;
    }

    public static boolean isPipeline(PsiCallExpression callExpression) {
        PsiElement[] children = callExpression.getChildren();
        for (PsiElement child : children) {
            if (child instanceof PsiReferenceExpression) {
                for (PsiElement psiElement : child.getChildren()) {
                    if (psiElement instanceof PsiMethodCallExpression) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public static boolean isComplexCall(PsiCallExpression callExpression) {
        PsiExpressionList argumentList = callExpression.getArgumentList();
        if (argumentList != null) {
            PsiExpression[] expressions = argumentList.getExpressions();
            for (PsiExpression expression : expressions) {
                if (expression instanceof PsiCallExpression) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isInterface(PsiModifierList psiModifierList) {
        PsiElement parent = psiModifierList.getParent();
        if (parent instanceof PsiClass) {
            PsiClass psiClass = (PsiClass) parent;
            return psiClass.isInterface();
        }
        return false;
    }

    public static PsiMethod findEnclosedPsiMethod(PsiLambdaExpression expression) {
        PsiElement parent = expression.getParent();
        while (!(parent instanceof PsiMethod)) {
            parent = parent.getParent();
        }
        return (PsiMethod) parent;
    }

    public static boolean isLambdaExpression(PsiLambdaExpression expression, List<String> argTypes, String returnType) {
        PsiType psiType = expression.getFunctionalInterfaceType();
        if (psiType == null)
            return false;
        if (!Objects.equals(returnType, psiType.getCanonicalText())) {
            return false;
        }

        PsiParameter[] psiParameters = expression.getParameterList().getParameters();
        return parameterEquals(psiParameters, argTypes);
    }

    private static boolean parameterEquals(PsiParameter[] psiParameters, List<String> argTypes) {
        if (psiParameters.length != argTypes.size())
            return false;
        for (int i = 0; i < psiParameters.length; i++) {
            PsiParameter psiParameter = psiParameters[i];
            if (!psiParameter.getType().getCanonicalText().equals(argTypes.get(i)))
                return false;
        }
        return true;
    }

    public static void openInEditor(String className,int lineNumber,int columnNumber, Project project){
        PsiClass matchedClass = ClassUtil.findPsiClass(PsiManager.getInstance(project), className);

        if(matchedClass !=null){

            VirtualFile virtualFile = PsiUtils.findVirtualFile(matchedClass);
            FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);

            //lineNumber -1
            OpenFileDescriptor openFileDescriptor = new OpenFileDescriptor(project,virtualFile,lineNumber-1,columnNumber);

            fileEditorManager.openTextEditor(openFileDescriptor,true);
        }
    }

}
