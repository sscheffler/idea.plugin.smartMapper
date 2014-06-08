package de.crawling.spider.idea.plugin.mapper;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sscheffler on 25.05.14.
 */
public class RegexUtil {

    public static final String EXTRACT_CLASS_NAME_PATTERN = ".*\\.([^\\.]*)$";

    public String calculateVariableName(String canonicalText) {
        String variableName="";
        String className = calculateClassName(canonicalText);

        if(StringUtils.isNotBlank(className)){
            char c []={className.toCharArray()[0]};
            String swapCharString = new String(c).toLowerCase();
            String swapRestString = className.substring(1);
            variableName = swapCharString + swapRestString;
        }

        return variableName;
    }


    public String calculateClassName(String canonicalText) {
        Pattern p = Pattern.compile(EXTRACT_CLASS_NAME_PATTERN);
        Matcher m = p.matcher(canonicalText);
        boolean classNameFound = m.find();
        String className="";
        if(classNameFound) {
            className = m.group(1);
        }
        return className;
    }




}