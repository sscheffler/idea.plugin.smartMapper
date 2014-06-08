package de.crawling.spider.idea.plugin.mapper.util;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang.StringUtils.*;

/**
 * Created by sscheffler on 25.05.14.
 */
public class RegexUtil {

    public static final String EXTRACT_CLASS_NAME_PATTERN = ".*\\.([^\\.]*)$";

    public static final RegexUtil INSTANCE = new RegexUtil();
    private final static Logger LOGGER = LoggerFactory.getLogger(RegexUtil.class);

    private RegexUtil() {
    }

    /**
     * gets a variable name by a caconcial ClassString('e.g.: com.foo.BarTender' = 'barTender')
     * @param canonicalText
     * @return
     */
    public String calculateVariableName(String canonicalText) {
        if(isBlank(canonicalText)){
            return "";
        }

        String variableName="";
        String className = calculateClassName(canonicalText);
        className = (isBlank(className))?canonicalText:className;

        char c []={className.toCharArray()[0]};
        String swapCharString = new String(c).toLowerCase();
        String swapRestString = className.substring(1);
        variableName = swapCharString + swapRestString;

        return variableName;
    }


    /**
     * gets the classname by a caconcial String('e.g.: com.foo.Bar' = 'Bar')
     * @param canonicalText
     * @return
     */
    public String calculateClassName(String canonicalText) {
        if(isBlank(canonicalText)){
            return "";
        }
        Pattern p = Pattern.compile(EXTRACT_CLASS_NAME_PATTERN);
        Matcher m = p.matcher(canonicalText);
        boolean classNameFound = m.find();
        String className="";
        if(classNameFound) {
            className = m.group(1);
        }
        return className;
    }


    /**
     * finds all methods which starts with 'prefix' + 'methodName'
     * @param mapperMethodPrefix
     * @param methodName
     * @param editorClass
     * @return
     */
    public List<PsiMethod> findAllMethodsWithIndex(String mapperMethodPrefix, String methodName, PsiClass editorClass) {
        if(isBlank(methodName) || null == editorClass){
            return Collections.emptyList();
        }
        String prefix = (isBlank(mapperMethodPrefix))?"":mapperMethodPrefix;

        List<PsiMethod> relevantMethods = new ArrayList<>();
        for(PsiMethod method : editorClass.getMethods()){

            LOGGER.trace("check if method name matches to '{}' + '{}'",prefix, methodName);

            if(method.getName().matches( "^" + prefix + methodName + "\\d*$" )){
                relevantMethods.add(method);
            }
        }

        return relevantMethods;
    }


    public int getIncrementIndex(List<PsiMethod> testData) {
        return 0;
    }
}
