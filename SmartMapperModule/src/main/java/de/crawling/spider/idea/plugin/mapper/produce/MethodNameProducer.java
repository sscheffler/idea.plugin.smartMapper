package de.crawling.spider.idea.plugin.mapper.produce;

import com.intellij.psi.PsiMethod;
import de.crawling.spider.idea.plugin.mapper.model.MapperProperties;
import de.crawling.spider.idea.plugin.mapper.util.RegexUtil;

import java.util.List;

/**
 * Created by sscheffler on 08.06.14.
 */
public class MethodNameProducer {


    public static MethodNameProducer INSTANCE = new MethodNameProducer();

    private RegexUtil regexUtil = RegexUtil.INSTANCE;

    private MethodNameProducer() {
    }

    public String produceMethodName(final MapperProperties mapperProperties){

        String methodName = regexUtil.calculateClassName(mapperProperties.getSetterCanonicalClassName());
        String result = mapperProperties.getMapperMethodPrefix()+methodName;

        List<PsiMethod> relevantMethods = regexUtil.findAllMethodsWithIndex(
                mapperProperties.getMapperMethodPrefix(),
                methodName,
                mapperProperties.getEditorClass()
        );

        if(!relevantMethods.isEmpty()){
            int newIndex = regexUtil.getIncrementIndex(relevantMethods);
            result = result + newIndex;
        }

        return result;
    }
}
