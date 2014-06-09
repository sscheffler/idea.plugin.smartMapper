package de.crawling.spider.idea.plugin.mapper.gui;

import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBTextField;
import de.crawling.spider.idea.plugin.mapper.model.DefaultPrimitiveTypes;
import de.crawling.spider.idea.plugin.mapper.model.MapperProperties;
import de.crawling.spider.idea.plugin.mapper.gui.listeners.GetterPsiClassFindKeyListner;
import de.crawling.spider.idea.plugin.mapper.gui.listeners.SetterPsiClassFindKeyListner;
import de.crawling.spider.idea.plugin.mapper.model.MappingResults;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by sscheffler on 07.06.14.
 */
public class PluginMainDialog extends DialogWrapper {

    private final static Logger LOGGER = LoggerFactory.getLogger(PluginMainDialog.class);
    public static final String TITLE = "Smart Mapper";

    private CollectionListModel<PsiMethod> methodModel;
    private GlobalSearchScope scope;
    private JavaPsiFacade javaFacade;
    private PsiClass selectedSetterPsiClass;
    private PsiClass selectedGetterPsiClass;
    private JBList methodList;
    private Project project;
    private Color originalTextFieldBackGroundColor;


    JPanel mainPanel;
    JBTextField setterTextField;
    JBTextField getterTextField;
    JBCheckBox loadSuperClassGetterMethods;
    JBCheckBox fillWithDefaultValues;

    public PluginMainDialog(@Nullable Project project) {

        super(project);
        this.project = project;
        setTitle(TITLE);

        scope = GlobalSearchScope.allScope(project);
        javaFacade = JavaPsiFacade.getInstance(project);
        methodModel = new CollectionListModel<>();

        buildGuiElementStructure();
        init();
    }

    /**
     * visualization
     */
    private void buildGuiElementStructure() {
        methodList = new JBList(methodModel);
        methodList.setCellRenderer(new DefaultPsiElementCellRenderer());
        setterTextField = new JBTextField();
        getterTextField = new JBTextField();
        loadSuperClassGetterMethods = new JBCheckBox("super", Boolean.TRUE);
        fillWithDefaultValues = new JBCheckBox("default", Boolean.FALSE);
        originalTextFieldBackGroundColor = setterTextField.getBackground();
        mainPanel = new JPanel(new BorderLayout());

        //TODO: make dynamic
        setSize(400,400);

        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(methodList);

        JPanel methodListMainPanel = new JPanel(new BorderLayout());
        GridBagConstraints c = new GridBagConstraints();
        JPanel methodListSubPanel = decorator.createPanel();
        JPanel northPanel = new JPanel(new FlowLayout());



        northPanel.add(LabeledComponent.create(setterTextField, "setterClass:"));
        northPanel.add(LabeledComponent.create(getterTextField, "getterClass:"));
        northPanel.add(loadSuperClassGetterMethods);
        northPanel.add(fillWithDefaultValues);

        methodListMainPanel.add(northPanel, BorderLayout.NORTH);
        methodListMainPanel.add(methodListSubPanel, BorderLayout.CENTER);

        mainPanel.add(methodListMainPanel);

        addKeyListenerToTextFields();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return mainPanel;
    }


    public void addKeyListenerToTextFields(){

        setterTextField.addKeyListener(new SetterPsiClassFindKeyListner(this, setterTextField, methodModel, scope, javaFacade));
        getterTextField.addKeyListener(new GetterPsiClassFindKeyListner(this, getterTextField, scope, javaFacade));
    }



    public java.util.List<PsiMethod> getSelectedSetterMethods(){
        java.util.List<PsiMethod> returnList = new ArrayList<>();
        int[] indizes = methodList.getSelectedIndices();
        if(0 != indizes.length){
            //return selected elements
            for(int index: indizes){
                try {
                    returnList.add(methodModel.getElementAt(index));
                }catch (ArrayIndexOutOfBoundsException e){

                }

            }
        }else{
            //return all elements
            returnList = methodModel.getItems();
        }

        return returnList;
    }

    public boolean isSuperClassMapping(){
        return this.loadSuperClassGetterMethods.isSelected();
    }

    public boolean isFillWithDefaultValues(){
        return this.fillWithDefaultValues.isSelected();
    }

    public String getCannonicalSetterClassName(){
        return setterTextField.getText();
    }

    public String getCannonicalGetterClassName(){
        return getterTextField.getText();
    }

    public PsiClass getSelectedSetterClass(){
        return selectedSetterPsiClass;
    }

    public void setSelectedSetterClass(PsiClass psiClass){
        this.selectedSetterPsiClass = psiClass;
    }

    public void setSelectedGetterClass(PsiClass psiClass){
        
        this.selectedGetterPsiClass = psiClass;
    }

    public PsiClass getSelectedGetterPsiClass(){
        return selectedGetterPsiClass;
    }

    public Color getOriginalTextFieldBackGroundColor() {
        return originalTextFieldBackGroundColor;
    }

    public void setSetterColor(Color color){
        setterTextField.setBackground(color);
    }

    public void setGetterColor(Color color){
        getterTextField.setBackground(color);
    }

    /**
     * builds the mapper properties
     * @return
     */
    public MapperProperties buildResults(){


        return new MapperProperties(
                getCannonicalSetterClassName(),
                getCannonicalGetterClassName(),
                getSelectedSetterMethods(),
                project,
                isSuperClassMapping(),
                isFillWithDefaultValues()
        );
    }

}

