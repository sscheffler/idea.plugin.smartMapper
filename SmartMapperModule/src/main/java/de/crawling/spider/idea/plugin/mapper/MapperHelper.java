package de.crawling.spider.idea.plugin.mapper;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sscheffler on 08.06.14.
 */
public class MapperHelper {

    public final static MapperHelper INSTANCE = new MapperHelper();

    protected MapperHelper(){

    }

    private final static Logger LOGGER = LoggerFactory.getLogger(MapperHelper.class);

    public PsiClass getEditorClass(AnActionEvent e) throws IllegalArgumentException{
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        if(psiFile instanceof PsiJavaFile){
            Editor editor = e.getData(LangDataKeys.EDITOR);

            int cursorPostion = editor.getCaretModel().getOffset();
            PsiElement psiElement = psiFile.findElementAt(cursorPostion);
            return PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
        }else{
            throw new IllegalArgumentException("selected file '"+psiFile.getName()+"' is no java file!");
        }
    }
}
