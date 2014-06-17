package de.crawling.spider.idea.plugin.mapper.gui.listeners;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.containers.SortedList;
import de.crawling.spider.idea.plugin.mapper.gui.PluginMainDialog;
import de.crawling.spider.idea.plugin.mapper.produce.ProduceSetterMethodList;
import de.crawling.spider.idea.plugin.mapper.util.PsiMethodComparator;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;

import static org.apache.commons.lang.StringUtils.trim;

/**
 * Created by sscheffler on 08.06.14.
 */
public class SetterPsiClassFindKeyListner implements KeyListener {

    private JBTextField textField;
    private CollectionListModel<PsiMethod> methodModel;
    private GlobalSearchScope scope;
    private JavaPsiFacade javaFacade;
    private PluginMainDialog pluginMainDialog;

    public SetterPsiClassFindKeyListner(PluginMainDialog pluginMainDialog, JBTextField textField, CollectionListModel<PsiMethod> methodModel, GlobalSearchScope scope, JavaPsiFacade javaFacade) {
        this.pluginMainDialog = pluginMainDialog;
        this.textField = textField;
        this.methodModel = methodModel;
        this.scope = scope;
        this.javaFacade = javaFacade;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        performKeyHandling();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        performKeyHandling();


    }

    @Override
    public void keyReleased(KeyEvent e) {
        performKeyHandling();
    }

    private void performKeyHandling() {

        String fieldValue = trim(textField.getText());
        PsiClass psiClass = javaFacade.findClass(fieldValue, scope);
        if(null != psiClass){
            pluginMainDialog.setSetterColor(Color.GREEN);
            if (psiClass != pluginMainDialog.getSelectedSetterClass()) {
                pluginMainDialog.setSelectedSetterClass(psiClass);

                ProduceSetterMethodList produceSetterMethodList = new ProduceSetterMethodList();
                List<PsiMethod> psiMethods = produceSetterMethodList.produceSetterMethodLists(psiClass);
                pluginMainDialog.replaceSelectedMethods(psiMethods);
            }
        }else{
            pluginMainDialog.setSetterColor(pluginMainDialog.getOriginalTextFieldBackGroundColor());
        }
    }

}
