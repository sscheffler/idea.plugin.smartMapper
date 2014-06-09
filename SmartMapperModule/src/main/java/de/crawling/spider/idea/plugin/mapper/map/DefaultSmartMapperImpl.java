package de.crawling.spider.idea.plugin.mapper.map;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import de.crawling.spider.idea.plugin.mapper.produce.DefaultMethodParameterValueProducer;
import de.crawling.spider.idea.plugin.mapper.util.MapperHelper;
import de.crawling.spider.idea.plugin.mapper.model.MapperProperties;
import de.crawling.spider.idea.plugin.mapper.util.RegexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

import static org.apache.commons.lang.StringUtils.*;

/**
 * Created by sscheffler on 25.05.14.
 */
public class DefaultSmartMapperImpl implements SmartMapper{

    private RegexUtil regexUtil = RegexUtil.INSTANCE;
    private MapperHelper mapperHelper = MapperHelper.INSTANCE;
    private DefaultMethodParameterValueProducer defaultMethodParameterValueProducer = DefaultMethodParameterValueProducer.INSTANCE;

    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultSmartMapperImpl.class);

    /**
     * creates a method String and tries to map getter methods in setter methods
     * @param mapperProperties
     * @return
     */
    private String getMappingMethodForSelecionWithGetterClass(MapperProperties mapperProperties) {
        StringBuilder builder = new StringBuilder();
        Project project = mapperProperties.getProject();
        String setterCanonicalClassName  = mapperProperties.getSetterCanonicalClassName();
        String getterCanonicalClassName  = mapperProperties.getGetterCanonicalClassName();

        String setterClassName = regexUtil.calculateClassName(setterCanonicalClassName);
        String getterClassName = regexUtil.calculateClassName(getterCanonicalClassName);
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);

        String setterVarName = regexUtil.calculateVariableNameFromClass(setterCanonicalClassName);
        String getterVarName = regexUtil.calculateVariableNameFromClass(getterCanonicalClassName);

        PsiClass setterClass = JavaPsiFacade.getInstance(project).findClass(setterCanonicalClassName, scope);
        PsiClass getterClass = JavaPsiFacade.getInstance(project).findClass(getterCanonicalClassName, scope);

        if (validatePsiClassForNull(setterCanonicalClassName, setterClass)) return "";

        if (validatePsiClassForNull(setterCanonicalClassName, getterClass)) return "";

        getterClassName = (isBlank(getterClassName))?"":"final "+ getterClassName;

        String methodName = mapperHelper.retrieveMethodName(mapperProperties);

        builder.append("public " + setterClassName +" " +methodName+"("+ getterClassName + " " + getterVarName +"){\n");
        builder.append(setterClassName + " " + setterVarName + " = new " + setterClassName + "();\n");

        for (PsiMethod setterMethod : mapperProperties.getSelectedMethods()) {
            if(setterMethod.getName().startsWith("set")){
                String setterName = setterMethod.getName();
                LOGGER.trace("Building setter String for:{}", setterName);
                String parameters = calculateGetter(getterClass,getterVarName, setterName, mapperProperties.isLoadSuperClassMethods());

                parameters = (isNotBlank(parameters)) ? parameters : calculateDefaultValue(setterName, mapperProperties, setterMethod.getParameterList());

                builder.append(setterVarName + "." + setterName + "( "+parameters+" );\n");

            }
        }
        builder.append("return "+setterVarName+";\n}");

        return builder.toString();
    }

    private boolean validatePsiClassForNull(String className, PsiClass psiClass) {
        if(null == psiClass){
            JOptionPane.showMessageDialog(null, "ClassName: " + className + " not known", "Input Failure", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }


    /**
     * returns a method strings which just contains the setter method calls of the selection
     * @param mapperProperties
     * @return
     */
    private String getSimpleMappingMethodForSelecion(MapperProperties mapperProperties){

        StringBuilder builder = new StringBuilder();
        Project project = mapperProperties.getProject();
        String setterCanonicalClassName  = mapperProperties.getSetterCanonicalClassName();

        String setterClassName = regexUtil.calculateClassName(setterCanonicalClassName);
        GlobalSearchScope scope = GlobalSearchScope.allScope(project);
        String setterVarName = regexUtil.calculateVariableNameFromClass(setterCanonicalClassName);

        //Error Handling
        PsiClass setterClass = JavaPsiFacade.getInstance(project).findClass(setterCanonicalClassName, scope);

        if (validatePsiClassForNull(setterCanonicalClassName, setterClass)) return "";

        if(mapperProperties.getSelectedMethods().isEmpty()){
            return "";
        }

        String methodName = mapperHelper.retrieveMethodName(mapperProperties);

        builder.append("public " + setterClassName +" " + methodName+"(){\n");
        builder.append(setterClassName + " " + setterVarName + " = new " + setterClassName + "();\n");

        for (PsiMethod setterMethod : mapperProperties.getSelectedMethods()) {
            if(setterMethod.getName().startsWith("set")){
                String setterName = setterMethod.getName();
                LOGGER.debug("Building setter String for:{}", setterName);

                String defaultValue = calculateDefaultValue(setterName, mapperProperties, setterMethod.getParameterList());

                builder.append(setterVarName + "." + setterName + "( " + defaultValue + " );\n");

            }
        }

        builder.append("return "+setterVarName+";\n}");

        return builder.toString();

    }

    private String calculateDefaultValue(final String setterName, final MapperProperties mapperProperties, PsiParameterList parameterList) {
        if(!mapperProperties.isDefaultValues()){
            LOGGER.debug("No default value will be calculated:, default selection[{}]", mapperProperties.isDefaultValues());
        }

        final StringBuilder builder = new StringBuilder();

        for(PsiParameter psiParameter : parameterList.getParameters()){
            PsiElement[] children = psiParameter.getChildren();
            PsiJavaCodeReferenceElement referenceElement = PsiTreeUtil.findChildOfType(psiParameter, PsiJavaCodeReferenceElement.class);
            String appender = "";
            appender = calculateDefaultValueAppender(setterName, psiParameter, referenceElement, appender);

            appendDefaultValue(parameterList, builder, psiParameter, appender);
        }
        return builder.toString();
    }

    private String calculateDefaultValueAppender(String setterName, PsiParameter psiParameter, PsiJavaCodeReferenceElement referenceElement, String appender) {
        if(null != referenceElement){
            String qualifiedClassName = referenceElement.getQualifiedName();
            appender = defaultMethodParameterValueProducer.produceDefaultValueForNonPrimitives(qualifiedClassName, setterName);
        }else{
            PsiKeyword keyWord = PsiTreeUtil.findChildOfType(psiParameter, PsiKeyword.class);
            if(null != keyWord){
                appender = defaultMethodParameterValueProducer.produceDefaultValueForPrimitives(keyWord.getText());
            }
        }
        return appender;
    }

    private void appendDefaultValue(PsiParameterList parameterList, StringBuilder builder, PsiParameter psiParameter, String appender) {
        if(isNotBlank(appender)) {
            int index = parameterList.getParameterIndex(psiParameter);
            if (index > 0) {
                builder.append(", ");
            }
            builder.append(appender);
        }
    }

    private String calculateGetter(final PsiClass getterClass,final String getterVarName, final String setterName, final boolean loadSuperClassMethods) {
        String getterName = "";

        String swapSetter = setterName.replaceFirst("set", "");

        if(null == getterClass){
            return "";
        }

        PsiMethod[] methods = (loadSuperClassMethods)?
                getterClass.getAllMethods() : getterClass.getMethods();

        for(PsiMethod getterMethod : methods){

            if(! getterMethod.getName().startsWith("get")){
                continue;
            }

            String swapGetter = getterMethod.getName().replaceFirst("get", "");

            if(swapSetter.toLowerCase().equals(swapGetter.toLowerCase())){
                getterName = getterVarName+"."+getterMethod.getName()+"()";
                break;
            }

        }
        return getterName;
    }

    @Override
    public String buildMapperMethod(final MapperProperties properties) {
        String returnValue = "";
        if(properties.propertiesValidForGetter()){
            LOGGER.debug("Mapping with getter methods was performed");
            returnValue =  getMappingMethodForSelecionWithGetterClass(properties);

        }else{
            LOGGER.debug("just creating setterMethods");
            returnValue = getSimpleMappingMethodForSelecion(properties);
        }


        return returnValue;
    }
}
