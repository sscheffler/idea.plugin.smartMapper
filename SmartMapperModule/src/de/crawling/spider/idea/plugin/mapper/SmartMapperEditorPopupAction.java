package de.crawling.spider.idea.plugin.mapper;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import de.crawling.spider.idea.plugin.mapper.gui.PluginMainDialog;
import de.crawling.spider.idea.plugin.mapper.map.DefaultSmartMapperImpl;
import de.crawling.spider.idea.plugin.mapper.map.SmartMapper;
import de.crawling.spider.idea.plugin.mapper.update.DefaultUpdaterImpl;
import de.crawling.spider.idea.plugin.mapper.update.Updater;

import javax.swing.*;

/**
 * Created by sscheffler on 24.05.14.
 */
public class SmartMapperEditorPopupAction extends AnAction {

    private Updater updater;
    private SmartMapper smartMapper = new DefaultSmartMapperImpl();

    public void actionPerformed(AnActionEvent e) {
        startMainDialog(e);
    }

    private void startMainDialog(AnActionEvent e){
        try {
            final Project project = e.getProject();
            PluginMainDialog dialog = new PluginMainDialog(project);
            dialog.show();
            MapperProperties properties = dialog.getMapperProperties();

            if(dialog.isOK() && properties.propertiesValid()){
                updater = new DefaultUpdaterImpl(project);
                String methodString = smartMapper.buildMapperMethod(properties);
                updater.updateClassWithMethod(methodString, dialog.getSelectedSetterClass());
            }

        }catch(IllegalArgumentException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "No editor open", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private PsiClass getPsiClass(AnActionEvent e){
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(LangDataKeys.EDITOR);

        int cursorPostion = editor.getCaretModel().getOffset();
        PsiElement psiElement = psiFile.findElementAt(cursorPostion);
        return PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
    }




}
