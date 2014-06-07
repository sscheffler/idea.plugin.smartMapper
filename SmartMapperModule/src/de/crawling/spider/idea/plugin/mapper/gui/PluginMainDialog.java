package de.crawling.spider.idea.plugin.mapper.gui;

import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by sscheffler on 07.06.14.
 */
public class PluginMainDialog extends DialogWrapper {


    public static final String TITLE = "Smart Mapping";

    private CollectionListModel<PsiMethod> fieldModel;
    private LabeledComponent<JPanel> fieldListPanel;
    private GlobalSearchScope scope;
    private JavaPsiFacade javaFacade;
    private PsiClass selectedPsiClass;

    JBTextField jbTextField;
    JPanel p;
    JBTextField tfieldOne;

    public PluginMainDialog(@Nullable Project project, PsiClass psiClass) {
        super(project);
        scope = GlobalSearchScope.allScope(project);
        javaFacade = JavaPsiFacade.getInstance(project);
        setTitle(TITLE);

        if(null != psiClass){

            fieldModel = new CollectionListModel<>();
        }

        //TODO: use instead
        final JBList fieldList = new JBList(fieldModel);
        fieldList.setCellRenderer(new DefaultPsiElementCellRenderer());

        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(fieldList);
        JPanel panel = decorator.createPanel();
        p = new JPanel();
        tfieldOne = new JBTextField("asder");
        p.add(tfieldOne);
        p.add(new JBTextField("bla"));


        tfieldOne.addKeyListener(new KeyListener() {
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
                String fieldValue = tfieldOne.getText();

                PsiClass setterClass = javaFacade.findClass(fieldValue, scope);
                if(null != setterClass && setterClass != selectedPsiClass){
                    selectedPsiClass = setterClass;
                    for (PsiMethod setterMethod : setterClass.getMethods()) {
                        if(setterMethod.getName().startsWith("set")){
                            fieldModel.add(setterMethod);
                        }
                    }
                }
            }



        });

        jbTextField = new JBTextField();

        fieldListPanel = LabeledComponent.create(panel, "FieldList");
        p.add(panel);

//        fieldListPanel.add(LabeledComponent.create(jbTextField,"Text"));

        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return p;
    }





}
