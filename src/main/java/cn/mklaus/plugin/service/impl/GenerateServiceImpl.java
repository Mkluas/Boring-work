package cn.mklaus.plugin.service.impl;

import cn.mklaus.plugin.Entity;
import cn.mklaus.plugin.Template;
import cn.mklaus.plugin.service.GenerateService;
import cn.mklaus.plugin.util.Langs;
import cn.mklaus.plugin.velocity.Callback;
import cn.mklaus.plugin.velocity.VelocityUtils;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiClass;

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
    public void generate(List<PsiClass> classes, List<Template> templates, String packagePath) {
        for(PsiClass psiClass : classes) {
            for (Template template : templates) {
                generate(psiClass, template, packagePath);
            }
        }
        VirtualFileManager.getInstance().syncRefresh();
    }

    private void generate(PsiClass psiClass, Template template, String packagePath) {
        Map<String, Object> map = new HashMap<>(8);
        Callback callback = new Callback();
        Entity entity = new Entity(psiClass);
        map.put("entity", entity);
        map.put("callback", callback);
        map.put("basePackage", packagePath);
        map.put("package", packagePath + "." + template.getPrefixDir());

        String contentText = VelocityUtils.generate(template.getContent(), map);
        File saveFile = getSaveFile(psiClass, template, packagePath, callback.getFilename());
        Langs.write(saveFile, contentText);
    }

    private File getSaveFile(PsiClass psiClass, Template template, String packagePath, String filename) {
        String basePath = psiClass.getProject().getBasePath();
        String relatePath = template.getRelativePath().equals(Template.PATH.PACKAGE_BASE) ? "src/main/java/" + packagePath :
                (template.getRelativePath().equals(Template.PATH.RESOURCES_BASE) ? "src/main/resources" : "");
        String savePath = basePath + File.separator + relatePath + File.separator+ template.getPrefixDir();
        return new File(savePath.replace(".", File.separator), filename);
    }

}
