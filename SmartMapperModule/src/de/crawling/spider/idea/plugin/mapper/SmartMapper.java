package de.crawling.spider.idea.plugin.mapper;

import java.lang.reflect.Method;

/**
 * Created by sscheffler on 25.05.14.
 */
public class SmartMapper {

    public String getAllSetterMethodsForClass(Class setterClass) {

        StringBuilder builder = new StringBuilder();

        Method[] methods = setterClass.getMethods();
        String objectName = setterClass.getCanonicalName();

        String varName = setterClass.getSimpleName().toLowerCase();

        builder.append(objectName + " " + varName + " = new " + objectName +"();\n");


        for (Method method : methods) {
            if (method.getName().startsWith("set"))
                builder.append(varName + "." + method.getName() + "(value);\n");
        }
        return builder.toString();

    }
}
