package de.crawling.spider.idea.plugin.mapper.model;


/**
 * TODO: really needed?
 *
 * @author sscheffler(stefan-scheffler@web.de)
 * @date 09.06.14
 */
public class MappingResults {

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
