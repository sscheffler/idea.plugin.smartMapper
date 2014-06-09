package de.crawling.spider.idea.plugin.mapper.util;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import de.crawling.spider.idea.plugin.mapper.produce.MethodNameProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by sscheffler on 08.06.14.
 */
public class MapperHelper {

    public final static MapperHelper INSTANCE = new MapperHelper();
    private MethodNameProducer methodNameProducer = MethodNameProducer.INSTANCE;

    protected MapperHelper(){

    }

    private final static Logger LOGGER = LoggerFactory.getLogger(MapperHelper.class);

    public PsiClass retrieveEditorClass(AnActionEvent e) throws IllegalArgumentException{
        PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        if(psiFile instanceof PsiJavaFile){
            Editor editor = e.getData(LangDataKeys.EDITOR);

            int cursorPostion = editor.getCaretModel().getOffset();
            PsiElement psiElement = psiFile.findElementAt(cursorPostion);
            PsiClass returnValue = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);

            LOGGER.debug("found editorclass: {}", returnValue);
            return returnValue;
        }else{
            throw new IllegalArgumentException("selected file '"+psiFile.getName()+"' is no java file!");
        }
    }

    public String retrieveMethodName(MapperProperties mapperProperties) throws IllegalArgumentException{
        if(null == mapperProperties.getEditorClass()){
            throw new IllegalArgumentException("Editor class could not be resolved");
        }

        return methodNameProducer.produceMethodName(mapperProperties);
    }
}
