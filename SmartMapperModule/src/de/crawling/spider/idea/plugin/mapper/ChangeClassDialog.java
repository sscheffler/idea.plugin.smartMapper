package de.crawling.spider.idea.plugin.mapper;

import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
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
        String setterCalls = smartMapper.getAllSetterMethodsForClass(project, classNameTextField.getText(), variableNameTextField.getText());
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        Document document = editor.getDocument();
        int cursorPos = editor.getCaretModel().getOffset();

        final Runnable insertRunner = new Runnable() {
            @Override
            public void run() {
                document.insertString(cursorPos, setterCalls);
            }
        };
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                CommandProcessor.getInstance().executeCommand(project, new Runnable() {
                    @Override
                    public void run() {
                        ApplicationManager.getApplication().runWriteAction(insertRunner);
                    }
                }, "de.crawling.spider.idea.plugin.mapper.SmartMapper", null);
            }
        });




        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

}
