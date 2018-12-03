package cn.mklaus.plugin.persistent;

import cn.mklaus.plugin.Template;
import cn.mklaus.plugin.util.Langs;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author klaus
 * @date 2018/11/29 3:43 PM
 */
@Data
@State(name = "TemplateMap", storages = @Storage("boring-work-setting.xml"))
public class TemplateStateComponent implements PersistentStateComponent<TemplateStateComponent> {

    private Map<String, Template> templateMap;

    private final static List<String> DEFAULT_TEMPLATE_LIST;
    private final static Map<String, Template> DEFAULT_TEMPLATE_MAP;


    public TemplateStateComponent() {
        this.templateMap = new HashMap<>(8);
        DEFAULT_TEMPLATE_LIST.forEach(s -> this.templateMap.putIfAbsent(s, DEFAULT_TEMPLATE_MAP.get(s)));
    }

    public static TemplateStateComponent getInstance() {
        return ServiceManager.getService(TemplateStateComponent.class);
    }

    public boolean isTemplateListEqual(final List<Template> templates) {
        if (templates == null) { return this.templateMap.isEmpty(); }


        templates.sort(Comparator.comparing(Template::getName));
        return Arrays.equals(templates.toArray(), getTemplateList().toArray());
    }

    public List<Template> getTemplateList() {
        ArrayList<Template> list = new ArrayList<>(this.templateMap.values());
        list.sort(Comparator.comparing(Template::getName));
        return list;
    }

    @Nullable
    @Override
    public TemplateStateComponent getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull TemplateStateComponent state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    static {
        Map<String, Template> map = new HashMap<>();
        Template service = new Template("Service", Langs.loadContentText("template/Service.java.vm"), "service");
        Template serviceImpl = new Template("ServiceImpl", Langs.loadContentText("template/ServiceImpl.java.vm"), "service.impl");
        Template controller = new Template("Controller", Langs.loadContentText("template/Controller.java.vm"), "web");
        map.put("Service", service);
        map.put("ServiceImpl", serviceImpl);
        map.put("Controller", controller);

        DEFAULT_TEMPLATE_MAP = map;
        DEFAULT_TEMPLATE_LIST = Arrays.asList(map.keySet().toArray(new String[]{}));
    }
}