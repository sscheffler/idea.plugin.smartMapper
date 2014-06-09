package de.crawling.spider.idea.plugin.mapper.produce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sscheffler on 09.06.14.
 */
public class CommentProducer {

    private final static Logger LOGGER = LoggerFactory.getLogger(CommentProducer.class);
    public final static CommentProducer INSTANCE = new CommentProducer();

    private CommentProducer() {
    }


    public String produceMethodMappingComment(){
        return null;
    }


}
