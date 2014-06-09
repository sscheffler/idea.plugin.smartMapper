package de.crawling.spider.idea.plugin.mapper.produce;

import com.intellij.psi.PsiParameter;
import de.crawling.spider.idea.plugin.mapper.util.RegexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Produces default values for method parameters
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


    public String produceDefaultValue(String qualifiedClassName, String setterMethodName) {
        String returnValue = "";
        try {
            Class parameterTypeClass = Class.forName(qualifiedClassName);
            if(!parameterTypeClass.isPrimitive()){
                returnValue = evaluateNonPrimitives(parameterTypeClass.getCanonicalName(), setterMethodName);
            }
        }catch(ClassNotFoundException cne){
            LOGGER.error("Class [{}] was not found", qualifiedClassName);
            return "";
        }

        return returnValue;
    }

    private String evaluateNonPrimitives(final String name, String setterMethodName){

        String returnValue = "";

        //TODO: implement more cases / recursive constructor filling [sscheffler(stefan-scheffler@web.de) - 09.06.14 - 07:56]
        switch (name){
            case "java.lang.String":{
                returnValue = "\"" + regexUtil.calculateDefaultValueFromSetter(setterMethodName) + "\"";
                break;
            }
            default: returnValue = "";//"new " +name+ "( )";
        }

        return returnValue;
    }
}
