package de.crawling.spider.idea.plugin.mapper;

import com.intellij.openapi.editor.Editor;

import javax.swing.*;
import java.awt.event.*;

public class ChangeClassDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField classNameTextField;
    private JTextField variableNameTextField;

    private final Editor editor;
    private final SmartMapper smartMapper = new SmartMapper();

    public ChangeClassDialog(final Editor editor) {


        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        if(null == editor){
            throw new IllegalArgumentException("Editor was null");
        }

        this.editor = editor;

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
        System.out.println(smartMapper.getAllSetterMethodsForClass(String.class));
        System.out.println(editor.getDocument().getText());

        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

//    public static void main(String[] args) {
//        ChangeClassDialog dialog = new ChangeClassDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }
}
