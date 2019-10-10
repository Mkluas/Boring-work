package cn.mklaus.plugin.util;

import cn.mklaus.plugin.Template;
import com.intellij.ide.fileTemplates.impl.UrlUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.ExceptionUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author klaus
 * @date 2018/11/29 3:06 PM
 */
public class Langs {

    public static String capital(String name) {
        if (isBlankString(name)) {
            return "";
        } else {
            return name.substring(0,1).toUpperCase() + name.substring(1);
        }
    }

    public static Project currentProject() {
        ProjectManager projectManager = ProjectManager.getInstance();
        Project[] openProjects = projectManager.getOpenProjects();
        return openProjects.length > 0 ? openProjects[0] : projectManager.getDefaultProject();
    }

    public static String loadContentText(String path) {
        URL resource = Langs.class.getClassLoader().getResource(path);
        try {
            return UrlUtil.loadText(resource);
        } catch (IOException e) {
            return "Load " + path + ": " +e.toString();
        }
    }

    public static String showInputDialog(String title, String message, String initialValue) {
        return Messages.showInputDialog(message, title, Messages.getQuestionIcon(), initialValue, new InputValidator() {
            @Override
            public boolean checkInput(String inputString) {
                return !(inputString == null || inputString.trim().length() == 0);
            }
            @Override
            public boolean canClose(String inputString) {
                return this.checkInput(inputString);
            }
        });
    }

    public static boolean isBlankString(String text) {
        return text == null || text.trim().length() == 0;
    }

    public static List<Template> cloneTemplateList(List<Template> templates) {
       List<Template> list = new ArrayList<>();
       templates.forEach(template -> list.add(template.clone()));
       return list;
    }

    public static boolean isClassExists(String qualifiedName) {
        return JavaPsiFacade.getInstance(currentProject()).findClass(qualifiedName, GlobalSearchScope.allScope(currentProject())) != null;
    }

    /**
     * 写入文件内容
     * @param file 文件
     * @param content 内容
     */
    public static void write(File file, String content) {
        write(file, content, false);
    }

    /**
     * 写入文件内容
     * @param file 文件
     * @param content 文件内容
     * @param append 是否为追加模式
     */
    public static void write(File file, String content, boolean append) {
        try {
            FileUtil.writeToFile(file, content, append);
        } catch (IOException e) {
            ExceptionUtil.rethrow(e);
        }
    }
}
