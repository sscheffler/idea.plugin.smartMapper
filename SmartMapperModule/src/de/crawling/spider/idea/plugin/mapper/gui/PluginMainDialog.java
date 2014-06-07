package de.crawling.spider.idea.plugin.mapper.gui;

import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

/**
 * Created by sscheffler on 07.06.14.
 */
public class PluginMainDialog extends DialogWrapper {


    public static final String TITLE = "Smart Mapper";

    private CollectionListModel<PsiMethod> methodModel;
    private GlobalSearchScope scope;
    private JavaPsiFacade javaFacade;
    private PsiClass selectedPsiClass;
    private JBList methodList;


    JPanel mainPanel;
    JBTextField setterTextField;

    public PluginMainDialog(@Nullable Project project) {
        super(project);
        setTitle(TITLE);

        scope = GlobalSearchScope.allScope(project);
        javaFacade = JavaPsiFacade.getInstance(project);
        methodModel = new CollectionListModel<>();

        buildGuiElementStructure();
        setterTextField.requestFocus();
        init();
    }

    /**
     * visualization
     */
    private void buildGuiElementStructure() {
        methodList = new JBList(methodModel);
        methodList.setCellRenderer(new DefaultPsiElementCellRenderer());
        setterTextField = new JBTextField();
        mainPanel = new JPanel(new BorderLayout());

        //TODO: make dynamic
        setSize(400,400);

        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(methodList);

        JPanel methodListMainPanel = new JPanel(new BorderLayout());
        GridBagConstraints c = new GridBagConstraints();
        JPanel methodListSubPanel = decorator.createPanel();

        methodListMainPanel.add(setterTextField, BorderLayout.NORTH);
        methodListMainPanel.add(methodListSubPanel, BorderLayout.CENTER);

        mainPanel.add(methodListMainPanel);

        addSetterKeyListener();
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
                            methodModel.add(setterMethod);
                        }
                    }
                }
            }


        });
    }

    public java.util.List<PsiMethod> getSelectedSetterMethods(){
        java.util.List<PsiMethod> returnList = new ArrayList<>();
        int[] indizes = methodList.getSelectedIndices();
        for(int index: indizes){
            try {
                returnList.add(methodModel.getElementAt(index));
            }catch (ArrayIndexOutOfBoundsException e){

            }

        }

        return returnList;
    }

    public String getCannonicalClassName(){
        return setterTextField.getText();
    }

    public PsiClass getSelectedClass(){
        return selectedPsiClass;
    }





}
