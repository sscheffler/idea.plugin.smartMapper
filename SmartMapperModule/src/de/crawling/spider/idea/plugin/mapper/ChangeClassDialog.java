package de.crawling.spider.idea.plugin.mapper;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.filters.getters.ExpectedTypesGetter;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.*;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangeClassDialog extends JDialog {
    public static final String EXTRACT_CLASS_NAME_PATTERN = ".*\\.([^\\.]*)$";
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField classNameTextField;
    private JTextField variableNameTextField;
    private JTextField mapperClassTextField;

    private final Project project;
    private final SmartMapper smartMapper = new SmartMapper();
    private Updater updater;
    private PsiClass editorClass;


    public ChangeClassDialog(PsiClass editorClass) {
        this.editorClass = editorClass;
        this.project = editorClass.getProject();
        updater = new Updater(project);

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        final int cursorPos = editor.getCaretModel().getOffset();
        PsiIdentifier element = (PsiIdentifier)editorClass.getContainingFile().findElementAt(cursorPos);

        GlobalSearchScope scope = GlobalSearchScope.allScope(project);


        PsiTypeElement psiJavaCodeReferenceElement = PsiTreeUtil.getParentOfType(element, PsiTypeElement.class);

        String canonicalText = psiJavaCodeReferenceElement.getType().getCanonicalText();
        String variableName = "";
        if(null != canonicalText){
            PsiClass getterClass = JavaPsiFacade.getInstance(project).findClass(canonicalText, scope);
            if(null != getterClass){
                calculateVariableName(canonicalText);
            }else{
                JOptionPane.showMessageDialog(null, "Class '" + canonicalText + "' not found!!!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }




//            Collection<PsiIdentifier> list= PsiTreeUtil.findChildrenOfAnyType(method, PsiIdentifier.class);
//        PsiElement element1 = element.getContext();
//        PsiElement element2 = element.getParent();






        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setActions();



    }

    private void calculateVariableName(String canonicalText) {
        String variableName;Pattern p = Pattern.compile(EXTRACT_CLASS_NAME_PATTERN);
        Matcher m = p.matcher(canonicalText);
        boolean classNameFound = m.find();
        if(classNameFound){
            String className = m.group(1);
            if(StringUtils.isNotBlank(className)){
                char c []={className.toCharArray()[0]};
                String swapCharString = new String(c).toLowerCase();
                String swapRestString = className.substring(1);
                variableName = swapCharString + swapRestString;
            }
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
        try {
            String setterCalls = smartMapper.getAllSetterMethodsForClass(
                    project,
                    classNameTextField.getText(),
                    variableNameTextField.getText(),
                    mapperClassTextField.getText());

            updater.updateOnPsiElement(setterCalls, editorClass);
            dispose();
        }catch(NullPointerException ne){

        }
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

}
