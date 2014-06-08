package de.crawling.spider.idea.plugin.mapper;

import com.intellij.psi.PsiClass;
import org.apache.commons.lang.StringUtils;

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


    public List<String> findAllMethodsWithIndex(String mapperMethodPrefix, String methodName, PsiClass editorClass) {
        if(isBlank(methodName) || null == editorClass){
            return Collections.emptyList();
        }
        return null;
    }
}
