package de.crawling.spider.idea.plugin.mapper;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import de.crawling.spider.idea.plugin.mapper.gui.PluginMainDialog;
import de.crawling.spider.idea.plugin.mapper.map.DefaultSmartMapperImpl;
import de.crawling.spider.idea.plugin.mapper.map.SmartMapper;
import de.crawling.spider.idea.plugin.mapper.produce.ProduceSetterMethodList;
import de.crawling.spider.idea.plugin.mapper.update.DefaultUpdaterImpl;
import de.crawling.spider.idea.plugin.mapper.update.Updater;
import de.crawling.spider.idea.plugin.mapper.util.MapperHelper;
import de.crawling.spider.idea.plugin.mapper.model.MapperProperties;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by sscheffler on 24.05.14.
 */
public class SmartMapperEditorPopupAction extends AnAction {

    private Updater updater;
    private SmartMapper smartMapper = new DefaultSmartMapperImpl();
    private MapperHelper mapperHelper = MapperHelper.INSTANCE;

    public void actionPerformed(AnActionEvent e) {
        startMainDialog(e);
    }

    private void startMainDialog(AnActionEvent e){
        try {
            PropertyConfigurator.configure("/home/sscheffler/.repository/idea.plugin.smartMapper/SmartMapperModule/src/main/resources/log4j.properties");
            final Project project = e.getProject();
            PluginMainDialog dialog = new PluginMainDialog(project);


            Editor editor = e.getData(PlatformDataKeys.EDITOR);

            PsiClass editorClass = mapperHelper.retrieveEditorClass(e);
            fillGetterTextField(project, editor, editorClass, dialog);




            dialog.show();
            MapperProperties properties = dialog.buildResults();

            if(dialog.isOK() && properties.propertiesValid()){

                properties.setEditorClass(editorClass);
                updater = new DefaultUpdaterImpl(project);
                String methodString = smartMapper.buildMapperMethod(properties);

                updater.updateClassWithMethod(methodString, properties);
            }

        }catch(IllegalArgumentException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "No editor or wrong file open", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void fillGetterTextField(Project project, Editor editor, PsiClass editorClass, PluginMainDialog dialog) {
        PsiClass setterClass = null;
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);

        int cursorPos = editor.getCaretModel().getOffset();
        PsiElement element = editorClass.getContainingFile().findElementAt(cursorPos);

        PsiTypeElement psiJavaCodeReferenceElement = PsiTreeUtil.getParentOfType(element, PsiTypeElement.class);
        if(null != psiJavaCodeReferenceElement) {

            String canonicalText = psiJavaCodeReferenceElement.getType().getCanonicalText();
            setterClass = JavaPsiFacade.getInstance(project).findClass(canonicalText, scope);
            if(null!= setterClass){
                dialog.setSelectedSetterClass(setterClass);
                dialog.setSetterTextFieldText(canonicalText);
                dialog.setSetterColor(Color.green);

                ProduceSetterMethodList produceSetterMethodList = new ProduceSetterMethodList();
                java.util.List<PsiMethod> psiMethods = produceSetterMethodList.produceSetterMethodLists(setterClass);
                dialog.replaceSelectedMethods(psiMethods);
            }
        }
    }


}
