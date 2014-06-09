package de.crawling.spider.idea.plugin.mapper.produce;

import de.crawling.spider.idea.plugin.mapper.model.MapperProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: implement me
 *
 * @author sscheffler(stefan-scheffler@web.de)
 * @date 09.06.14
 */
public class CommentProducer {

    private final static Logger LOGGER = LoggerFactory.getLogger(CommentProducer.class);
    public final static CommentProducer INSTANCE = new CommentProducer();

    private CommentProducer() {
    }


    public String produceMethodMappingComment(MapperProperties properties){
        final StringBuilder builder = new StringBuilder();
        builder.append("/**\n");
        builder.append("/**\n");

        builder.append("**/\n");


        return builder.toString();
    }


}
