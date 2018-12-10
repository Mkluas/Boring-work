package cn.mklaus.plugin.persistent;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author klaus
 * @date 2018/12/8 12:20 PM
 */
@Data
@State(name = "Global", storages = @Storage("boring-work-setting.xml"))
public class GlobalStateComponent implements PersistentStateComponent<GlobalStateComponent> {

    private String author;

    public GlobalStateComponent() {
        if (author == null) {
            author = "";
        }
    }

    public static GlobalStateComponent getInstance() {
        return ServiceManager.getService(GlobalStateComponent.class);
    }

    @Nullable
    @Override
    public GlobalStateComponent getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull GlobalStateComponent state) {
        XmlSerializerUtil.copyBean(state, this);
    }

}
