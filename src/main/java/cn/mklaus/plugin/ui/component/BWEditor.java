package cn.mklaus.plugin.ui.component;

import cn.mklaus.plugin.util.Langs;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.testFramework.LightVirtualFile;

import javax.swing.*;

/**
 * @author klaus
 * @date 2018/11/29 7:10 PM
 */
public class BWEditor {

    private static FileType VELOCITY_FILE_TYPE = FileTypeManager.getInstance().getFileTypeByExtension("vm");
    private static String TEMP_FILE_NAME = "template.vm";
    private Editor editor;

    private Project project;
    private DocumentListener listener;
    private String initialText;

    public BWEditor(DocumentListener listener) {
        this(Langs.currentProject(), listener);
    }

    public BWEditor(Project project, DocumentListener listener) {
        this(project, "" , listener);
    }

    public BWEditor(Project project,  String initialText, DocumentListener listener) {
        this.project = project;
        this.listener = listener;
        this.initialText = initialText;
        this.editor = initEditor();
    }

    private Editor initEditor() {
        PsiFileFactory psiFileFactory = PsiFileFactory.getInstance(project);
        PsiFile psiFile = psiFileFactory.createFileFromText(TEMP_FILE_NAME, VELOCITY_FILE_TYPE, initialText, 0, true);
        psiFile.getViewProvider().putUserData(FileTemplateManager.DEFAULT_TEMPLATE_PROPERTIES, FileTemplateManager.getInstance(project).getDefaultProperties());

        PsiDocumentManager manager = PsiDocumentManager.getInstance(project);
        Document document = manager.getDocument(psiFile);
        assert document != null;

        EditorFactory factory = EditorFactory.getInstance();
        Editor editor = factory.createEditor(document, project);
        editor.getDocument().addDocumentListener(this.listener);

        EditorSettings editorSettings = editor.getSettings();
        editorSettings.setVirtualSpace(false);
        editorSettings.setLineMarkerAreaShown(false);
        editorSettings.setIndentGuidesShown(false);
        editorSettings.setLineNumbersShown(true);
        editorSettings.setFoldingOutlineShown(true);
        editorSettings.setAdditionalColumnsCount(3);
        editorSettings.setAdditionalLinesCount(3);
        editorSettings.setCaretRowShown(false);

        return editor;
    }

    public void setText(String text) {
        ((EditorEx) editor).setHighlighter(EditorHighlighterFactory.getInstance().createEditorHighlighter(project, new LightVirtualFile("template_highlight.vm")));
        WriteCommandAction.runWriteCommandAction(project, ()-> this.editor.getDocument().setText(text));
    }

    public JComponent getComponent() {
        return editor.getComponent();
    }

}
