package de.crawling.spider.idea.plugin.mapper.gui;

import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by sscheffler on 07.06.14.
 */
public class PluginMainDialog extends DialogWrapper {


    public static final String TITLE = "Smart Mapping";

    private CollectionListModel<PsiField> fieldModel;
    private LabeledComponent<JPanel> fieldListPanel;

    public PluginMainDialog(@Nullable Project project, PsiClass psiClass) {
        super(project);
        init();
        setTitle(TITLE);

        if(null != psiClass){

            fieldModel = new CollectionListModel<>(psiClass.getAllFields());
        }

        //TODO: use instead
//        JBList fieldList = new JBList(fieldModel);
        JList fieldList = new JList(fieldModel);
        fieldList.setCellRenderer(new DefaultPsiElementCellRenderer());

        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fieldList);
        JPanel panel = decorator.createPanel();
        fieldListPanel = LabeledComponent.create(panel, "FieldList");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return fieldListPanel;
    }




}
