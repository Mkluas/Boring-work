package cn.mklaus.plugin.action;

import cn.mklaus.plugin.Template;
import cn.mklaus.plugin.persistent.TemplateStateComponent;
import cn.mklaus.plugin.service.GenerateService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author klaus
 * @date 2018/11/30 11:42 AM
 */
public class BoringWork extends AnAction {

    public BoringWork() {
        super("Boring Work");
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        List<PsiClass> classes = new ArrayList<>();

        PsiElement element = e.getData(CommonDataKeys.PSI_ELEMENT);
        if (element instanceof PsiClass) {
            classes.add((PsiClass) element);
        } else if (element instanceof PsiDirectory) {

            for (PsiFile file: ((PsiDirectory) element).getFiles()) {
                if (file instanceof PsiJavaFile) {
                    PsiJavaFile psiJavaFile = (PsiJavaFile)file;
                    classes.addAll(Arrays.asList(psiJavaFile.getClasses()));
                }
            }
        }

        if (!classes.isEmpty()) {
            List<Template> templateList = TemplateStateComponent.getInstance().getTemplateList();
            GenerateService.getInstance(project).generate(classes, templateList);
        }

    }

}
