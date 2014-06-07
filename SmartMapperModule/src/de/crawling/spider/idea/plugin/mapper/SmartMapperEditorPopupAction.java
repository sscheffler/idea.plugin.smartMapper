package de.crawling.spider.idea.plugin.mapper;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import de.crawling.spider.idea.plugin.mapper.gui.PluginMainDialog;

import javax.swing.*;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * Created by sscheffler on 24.05.14.
 */
public class SmartMapperEditorPopupAction extends AnAction {

    private final SmartMapper smartMapper = new SmartMapper();
    private Updater updater;

    public void actionPerformed(AnActionEvent e) {
        startMainDialog(e);
    }

    private void startMainDialog(AnActionEvent e){
        try {
            final Project project = e.getProject();
            PluginMainDialog dialog = new PluginMainDialog(project);
            dialog.show();
            if(dialog.isOK() && null != dialog.getSelectedSetterClass()){
                PsiClass getterClass = dialog.getSelectedGetterPsiClass();
                updater = new Updater(project);
                List<PsiMethod> methods = dialog.getSelectedSetterMethods();

                String methodString = smartMapper.getSimpleMappingMethodForSelecion(
                        dialog.getCannonicalSetterClassName(),
                        methods,
                        project
                );

                if(isNotBlank(methodString)) {
                    updater.updateClassWithCreatingNewMethod(methodString, dialog.getSelectedSetterClass());
                }else{
                    return;
                }
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
