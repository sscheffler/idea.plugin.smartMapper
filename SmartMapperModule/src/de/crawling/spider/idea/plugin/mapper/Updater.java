package de.crawling.spider.idea.plugin.mapper;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;

/**
 * Created by sscheffler on 25.05.14.
 */
public class Updater {

    private Project project;

    public Updater(Project project) {
        this.project = project;
    }

    /**
     * updates the setter on the document
     * @param setterCalls
     */
    public void updateOnDocument(String setterCalls){
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
    }
}
