package de.crawling.spider.idea.plugin.mapper;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;

import javax.swing.*;
import java.lang.reflect.Method;

/**
 * Created by sscheffler on 25.05.14.
 */
public class SmartMapper {

    /**
     * Uses reflection to get setter methods
     * @param className
     * @param varName
     * @return
     */
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

    /**
     * Uses Psi Structure to get setter methods
     * @param project
     * @param setterClassName
     * @param varName
     * @return
     */
    public String getAllSetterMethodsForClass(Project project, String setterClassName, String varName, String getterClassName) {

        StringBuilder builder = new StringBuilder();

        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        PsiClass psiClass = JavaPsiFacade.getInstance(project).findClass(setterClassName, scope);

        if(null == psiClass){
            JOptionPane.showMessageDialog(null, "Class '" + setterClassName + "' not found!!!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        builder.append(setterClassName + " " + varName + " = new " + setterClassName + "();\n");

        for (PsiMethod psiMethod : psiClass.getMethods()) {
            if(psiMethod.getName().startsWith("set")){
                builder.append(varName + "." + psiMethod.getName() + "();\n");
            }
        }
        return builder.toString();

    }
}
