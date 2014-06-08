package de.crawling.spider.idea.plugin.mapper.map;

import de.crawling.spider.idea.plugin.mapper.MapperProperties;

/**
 * Created by sscheffler on 08.06.14.
 */
public interface SmartMapper {

    public String buildMapperMethod(MapperProperties properties);
}
