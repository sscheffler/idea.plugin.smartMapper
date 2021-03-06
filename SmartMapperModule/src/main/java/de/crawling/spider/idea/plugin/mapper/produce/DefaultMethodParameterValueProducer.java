package de.crawling.spider.idea.plugin.mapper.produce;

import de.crawling.spider.idea.plugin.mapper.model.DefaultPrimitiveTypes;
import de.crawling.spider.idea.plugin.mapper.util.RegexUtil;

/**
 * Produces default values for method parameters
 *
 * @author sscheffler(stefan-scheffler@web.de)
 * @date 09.06.14
 */
public class DefaultMethodParameterValueProducer {

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

        //TODO: refactor, because it can be work together wtih the primitive method [Stefan Scheffler(sscheffler@avantgarde-labs.de) - 15.06.14 - 11:53]
        String returnValue = "";
//        try {
//            Class parameterTypeClass = Class.forName(qualifiedClassName);
            returnValue = evaluateNonPrimitives(qualifiedClassName, setterMethodName);
//        }catch(ClassNotFoundException cne){
//            LOGGER.error("Class [{}] was not found", qualifiedClassName);
//        }

        return returnValue;
    }


    private String evaluatePrimitives(String name) {

        DefaultPrimitiveTypes defaultType = DefaultPrimitiveTypes.fromValue(name);
        if(null == defaultType){
            return "";
        }

        return defaultType.getDefaultValue();

    }

    private String evaluateNonPrimitives(final String name, String setterMethodName){

        //TODO: implement more cases / recursive constructor filling [sscheffler(stefan-scheffler@web.de) - 09.06.14 - 07:56]
        DefaultPrimitiveTypes defaultType = DefaultPrimitiveTypes.fromValue(name);
        if(null != defaultType){
            return defaultType.getDefaultValue();
        }

        switch (name){
            case "java.lang.String":
                return "\"" + regexUtil.calculateDefaultValueFromSetter(setterMethodName) + "\"";
            case "java.util.Date":
                return "new Date()";

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

