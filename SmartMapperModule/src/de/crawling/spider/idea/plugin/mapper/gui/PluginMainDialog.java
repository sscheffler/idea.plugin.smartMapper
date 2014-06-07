package de.crawling.spider.idea.plugin.mapper.gui;

import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by sscheffler on 07.06.14.
 */
public class PluginMainDialog extends DialogWrapper {


    public static final String TITLE = "Smart Mapper";

    private CollectionListModel<PsiMethod> fieldModel;
    private GlobalSearchScope scope;
    private JavaPsiFacade javaFacade;
    private PsiClass selectedPsiClass;

    JPanel mainPanel;
    JBTextField setterTextField;

    public PluginMainDialog(@Nullable Project project) {
        super(project);
        setTitle(TITLE);

        scope = GlobalSearchScope.allScope(project);
        javaFacade = JavaPsiFacade.getInstance(project);
        fieldModel = new CollectionListModel<>();

        buildGuiElementStructure();
        init();
    }

    /**
     * visualization
     */
    private void buildGuiElementStructure() {
        final JBList methodList = new JBList(fieldModel);
        methodList.setCellRenderer(new DefaultPsiElementCellRenderer());

        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(methodList);
        JPanel methodListPanel = decorator.createPanel();
        mainPanel = new JPanel();
        setterTextField = new JBTextField(" ");
        addSetterKeyListener();

        mainPanel.add(setterTextField);

        mainPanel.add(methodListPanel);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return mainPanel;
    }


    public void addSetterKeyListener(){
        setterTextField.addKeyListener(new KeyListener() {
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
                String fieldValue = setterTextField.getText();

                PsiClass setterClass = javaFacade.findClass(fieldValue, scope);
                if (null != setterClass && setterClass != selectedPsiClass) {
                    selectedPsiClass = setterClass;
                    for (PsiMethod setterMethod : setterClass.getMethods()) {
                        if (setterMethod.getName().startsWith("set")) {
                            fieldModel.add(setterMethod);
                        }
                    }
                }
            }


        });
    }





}
