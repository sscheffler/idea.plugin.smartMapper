package de.crawling.spider.idea.plugin.mapper.update;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.impl.JavaCodeFragmentFactoryImpl;
import de.crawling.spider.idea.plugin.mapper.model.MapperProperties;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * Created by sscheffler on 25.05.14.
 */
public class DefaultUpdaterImpl implements Updater{

    private Project project;
    public DefaultUpdaterImpl(Project project) {
        this.project = project;
    }

    /**
     * updates the setter on the document
     * @param setterCalls
     */
    @Deprecated
    private void updateOnDocument(final String setterCalls, PsiClass psiClass){
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        final Document document = editor.getDocument();
        final int cursorPos = editor.getCaretModel().getOffset();

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

    /**
     * updates the class over psi structure
     * @param setterCalls
     * @param psiClass
     */
    @Deprecated
    private void updateWithinMethod(final String setterCalls, final PsiClass psiClass){


        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        Document document = editor.getDocument();
      final int cursorPos = editor.getCaretModel().getOffset();

        new WriteCommandAction.Simple(psiClass.getProject(), psiClass.getContainingFile()){

            @Override
            protected void run() throws Throwable {

                JavaCodeFragmentFactory codeFragmentFactory = JavaCodeFragmentFactory.getInstance(psiClass.getProject());
                final PsiCodeFragment fragment = codeFragmentFactory.createCodeBlockCodeFragment(setterCalls, null,true);

                PsiElement element = psiClass.getContainingFile().findElementAt(cursorPos);
                element.replace(fragment);

            }
        }.execute();
    }

    /**
     * updates the class over psi structure
     * @param methodString
     * @param psiClass
     */
    private void updateClassWithCreatingNewMethod(final String methodString, final PsiClass psiClass){

        WriteCommandAction action = new WriteCommandAction.Simple(psiClass.getProject(), psiClass.getContainingFile()){

            @Override
            protected void run() throws Throwable {
                PsiElementFactory factory = JavaPsiFacade.getElementFactory(psiClass.getProject());
                PsiMethod map = factory.createMethodFromText(methodString, psiClass);
                PsiElement method = psiClass.add(map);
                JavaCodeStyleManager.getInstance(psiClass.getProject()).shortenClassReferences(method);

            }
        };
        action.execute();
    }

    @Override
    public void updateClassWithMethod(final String methodString, final MapperProperties properties) {
        if(isNotBlank(methodString)) {
            updateClassWithCreatingNewMethod(methodString, properties.getEditorClass());
        }
    }
}
