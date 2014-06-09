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


    public void produceDefaultValue(PsiParameter psiParameter) {
        
    }
}
