package cn.mklaus.plugin.ui;

import cn.mklaus.plugin.persistent.GlobalStateComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * @author klaus
 * @date 2018/11/29 2:57 PM
 */
public class MainSetting implements Configurable, Configurable.Composite {

    private JPanel mainPanel;
    private JPanel formPanel;

    private String author;

    public MainSetting() {
        this.author = GlobalStateComponent.getInstance().getAuthor();
        initView();
    }

    private void initView() {
        this.mainPanel = new JPanel(new BorderLayout());
        this.formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        formPanel.add(new Label("Author"));
        JTextField authorTextField = new JTextField(18);
        authorTextField.setText(this.author);
        formPanel.add(authorTextField);
        authorTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                author = authorTextField.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) { }
            @Override
            public void changedUpdate(DocumentEvent e) { }
        });

        this.mainPanel.add(formPanel, BorderLayout.NORTH);
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
        return !GlobalStateComponent.getInstance().getAuthor().equals(author);
    }

    @Override
    public void apply() throws ConfigurationException {
        GlobalStateComponent.getInstance().setAuthor(author);
    }

}
