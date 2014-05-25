package de.crawling.spider.idea.plugin.mapper;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.*;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.*;

import static org.apache.commons.lang.StringUtils.*;

public class ChangeClassDialog extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField classNameTextField;
    private JTextField variableNameTextField;
    private JTextField mapperClassTextField;
    private JTextField mapperVariableTextField;
    private JCheckBox superCheckBox;

    private final Project project;
    private final SmartMapper smartMapper = new SmartMapper();
    private final RegexUtil regexUtil = new RegexUtil();
    private Updater updater;
    private PsiClass editorClass;


    public ChangeClassDialog(PsiClass editorClass) {
        this.editorClass = editorClass;
        this.project = editorClass.getProject();
        updater = new Updater(project);

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        initializeFields(editorClass);
        setActions();
    }

    private void initializeFields(PsiClass editorClass) {

        setTitle("Smart Mapper");
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        final int cursorPos = editor.getCaretModel().getOffset();

        PsiElement element = editorClass.getContainingFile().findElementAt(cursorPos);

        GlobalSearchScope scope = GlobalSearchScope.allScope(project);


        PsiTypeElement psiJavaCodeReferenceElement = PsiTreeUtil.getParentOfType(element, PsiTypeElement.class);
        if(null != psiJavaCodeReferenceElement){

            String canonicalText = psiJavaCodeReferenceElement.getType().getCanonicalText();
            String variableName = "";
            PsiClass getterClass = JavaPsiFacade.getInstance(project).findClass(canonicalText, scope);
            if(null != getterClass){
                variableName = regexUtil.calculateVariableName(canonicalText);


            }else{
                JOptionPane.showMessageDialog(null, "Class '" + canonicalText + "' not found!!!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            mapperClassTextField.setText(canonicalText);
            mapperVariableTextField.setText(variableName);
            mapperVariableTextField.setToolTipText(variableName);
        }
    }



    private void setActions() {
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {

        String setterClassName = classNameTextField.getText();
        String setterVarName = variableNameTextField.getText();
        if(isBlank(setterClassName) || isBlank(setterVarName)){
            JOptionPane.showMessageDialog(null, "ClassName: " + setterClassName+"\nVariableName: " +setterVarName, "Input Failure" , JOptionPane.INFORMATION_MESSAGE);

            return;
        }

        try {
            String methodString = smartMapper.getMappingMethod(
                    project,
                    setterClassName,
                    setterVarName,
                    mapperClassTextField.getText(),
                    mapperVariableTextField.getText());
            if(isNotBlank(methodString)) {
                updater.updateClassWithCreatingNewMethod(methodString, editorClass);
            }else{
                return;
            }

        }catch(NullPointerException ne){
            ne.printStackTrace();
        }
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

}
