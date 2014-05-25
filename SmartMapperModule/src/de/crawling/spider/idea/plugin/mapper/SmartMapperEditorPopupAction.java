package de.crawling.spider.idea.plugin.mapper;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.util.EditorUtil;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.roots.impl.ProjectFileIndexImpl;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiFileImplUtil;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by sscheffler on 24.05.14.
 */
public class SmartMapperEditorPopupAction extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getProject();

//        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
//        PsiClass psiClass = JavaPsiFacade.getInstance(project).findClass("de.BClass", scope);

        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Editor editor = e.getData(LangDataKeys.EDITOR);

        if (project == null) {
            return;
        }

        startDialog(project);

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
//        final Runnable readRunner = new Runnable() {
//            @Override
//            public void run() {
////                String s = getAllSetterMethodsForClass(String.class);
////                document.insertString(offset, s);
//            }
//        };
//        ApplicationManager.getApplication().invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                CommandProcessor.getInstance().executeCommand(project, new Runnable() {
//                    @Override
//                    public void run() {
//                        ApplicationManager.getApplication().runWriteAction(readRunner);
//                    }
//                }, "de.crawling.spider.idea.plugin.mapper.SmartMapper", null);
//            }
//        });
    }

    private void startDialog(Project project) {

        try {
            ChangeClassDialog dialog = new ChangeClassDialog(project);
            dialog.pack();
            dialog.setVisible(true);
        }catch(IllegalArgumentException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "No editor open", JOptionPane.INFORMATION_MESSAGE);
        }
    }




}
