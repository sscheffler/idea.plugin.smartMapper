package de.crawling.spider.idea.plugin.mapper.gui.listeners;

import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.components.JBTextField;
import de.crawling.spider.idea.plugin.mapper.gui.PluginMainDialog;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static org.apache.commons.lang.StringUtils.trim;

/**
 * Created by sscheffler on 08.06.14.
 */
public class GetterPsiClassFindKeyListner implements KeyListener {

    private JBTextField textField;
    private GlobalSearchScope scope;
    private JavaPsiFacade javaFacade;
    private PluginMainDialog pluginMainDialog;

    public GetterPsiClassFindKeyListner(PluginMainDialog pluginMainDialog, JBTextField textField, GlobalSearchScope scope, JavaPsiFacade javaFacade) {
        this.pluginMainDialog = pluginMainDialog;
        this.textField = textField;
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
            pluginMainDialog.setGetterColor(Color.GREEN);

            if (psiClass != pluginMainDialog.getSelectedGetterPsiClass()) {
                pluginMainDialog.setSelectedGetterClass(psiClass);
            }
        }else{
            pluginMainDialog.setGetterColor(pluginMainDialog.getOriginalTextFieldBackGroundColor());
        }
    }

}
