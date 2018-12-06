package cn.mklaus.plugin.ui.view;

import cn.mklaus.plugin.Template;
import cn.mklaus.plugin.persistent.TemplateStateComponent;
import cn.mklaus.plugin.service.GenerateService;
import cn.mklaus.plugin.util.Langs;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiPackage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author klaus
 * @date 2018/12/3 5:32 PM
 */
public class GenerateOptionsDialog extends JDialog {
    private JPanel mainPanel;
    private JButton okButton;
    private JButton cancelButton;
    private JTextField packageField;
    private JButton packageChooseButton;
    private JCheckBox allCheckBox;
    private JPanel templatePanel;

    private Project project;
    private List<PsiClass> classes;
    private List<JCheckBox> checkBoxList;
    private List<Template> templateList;

    public GenerateOptionsDialog(Project project, List<PsiClass> classes) {
        this.project = project;
        this.classes = classes;
        this.templateList = TemplateStateComponent.getInstance().getTemplateList();
        setContentPane(mainPanel);
        setModal(true);
        this.initView();
    }

    private void initView() {
        checkBoxList = new ArrayList<>();
        templatePanel.setLayout(new GridLayout(6,2));
        templateList.forEach(t -> {
            JCheckBox cb = new JCheckBox(t.getName());
            checkBoxList.add(cb);
            templatePanel.add(cb);
        });
        allCheckBox.addActionListener(e -> checkBoxList.forEach(cb -> cb.setSelected(allCheckBox.isSelected())));

        packageChooseButton.addActionListener(e -> {
            PackageChooserDialog dialog = new PackageChooserDialog("Package Chooser", project);
            dialog.show();
            PsiPackage psiPackage = dialog.getSelectedPackage();
            if (psiPackage != null) {
                packageField.setText(psiPackage.getQualifiedName());
            }
        });

        okButton.addActionListener(e -> onOk());
        cancelButton.addActionListener(e -> dispose());


        if (!classes.isEmpty()) {
            PsiClass psiClass = classes.get(0);
            String qualifiedName = psiClass.getQualifiedName();
            if (qualifiedName.contains(".entity." + psiClass.getName())) {
                packageField.setText(qualifiedName.substring(0, qualifiedName.indexOf(".entity." + psiClass.getName())));
            } else {
                packageField.setText(qualifiedName.substring(0, qualifiedName.indexOf("." + psiClass.getName())));
            }
        }
    }

    private void onOk() {
        List<Template> selectedTemplateList = getSelectedTemplateList();
        if (selectedTemplateList.isEmpty()) {
            Messages.showWarningDialog("Please select at least one template", "WARNING");
            return;
        }

        String packagePath = packageField.getText();
        if (Langs.isBlankString(packagePath)) {
            Messages.showWarningDialog("Please choose base package", "WARNING");
            return;
        }

        boolean isUserExist = Langs.isClassExists(packagePath + ".entity.User");
        System.out.println("isUserExist = " + isUserExist);

        GenerateService.getInstance(project).generate(classes, selectedTemplateList, packagePath);
        dispose();
    }

    private List<Template> getSelectedTemplateList() {
        List<String> names = checkBoxList.stream()
                .filter(JCheckBox::isSelected)
                .map(JCheckBox::getText)
                .collect(Collectors.toList());
        return templateList.stream()
                .filter(t -> names.contains(t.getName()))
                .collect(Collectors.toList());
    }

    public void open() {
        this.pack();
        setLocationRelativeTo(null);
        this.setVisible(true);
    }

}
