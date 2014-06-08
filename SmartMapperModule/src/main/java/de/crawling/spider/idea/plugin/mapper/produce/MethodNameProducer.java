package de.crawling.spider.idea.plugin.mapper.produce;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import de.crawling.spider.idea.plugin.mapper.util.MapperProperties;
import de.crawling.spider.idea.plugin.mapper.util.RegexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * Created by sscheffler on 08.06.14.
 */
public class MethodNameProducer {

    private final static Logger LOGGER = LoggerFactory.getLogger(MethodNameProducer.class);

    public static MethodNameProducer INSTANCE = new MethodNameProducer();

    private RegexUtil regexUtil = RegexUtil.INSTANCE;

    private MethodNameProducer() {
    }

    public String produceMethodName(final MapperProperties mapperProperties){

        String methodName = regexUtil.calculateClassName(mapperProperties.getSetterCanonicalClassName());
        String result = mapperProperties.getMapperMethodPrefix()+methodName;
        LOGGER.debug("Found method:{}", methodName);

        List<PsiMethod> relevantMethods = regexUtil.findAllMethodsWithIndex(
                mapperProperties.getMapperMethodPrefix(),
                methodName,
                mapperProperties.getEditorClass()
        );
        LOGGER.debug("Found relevant methods:{}", relevantMethods);

        if(!relevantMethods.isEmpty()){
            int newIndex = regexUtil.getIncrementIndex(relevantMethods);
            LOGGER.trace("New index: {}", newIndex);
            result = result + newIndex;
        }

        return result;
    }
}
