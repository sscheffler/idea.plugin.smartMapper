package de.crawling.spider.idea.plugin.mapper;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;

import javax.swing.*;
import java.lang.reflect.Method;

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

    public String getMappingMethod(
            Project project,
            String setterCanonicalClassName,
            String setterVarName,
            String getterCanonicalClassName,
            String getterVarName) {

        String setterClassName = regexUtil.calculateClassName(setterCanonicalClassName);
        String getterClassName = regexUtil.calculateClassName(getterCanonicalClassName);

        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        PsiClass setterClass = JavaPsiFacade.getInstance(project).findClass(setterCanonicalClassName, scope);
        PsiClass getterClass = JavaPsiFacade.getInstance(project).findClass(getterCanonicalClassName, scope);

        if(null == setterClass){
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("public " + setterClassName +" mapTo"+ setterClassName+"(final "+ getterClassName + " " + getterVarName +"){\n");
        builder.append(setterClassName + " " + setterVarName + " = new " + setterClassName + "();\n");

        for (PsiMethod setterMethod : setterClass.getMethods()) {
            if(setterMethod.getName().startsWith("set")){
                String setterName = setterMethod.getName();
                String getterName = calculateGetter(getterClass, setterName);

                builder.append(setterVarName + "." + setterName + "("+getterVarName+"."+getterName+");\n");

            }
        }

        builder.append("return "+setterVarName+"\n}");
        return builder.toString();
    }

    /**
     * Uses Psi Structure to get setter methods
     * @param project
     * @param setterClassName
     * @param varName
     * @return
     */
    @Deprecated
    public String getAllSetterMethodsForClass(Project project, String setterClassName, String varName, String getterClassName) {

        StringBuilder builder = new StringBuilder();

        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        PsiClass setterClass = JavaPsiFacade.getInstance(project).findClass(setterClassName, scope);
        PsiClass getterClass = JavaPsiFacade.getInstance(project).findClass(getterClassName, scope);




        if(null == setterClass){
            JOptionPane.showMessageDialog(null, "Class '" + setterClassName + "' not found!!!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        builder.append(setterClassName + " " + varName + " = new " + setterClassName + "();\n");

        for (PsiMethod setterMethod : setterClass.getMethods()) {
            if(setterMethod.getName().startsWith("set")){
                String setterName = setterMethod.getName();
                String getterName = calculateGetter(getterClass, setterName);

                builder.append(varName + "." + setterName + "("+getterName+");\n");

            }
        }
        return builder.toString();

    }

    private String calculateGetter(PsiClass getterClass, String setterName) {
        String getterName = "";

        String swapSetter = setterName.replaceFirst("set", "");

        if(null == getterClass){
            return "";
        }

        for(PsiMethod getterMethod : getterClass.getMethods()){

            if(! getterMethod.getName().startsWith("get")){
                continue;
            }

            String swapGetter = getterMethod.getName().replaceFirst("get", "");

            if(swapSetter.toLowerCase().equals(swapGetter.toLowerCase())){
                getterName = getterMethod.getName()+"()";
                break;
            }

        }
        return getterName;
    }
}
