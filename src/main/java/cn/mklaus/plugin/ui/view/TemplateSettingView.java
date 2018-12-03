package cn.mklaus.plugin.ui.view;

import cn.mklaus.plugin.Template;
import cn.mklaus.plugin.ui.component.BWEditor;
import cn.mklaus.plugin.util.Langs;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Splitter;
import com.intellij.ui.*;
import com.intellij.ui.border.CustomLineBorder;
import com.intellij.ui.components.JBList;

import javax.swing.*;

import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author klaus
 * @date 2018/11/29 7:23 PM
 */
public class TemplateSettingView extends JPanel implements ListSelectionListener, DocumentListener {

    private Delegate delegate;

    private Splitter splitter;
    private JPanel rightPanel;
    private JBList<String> jbList;
    private BWEditor bwEditor;
    ComboBox<Template.PATH> comboBox;
    JTextField prefixTextField;

    private int selectedIndex = -1;
    private Template selectedTemplate;


    public TemplateSettingView(Delegate delegate) {
        this.delegate = delegate;
        initView();
        refresh();
        hideRightPanel();
    }

    private void showRightPanel() {
        splitter.setSecondComponent(rightPanel);
    }

    private void hideRightPanel() {
        splitter.setSecondComponent(new JPanel(new BorderLayout()));
    }

    private void initView() {
        splitter = new Splitter(false, 0.2f);
        splitter.setFirstComponent(getLeftPanel());
        splitter.setSecondComponent(getRightPanel());

        setLayout(new BorderLayout());
        add(splitter, BorderLayout.CENTER);
    }

    private JPanel getLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        jbList = createJBList();
        leftPanel.add(getLeftToolBar().getComponent(), BorderLayout.NORTH);
        leftPanel.add(jbList, BorderLayout.CENTER);
        return leftPanel;
    }

    private JPanel getRightPanel() {
        rightPanel = new JPanel(new BorderLayout());
        bwEditor = new BWEditor(this);
        Splitter splitter = new Splitter(true, 0.6f);
        splitter.setFirstComponent(bwEditor.getComponent());
        splitter.setSecondComponent(getDescriptionPanel());
        rightPanel.add(splitter, BorderLayout.CENTER);

        rightPanel.add(getRightToolBar(), BorderLayout.NORTH);
        return rightPanel;
    }

    /**
     * 模板操作工具栏
     */
    private ActionToolbar getLeftToolBar() {
        DefaultActionGroup actionGroup = new DefaultActionGroup();

        actionGroup.add(new AnAction(AllIcons.General.Add) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                String templateName = Langs.showInputDialog("Create template", "Please input template name", "");
                if (Langs.isBlankString(templateName)) { return; }
                delegate.createTemplate(templateName);
                refresh();
                jbList.setSelectedIndex(delegate.getTemplateList().size() - 1);
            }
        });

        actionGroup.add(new AnAction(AllIcons.General.Remove) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                delegate.removeTemplate(jbList.getSelectedIndex());
                int index = selectedIndex;
                refresh();
                int size = delegate.getTemplateList().size();
                if (index < size) {
                    jbList.setSelectedIndex(index);
                } else if (size > 0) {
                    jbList.setSelectedIndex(size - 1);
                } else {
                    hideRightPanel();
                }
            }

            @Override
            public void update(AnActionEvent e) {
                e.getPresentation().setEnabled(selectedTemplate != null);
            }
        });

        actionGroup.add(new AnAction(AllIcons.Actions.Edit) {
            @Override
            public void actionPerformed(AnActionEvent e) {
                String templateName = Langs.showInputDialog("Create template", "Please input template name", selectedTemplate.getName());
                if (Langs.isBlankString(templateName)) { return; }
                selectedTemplate.setName(templateName);
                int index = selectedIndex;
                refresh();
                jbList.setSelectedIndex(index);
            }

            @Override
            public void update(AnActionEvent e) {
                e.getPresentation().setEnabled(selectedTemplate != null);
            }
        });

        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("Item Toolbar", actionGroup, true);
        toolbar.getComponent().setBorder(new CustomLineBorder(1,1,1,1));
        return toolbar;
    }

    /**
     * 模板列表
     */
    private JBList createJBList() {
        JBList<String> jbList = new JBList<>();
        jbList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jbList.setBorder(new CustomLineBorder(0,1,1,1));
        jbList.addListSelectionListener(this);
        return jbList;
    }

    /**
     * 保存路径面板
     */
    private JPanel getRightToolBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new Label("Relative path:"));

        List<Template.PATH> paths = Arrays.asList(Template.PATH.values());
        CollectionComboBoxModel<Template.PATH> comboBoxModel = new CollectionComboBoxModel<>(paths);
        comboBox = new ComboBox<>(comboBoxModel);
        comboBox.addItemListener(e -> selectedTemplate.setRelativePath((Template.PATH)comboBox.getSelectedItem()));

        panel.add(comboBox);
        panel.setBorder(new CustomLineBorder(1,1,0,1));

        panel.add(new Label("Prefix dir:"));
        prefixTextField = new JTextField(15);
        panel.add(prefixTextField);

        prefixTextField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                selectedTemplate.setPrefixDir(prefixTextField.getText());
            }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { }
        });

        return panel;
    }

    /**
     * 描述面板
     */
    private JPanel getDescriptionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        JEditorPane descPane = new JEditorPane();
        descPane.setEditorKit(UIUtil.getHTMLEditorKit());
        descPane.setText("<h3>This is a description.<h3/>");
        descPane.setEditable(false);
        descPane.addHyperlinkListener(new BrowserHyperlinkListener());

        panel.add(SeparatorFactory.createSeparator("简介", null), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                JBUI.insetsBottom(2), 0, 0));
        panel.add(ScrollPaneFactory.createScrollPane(descPane),
                new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        JBUI.insetsTop(2), 0, 0));
        return panel;
    }

    public void refresh() {
        List<String> names = delegate.getTemplateList().stream().map(Template::getName).collect(Collectors.toList());
        CollectionListModel<String> listModel = new CollectionListModel<>(names);
        jbList.setModel(listModel);
    }

    /**
     * JBList ListSelectionListener
     * @param e event
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        selectedIndex = jbList.getSelectedIndex();
        if (selectedIndex > -1 && selectedIndex < delegate.getTemplateList().size()) {
            showRightPanel();
            selectedTemplate = delegate.getTemplateList().get(selectedIndex);
            bwEditor.setText(selectedTemplate.getContent());
            comboBox.setSelectedIndex(selectedTemplate.getRelativePath().ordinal());
            prefixTextField.setText(selectedTemplate.getPrefixDir());
        } else {
            selectedTemplate = null;
        }
    }

    /**
     * Editor DocumentListener
     * @param e event
     */
    @Override
    public void documentChanged(DocumentEvent e) {
        String content = e.getDocument().getText();
        if (selectedTemplate != null) {
            selectedTemplate.setContent(content);
        }
    }

    public interface Delegate {
        List<Template> getTemplateList();
        void createTemplate(String templateName);
        void removeTemplate(int selectedIndex);
    }
}
