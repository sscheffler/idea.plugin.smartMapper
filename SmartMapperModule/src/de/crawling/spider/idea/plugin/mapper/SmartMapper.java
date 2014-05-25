package de.crawling.spider.idea.plugin.mapper;

import java.lang.reflect.Method;

/**
 * Created by sscheffler on 25.05.14.
 */
public class SmartMapper {

    public String getAllSetterMethodsForClass(Class bla) {

        StringBuilder builder = new StringBuilder();

        Method[] methods = bla.getMethods();
        String objectName = bla.getCanonicalName();

        String varName = bla.getSimpleName().toLowerCase();

        builder.append(objectName + " " + varName + " = new " + objectName +"();\n");


        for (Method method : methods) {
            if (method.getName().startsWith("set"))
                builder.append(varName + "." + method.getName() + "(value);\n");
        }
        return builder.toString();

    }
}
