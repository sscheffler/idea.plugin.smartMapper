package de.crawling.spider.idea.plugin.mapper.produce;

import de.crawling.spider.idea.plugin.mapper.TestHelper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class MethodNameProducerTest extends TestHelper{

    @InjectMocks
    MethodNameProducer methodNameProducer = MethodNameProducer.INSTANCE;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /**
    * tests if the method neme will be generated correctly
    *
    * @author sscheffler
    * @date 08.06.14
    */
    @Test
    public final void testCreateMethodName() {

    }
}