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

    public String getMappingMethod(
            final Project project,
            final String setterCanonicalClassName,
            final String setterVarName,
            final String getterCanonicalClassName,
            final String getterVarName,
            final boolean loadSuperClassMethods) {

        String setterClassName = regexUtil.calculateClassName(setterCanonicalClassName);
        String getterClassName = regexUtil.calculateClassName(getterCanonicalClassName);

        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        PsiClass setterClass = JavaPsiFacade.getInstance(project).findClass(setterCanonicalClassName, scope);
        PsiClass getterClass = JavaPsiFacade.getInstance(project).findClass(getterCanonicalClassName, scope);

        if(null == setterClass){
            JOptionPane.showMessageDialog(null, "ClassName: " + setterCanonicalClassName+" not known", "Input Failure" , JOptionPane.ERROR_MESSAGE);
            return "";
        }

        StringBuilder builder = new StringBuilder();

        getterClassName = (isBlank(getterClassName))?"":"final "+ getterClassName;

        builder.append("public " + setterClassName +" mapTo"+ setterClassName+"("+ getterClassName + " " + getterVarName +"){\n");
        builder.append(setterClassName + " " + setterVarName + " = new " + setterClassName + "();\n");

        for (PsiMethod setterMethod : setterClass.getMethods()) {
            if(setterMethod.getName().startsWith("set")){
                String setterName = setterMethod.getName();
                String getterName = calculateGetter(getterClass,getterVarName, setterName, loadSuperClassMethods);

                builder.append(setterVarName + "." + setterName + "( "+getterName+" );\n");

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
