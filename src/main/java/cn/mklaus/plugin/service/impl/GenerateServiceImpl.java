package cn.mklaus.plugin.service.impl;

import cn.mklaus.plugin.Template;
import cn.mklaus.plugin.service.GenerateService;
import cn.mklaus.plugin.util.Langs;
import cn.mklaus.plugin.velocity.VelocityUtils;
import com.intellij.psi.PsiClass;
import org.jetbrains.annotations.SystemIndependent;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author klaus
 * @date 2018/11/29 11:52 AM
 */
public class GenerateServiceImpl implements GenerateService {

    @Override
    public void generate(List<PsiClass> classes, List<Template> templates) {
        for(PsiClass psiClass : classes) {
            for (Template template : templates) {
                generate(psiClass, template);
            }
        }
    }

    private void generate(PsiClass psiClass, Template template) {
        String className = psiClass.getName();
        Map<String, Object> map = new HashMap<>(8);
        map.put("className", className);
        String contentText = VelocityUtils.generate(template.getContent(), map);
        String basePath = psiClass.getProject().getBasePath();
        System.out.println("basePath = " + basePath);
        @SystemIndependent String projectFilePath = psiClass.getProject().getProjectFilePath();
        System.out.println("projectFilePath = " + projectFilePath);
        File file = new File(basePath + "/" + template.getPrefixDir().replace(".", "/") + "/" + template.getName());
        Langs.write(file, contentText);
    }

}
