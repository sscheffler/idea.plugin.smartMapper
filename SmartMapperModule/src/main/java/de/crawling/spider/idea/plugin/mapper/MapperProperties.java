package de.crawling.spider.idea.plugin.mapper;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang.StringUtils.*;

/**
 * Created by sscheffler on 08.06.14.
 */
public class MapperProperties {

    private String setterCanonicalClassName;
    private String getterCanonicalClassName;
    private List<PsiMethod> selectedMethods;
    private Project project;
    private boolean loadSuperClassMethods;
    private boolean defaultValues;

    private PsiClass editorClass;

    public MapperProperties(String setterCanonicalClassName, String getterCanonicalClassName, List<PsiMethod> selectedMethods, Project project, boolean loadSuperClassMethods, boolean defaultValues) {
        this.setterCanonicalClassName = setterCanonicalClassName;
        this.getterCanonicalClassName = getterCanonicalClassName;
        this.selectedMethods = selectedMethods;
        this.project = project;
        this.loadSuperClassMethods = loadSuperClassMethods;
        this.defaultValues = defaultValues;
    }

    public String getSetterCanonicalClassName() {
        return setterCanonicalClassName;
    }

    public void setSetterCanonicalClassName(String setterCanonicalClassName) {
        this.setterCanonicalClassName = setterCanonicalClassName;
    }

    public String getGetterCanonicalClassName() {
        return getterCanonicalClassName;
    }

    public void setGetterCanonicalClassName(String getterCanonicalClassName) {
        this.getterCanonicalClassName = getterCanonicalClassName;
    }

    public List<PsiMethod> getSelectedMethods() {
        return selectedMethods;
    }

    public void setSelectedMethods(List<PsiMethod> selectedMethods) {
        this.selectedMethods = selectedMethods;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public boolean isLoadSuperClassMethods() {
        return loadSuperClassMethods;
    }

    public void setLoadSuperClassMethods(boolean loadSuperClassMethods) {
        this.loadSuperClassMethods = loadSuperClassMethods;
    }

    public boolean isDefaultValues() {
        return defaultValues;
    }

    public void setDefaultValues(boolean defaultValues) {
        this.defaultValues = defaultValues;
    }

    public boolean propertiesValid(){
        return (isNotBlank(setterCanonicalClassName) &&
                null != selectedMethods &&
                !selectedMethods.isEmpty() &&
                null!=project );
    }

    public boolean propertiesValidForGetter(){
        return (propertiesValid() &&
                isNotBlank(getterCanonicalClassName));
    }


    public PsiClass getEditorClass() {
        return editorClass;
    }

    public void setEditorClass(PsiClass editorClass) {
        this.editorClass = editorClass;
    }
}
