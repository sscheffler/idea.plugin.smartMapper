package de.crawling.spider.idea.plugin.mapper.produce;

import de.crawling.spider.idea.plugin.mapper.util.MapperProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sscheffler on 08.06.14.
 */
public class MethodNameProducer {

    private final static Logger LOGGER = LoggerFactory.getLogger(MethodNameProducer.class);

    public final static MethodNameProducer INSTANCE = new MethodNameProducer();

    private MethodNameProducer() {
    }

    public String produceMethodName(final MapperProperties mapperProperties){
        return "";
    }
}
