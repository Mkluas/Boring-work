package cn.mklaus.plugin.ui;

import cn.mklaus.plugin.Template;
import cn.mklaus.plugin.persistent.TemplateStateComponent;
import cn.mklaus.plugin.ui.view.TemplateSettingView;
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
public class TemplateSetting implements Configurable, TemplateSettingView.Delegate {

    private TemplateSettingView mainView;
    private List<Template> templates;

    public TemplateSetting() {
        this.templates = Langs.cloneTemplateList(TemplateStateComponent.getInstance().getTemplateList());
        this.mainView = new TemplateSettingView(this);
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
        Langs.cloneTemplateList(templates).forEach(t->maps.put(t.getName(), t));
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
        templates.add(templates.size(), new Template(templateName));
    }

    @Override
    public void removeTemplate(int selectedIndex) {
        templates.remove(selectedIndex);
    }

    @Override
    public void reset() {
        this.templates = Langs.cloneTemplateList(TemplateStateComponent.getInstance().getTemplateList());
        mainView.refresh();
    }
}
