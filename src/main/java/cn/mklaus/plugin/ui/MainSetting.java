package cn.mklaus.plugin.ui;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * @author klaus
 * @date 2018/11/29 2:57 PM
 */
public class MainSetting implements Configurable, Configurable.Composite {

    private JPanel mainPanel;

    public MainSetting() {
        this.mainPanel = new JPanel(new BorderLayout());
        this.mainPanel.add(new Label("Welcome"), BorderLayout.CENTER);
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Boring Work";
    }

    @NotNull
    @Override
    public Configurable[] getConfigurables() {
        Configurable[] subMenu = new Configurable[1];
        subMenu[0] = new TemplateSetting();
        return subMenu;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return mainPanel;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
