package de.crawling.spider.idea.plugin.mapper;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.lang.reflect.Method;

/**
 * Created by sscheffler on 24.05.14.
 */
public class SmartMapper extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getProject();
        if (project == null) {
            return;
        }

        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        startDialog(editor);

        int offset = editor.getCaretModel().getOffset();



        if (editor == null) {
            return;
        }
        final Document document = editor.getDocument();

        if (document == null) {
            return;
        }

//        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
//        if (virtualFile == null) {
//            return;
//        }
//        final String contents;
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(virtualFile.getPath()));
//            String currentLine;
//            StringBuilder stringBuilder = new StringBuilder();
//            while ((currentLine = br.readLine()) != null) {
//                stringBuilder.append(currentLine);
//                stringBuilder.append("\n");
//            }
//            contents = stringBuilder.toString();
//        } catch (IOException e1) {
//            return;
//        }
        final Runnable readRunner = new Runnable() {
            @Override
            public void run() {
                String s = getAllSetterMethodsForClass(String.class);
                document.insertString(offset, s);
            }
        };
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                CommandProcessor.getInstance().executeCommand(project, new Runnable() {
                    @Override
                    public void run() {
                        ApplicationManager.getApplication().runWriteAction(readRunner);
                    }
                }, "de.crawling.spider.idea.plugin.mapper.SmartMapper", null);
            }
        });
    }

    private void startDialog(Editor editor) {

        try {
            ChangeClassDialog dialog = new ChangeClassDialog(editor);
            dialog.pack();
            dialog.setVisible(true);
        }catch(IllegalArgumentException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "No editor open", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public String getAllSetterMethodsForClass(Class bla) {

        StringBuilder builder = new StringBuilder();

        Method[] methods = bla.getMethods();
        String objectName = bla.getCanonicalName();

        String varName = bla.getSimpleName().toLowerCase();

        builder.append(objectName + " " + varName + " = new " + objectName +"();\n");


        for (Method method : methods) {
            if (method.getName().startsWith("set"))
                builder.append(varName + "." + method.getName() + "(value);\n");
        }
        return builder.toString();

    }

    class Test {
        public void setA(){

        }

        public void setB(){

        }

        public void schelm(){

        }
    }

}
