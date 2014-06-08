package de.crawling.spider.idea.plugin.mapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author sscheffler(stefan-scheffler@web.de)
 * @date 08.06.14
 */
public class RegexUtilTest {

    public static final String CANONICAL_CLASS_TEXT = "de.foo.Bar";
    private RegexUtil regexUtil = RegexUtil.INSTANCE;

    @Before
    public void setUp() throws Exception {
    }

    /**
     * public List<String> findAllMethodsWithIndex(String mapperMethodPrefix, String methodName, PsiClass editorClass)
     * public String calculateClassName(String canonicalText) {
     * public String calculateVariableName(String canonicalText) {
     */

    /**
    * test calculateClassName
    *
    * @author sscheffler
    * @date 08.06.14
    */
    @Test
    public final void testCalculateClassName() {
        String toTest = regexUtil.calculateClassName(CANONICAL_CLASS_TEXT);
        assertEquals("Bar", toTest);
    }

    /**
    * test calculateVariableName
    *
    * @author sscheffler
    * @date 08.06.14
    */
    @Test
    public final void testCalculateVariableName() {
        String toTestCanonical = regexUtil.calculateVariableName(CANONICAL_CLASS_TEXT);
        String toTestNonCanonical = regexUtil.calculateVariableName("Bar");
        String expected = "bar";
        assertEquals(expected, toTestCanonical);
        assertEquals(expected, toTestNonCanonical);
    }

    /**
    * test: test errors of method findAllMethodsWithIndex
    *
    * @author sscheffler
    * @date 08.06.14
    */
    @Test
    public final void testFindAllMethodsError() {
        List<String> toTest = regexUtil.findAllMethodsWithIndex("", "", null);
        assertEquals(0, toTest.size());

        toTest = regexUtil.findAllMethodsWithIndex("bla", "weh", null);
        assertEquals(0, toTest.size());

        toTest = regexUtil.findAllMethodsWithIndex("bla", null, null);
        assertEquals(0, toTest.size());
    }

    /**
    * test success findAllMethodsWithIndex
    *
    * @author sscheffler
    * @date 08.06.14
    */
    @Test
    public final void testFindAllMethodsSuccess() {
        
    }
}