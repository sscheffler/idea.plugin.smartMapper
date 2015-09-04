package de.crawling.spider.idea.plugin.mapper.util;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.util.containers.SortedList;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
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
    public static final String EXTRACT_METHOD_INDEX_PATTERN = "^[^\\d]*(\\d*)";
    public static final String EXTRACT_SETTER_QUALIFIED_NAME_PATTERN = "^set(.*)";

    private RegexUtil() {
    }

    /**
     * gets a variable name by a caconcial ClassString('e.g.: com.foo.BarTender' = 'barTender')
     * @param canonicalText
     * @return
     */
    public String calculateVariableNameFromClass(String canonicalText) {
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
        String className = extractFirstGroup(canonicalText, EXTRACT_CLASS_NAME_PATTERN);
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
            if(method.getName().matches( "^" + prefix + methodName + "\\d*$" )){
                relevantMethods.add(method);
            }
        }

        return relevantMethods;
    }


    public int getIncrementIndex(List<PsiMethod> methods) {
        List<Integer> resultList = new ArrayList<Integer>();

        for(PsiMethod method : methods){

            String index = extractFirstGroup(method.getName(), EXTRACT_METHOD_INDEX_PATTERN);
            Integer val = (isBlank(index))? 0:Integer.valueOf(index);
            resultList.add(val);
        }

        Integer[] integerArray = new Integer[resultList.size()];
        resultList.toArray(integerArray);

        Arrays.sort(integerArray);

        int nextIndex = integerArray[resultList.size()-1]+1;

        return nextIndex;
    }

    private String extractFirstGroup(String text, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        boolean classNameFound = m.find();
        String className="";
        if(classNameFound) {
            className = m.group(1);
        }
        return className;
    }

    public String calculateDefaultValueFromSetter(String setterMethodName) {
        return extractFirstGroup(setterMethodName, EXTRACT_SETTER_QUALIFIED_NAME_PATTERN);
    }
}
