package de.crawling.spider.idea.plugin.mapper;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiTypeElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import de.crawling.spider.idea.plugin.mapper.gui.PluginMainDialog;
import de.crawling.spider.idea.plugin.mapper.map.DefaultSmartMapperImpl;
import de.crawling.spider.idea.plugin.mapper.map.SmartMapper;
import de.crawling.spider.idea.plugin.mapper.update.DefaultUpdaterImpl;
import de.crawling.spider.idea.plugin.mapper.update.Updater;
import de.crawling.spider.idea.plugin.mapper.util.MapperHelper;
import de.crawling.spider.idea.plugin.mapper.model.MapperProperties;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * Created by sscheffler on 24.05.14.
 */
public class SmartMapperEditorPopupAction extends AnAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(SmartMapperEditorPopupAction.class);
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
                LOGGER.debug("Mapper properties are valid. Method will be build");

                properties.setEditorClass(editorClass);
                updater = new DefaultUpdaterImpl(project);
                String methodString = smartMapper.buildMapperMethod(properties);
                LOGGER.debug("Method: {}", methodString);

                updater.updateClassWithMethod(methodString, properties);
            }

        }catch(IllegalArgumentException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "No editor or wrong file open", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void fillGetterTextField(Project project, Editor editor, PsiClass editorClass, PluginMainDialog dialog) {
        PsiClass getterClass = null;
        LOGGER.debug("Check if a getter can be found");
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);

        int cursorPos = editor.getCaretModel().getOffset();
        PsiElement element = editorClass.getContainingFile().findElementAt(cursorPos);

        PsiTypeElement psiJavaCodeReferenceElement = PsiTreeUtil.getParentOfType(element, PsiTypeElement.class);
        if(null != psiJavaCodeReferenceElement) {

            String canonicalText = psiJavaCodeReferenceElement.getType().getCanonicalText();
            getterClass = JavaPsiFacade.getInstance(project).findClass(canonicalText, scope);
            if(null!= getterClass){
                dialog.setSelectedGetterClass(getterClass);
                dialog.setGetterTextFieldText(canonicalText);
                dialog.setGetterColor(Color.green);
            }
        }
    }


}
