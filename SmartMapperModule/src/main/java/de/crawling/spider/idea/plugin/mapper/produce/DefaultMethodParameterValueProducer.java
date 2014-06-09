package de.crawling.spider.idea.plugin.mapper.produce;

import com.intellij.psi.PsiParameter;
import de.crawling.spider.idea.plugin.mapper.util.RegexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Produces default values for method parameters
 *
 * @author sscheffler(stefan-scheffler@web.de)
 * @date 09.06.14
 */
public class DefaultMethodParameterValueProducer {

    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultMethodParameterValueProducer.class);
    private RegexUtil regexUtil = RegexUtil.INSTANCE;

    public final static DefaultMethodParameterValueProducer INSTANCE = new DefaultMethodParameterValueProducer();

    private DefaultMethodParameterValueProducer() {
    }


    /**
     * builds default value for non primitive types
     * @param qualifiedClassName
     * @param setterMethodName
     * @return
     */
    public String produceDefaultValueForNonPrimitives(String qualifiedClassName, String setterMethodName) {
        String returnValue = "";
        try {
            Class parameterTypeClass = Class.forName(qualifiedClassName);
            returnValue = evaluateNonPrimitives(parameterTypeClass.getCanonicalName(), setterMethodName);
        }catch(ClassNotFoundException cne){
            LOGGER.error("Class [{}] was not found", qualifiedClassName);
        }

        return returnValue;
    }


    private String evaluatePrimitives(String name) {

        DefaultTypes defaultType = DefaultTypes.fromValue(name);
        if(null == defaultType){
            return "";
        }

        LOGGER.trace("defaultValue found [{}]", defaultType.getPrimName());
        return defaultType.getDefaultValue();

    }

    private String evaluateNonPrimitives(final String name, String setterMethodName){

        //TODO: implement more cases / recursive constructor filling [sscheffler(stefan-scheffler@web.de) - 09.06.14 - 07:56]
        DefaultTypes defaultType = DefaultTypes.fromValue(name);
        if(null != defaultType){
            LOGGER.trace("defaultValue found [{}]", defaultType.getComplName());
            return defaultType.getDefaultValue();
        }

        switch (name){
            case "java.lang.String":
                return "\"" + regexUtil.calculateDefaultValueFromSetter(setterMethodName) + "\"";
            case "java.lang.Boolean":
                return "true";

            default: return "";//"new " +name+ "( )";
        }

    }

    /**
     * builds default value for primitive types
     * @param typeName
     * @return
     */
    public String produceDefaultValueForPrimitives(String typeName) {

        return evaluatePrimitives(typeName);
    }
}

enum DefaultTypes{
    BOOLEAN("boolean", Boolean.class.getCanonicalName(), "true"),
    CHAR("char", Character.class.getCanonicalName(),"'a'"),
    BYTE("byte", Byte.class.getCanonicalName(), "Byte.valueOf(\"98\")"),
    SHORT("short", Short.class.getCanonicalName(), "Short.valueOf(\"0\")"),
    INTEGER("int", Integer.class.getCanonicalName(), "1"),
    LONG("long", Long.class.getCanonicalName(), "1L"),
    FLOAT("float", Float.class.getCanonicalName(), "1.0f"),
    DOUBLE("double", Double.class.getCanonicalName(), "1.0d");

    private final String primName;
    private final String complName;
    private final String defaultValue;

    DefaultTypes(String primName, String complName, String defaultValue) {
        this.primName = primName;
        this.complName = complName;
        this.defaultValue = defaultValue;
    }

    public String getPrimName() {
        return primName;
    }

    public String getComplName() {
        return complName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public static DefaultTypes fromValue(String str){
        for(DefaultTypes type : DefaultTypes.values()){
            if(type.getPrimName().equals(str) || type.complName.equals(str)){
                return type;
            }
        }
        return null;
    }

}