package de.crawling.spider.idea.plugin.mapper.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: really needed?
 *
 * @author sscheffler(stefan-scheffler@web.de)
 * @date 09.06.14
 */
public class MappingResults {

    private final static Logger LOGGER = LoggerFactory.getLogger(MappingResults.class);
    public final static MappingResults INSTANCE = new MappingResults();

    private MappingResults() {
    }

    private Class methodReturnClass;
    private Class methodMappingClass;

    public Class getMethodReturnClass() {
        return methodReturnClass;
    }

    public void setMethodReturnClass(Class methodReturnClass) {
        this.methodReturnClass = methodReturnClass;
    }

    public Class getMethodMappingClass() {
        return methodMappingClass;
    }

    public void setMethodMappingClass(Class methodMappingClass) {
        this.methodMappingClass = methodMappingClass;
    }
}
