package cn.mklaus.plugin.service;

import cn.mklaus.plugin.Template;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author klaus
 * @date 2018/11/29 11:48 AM
 */
public interface GenerateService {

    static GenerateService getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, GenerateService.class);
    }

    void generate(List<PsiClass> classes, List<Template> templates);

}
