package cn.mklaus.plugin.ui;

import cn.mklaus.plugin.Template;
import cn.mklaus.plugin.persistent.TemplateStateComponent;
import cn.mklaus.plugin.ui.view.MainSettingView;
import cn.mklaus.plugin.util.Langs;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author klaus
 * @date 2018/11/29 9:55 PM
 */
public class TemplateSetting implements Configurable, MainSettingView.Delegate {

    private MainSettingView mainView;
    private List<Template> templates;

    public TemplateSetting() {
        this.templates = Langs.cloneTemplateList(TemplateStateComponent.getInstance().getTemplateList());
        this.mainView = new MainSettingView(this);
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Template Setting";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return mainView;
    }

    @Override
    public boolean isModified() {
        List<Template> list = Langs.cloneTemplateList(templates);
        return !TemplateStateComponent.getInstance().isTemplateListEqual(list);
    }

    @Override
    public void apply() throws ConfigurationException {
        Map<String, Template> maps = new HashMap<>(templates.size() * 2);
        templates.forEach(t->maps.put(t.getName(), t));
        TemplateStateComponent.getInstance().setTemplateMap(maps);
    }


    /**
     * FOR delegate
     */


    @Override
    public List<Template> getTemplateList() {
        return templates;
    }

    @Override
    public void createTemplate(String templateName) {
        System.out.println("templates = " + templates);
        templates.add(templates.size(), new Template(templateName));
        System.out.println("templates = " + templates);
    }

    @Override
    public void removeTemplate(int selectedIndex) {
        System.out.println("templates = " + templates);
        System.out.println("remove " + templates.get(selectedIndex));
        templates.remove(selectedIndex);
    }

    @Override
    public void reset() {
        this.templates = Langs.cloneTemplateList(TemplateStateComponent.getInstance().getTemplateList());
        mainView.refresh();
    }
}
