package de.crawling.spider.idea.plugin.mapper;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.event.*;

public class ChangeClassDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField classNameTextField;
    private JTextField variableNameTextField;

    private final Project project;
    private final SmartMapper smartMapper = new SmartMapper();

    public ChangeClassDialog(final Project project) {


        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        if(null == project){
            throw new IllegalArgumentException("Project was null");
        }

        this.project = project;

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

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        System.out.println(smartMapper.getAllSetterMethodsForClass(project, classNameTextField.getText(), variableNameTextField.getText()));
//        System.out.println(editor.getDocument().getText());

        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

}
