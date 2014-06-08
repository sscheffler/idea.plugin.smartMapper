package de.crawling.spider.idea.plugin.mapper;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang.StringUtils.*;

/**
 * Created by sscheffler on 25.05.14.
 */
public class SmartMapper {

    public RegexUtil regexUtil = new RegexUtil();

    /**
     * Uses reflection to get setter methods
     * @param className
     * @param varName
     * @return
     */
    @Deprecated
    public String getAllSetterMethodsForClass(String className, String varName) {

        StringBuilder builder = new StringBuilder();
        try {

            Class setterClass = Class.forName(className);

            Method[] methods = setterClass.getMethods();
            String objectName = setterClass.getCanonicalName();

            builder.append(objectName + " " + varName + " = new " + objectName + "();\n");

            for (Method method : methods) {
                if (method.getName().startsWith("set"))
                    builder.append(varName + "." + method.getName() + "(value);\n");
            }

        } catch (ClassNotFoundException c) {
            JOptionPane.showMessageDialog(null, "Class '" + className + "' not found!!!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return builder.toString();

    }

    public String getMappingMethodForSelecionWithGetterClass(
                                                    final String setterCanonicalClassName,
                                                    final String getterCanonicalClassName,
                                                    final List<PsiMethod> selectedMethods,
                                                    final Project project,
                                                    final boolean loadSuperClassMethods
                                                    ){
        StringBuilder builder = new StringBuilder();

        String setterClassName = regexUtil.calculateClassName(setterCanonicalClassName);
        String getterClassName = regexUtil.calculateClassName(getterCanonicalClassName);
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);

        String setterVarName = regexUtil.calculateVariableName(setterCanonicalClassName);
        String getterVarName = regexUtil.calculateVariableName(getterCanonicalClassName);

        PsiClass setterClass = JavaPsiFacade.getInstance(project).findClass(setterCanonicalClassName, scope);
        PsiClass getterClass = JavaPsiFacade.getInstance(project).findClass(getterCanonicalClassName, scope);

        if (validatePsiClassForNull(setterCanonicalClassName, setterClass)) return "";

        if (validatePsiClassForNull(setterCanonicalClassName, getterClass)) return "";

        getterClassName = (isBlank(getterClassName))?"":"final "+ getterClassName;

        builder.append("public " + setterClassName +" mapTo"+ setterClassName+"("+ getterClassName + " " + getterVarName +"){\n");
        builder.append(setterClassName + " " + setterVarName + " = new " + setterClassName + "();\n");

        for (PsiMethod setterMethod : selectedMethods) {
            if(setterMethod.getName().startsWith("set")){
                String setterName = setterMethod.getName();
                String getterName = calculateGetter(getterClass,getterVarName, setterName, loadSuperClassMethods);

                builder.append(setterVarName + "." + setterName + "( "+getterName+" );\n");

            }
        }
        builder.append("return "+setterVarName+";\n}");

        return builder.toString();
    }

    private boolean validatePsiClassForNull(String className, PsiClass psiClass) {
        if(null == psiClass){
            JOptionPane.showMessageDialog(null, "ClassName: " + className + " not known", "Input Failure", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }


    /**
     * returns a method strings which just contains the setter method calls of the selection
     * @param setterCanonicalClassName
     * @param selectedMethods
     * @param project
     * @return
     */
    public String getSimpleMappingMethodForSelecion(final String setterCanonicalClassName,
                                         final List<PsiMethod> selectedMethods,
                                         final Project project){

        StringBuilder builder = new StringBuilder();
        String setterClassName = regexUtil.calculateClassName(setterCanonicalClassName);
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        String setterVarName = regexUtil.calculateVariableName(setterCanonicalClassName);

        //Error Handling
        PsiClass setterClass = JavaPsiFacade.getInstance(project).findClass(setterCanonicalClassName, scope);

        if (validatePsiClassForNull(setterCanonicalClassName, setterClass)) return "";

        if(selectedMethods.isEmpty()){
            return "";
        }


        builder.append("public " + setterClassName +" mapTo"+ setterClassName+"(){\n");
        builder.append(setterClassName + " " + setterVarName + " = new " + setterClassName + "();\n");

        for (PsiMethod setterMethod : selectedMethods) {
            if(setterMethod.getName().startsWith("set")){
                String setterName = setterMethod.getName();

                builder.append(setterVarName + "." + setterName + "( );\n");

            }
        }

        builder.append("return "+setterVarName+";\n}");
        return builder.toString();

    }

    private String calculateGetter(final PsiClass getterClass,final String getterVarName, final String setterName, final boolean loadSuperClassMethods) {
        String getterName = "";

        String swapSetter = setterName.replaceFirst("set", "");

        if(null == getterClass){
            return "";
        }

        PsiMethod[] methods = (loadSuperClassMethods)?
                getterClass.getAllMethods() : getterClass.getMethods();

        for(PsiMethod getterMethod : methods){

            if(! getterMethod.getName().startsWith("get")){
                continue;
            }

            String swapGetter = getterMethod.getName().replaceFirst("get", "");

            if(swapSetter.toLowerCase().equals(swapGetter.toLowerCase())){
                getterName = getterVarName+"."+getterMethod.getName()+"()";
                break;
            }

        }
        return getterName;
    }
}
