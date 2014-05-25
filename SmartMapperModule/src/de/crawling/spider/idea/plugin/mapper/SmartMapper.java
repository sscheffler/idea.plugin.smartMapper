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

    public String getAllSetterMethodsForClass(Project project, String className, String varName) {

        StringBuilder builder = new StringBuilder();

        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        PsiClass psiClass = JavaPsiFacade.getInstance(project).findClass(className, scope);

        if(null == psiClass){
            JOptionPane.showMessageDialog(null, "Class '" + className + "' not found!!!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        builder.append(className + " " + varName + " = new " + varName + "();\n");

        for (PsiMethod psiMethod : psiClass.getMethods()) {
            if(psiMethod.getName().startsWith("set")){
                builder.append(varName + "." + psiMethod.getName() + "(value);\n");
            }
        }
        return builder.toString();

    }
}
