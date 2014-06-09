package de.crawling.spider.idea.plugin.mapper;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import de.crawling.spider.idea.plugin.mapper.util.RegexUtil;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author sscheffler(stefan-scheffler@web.de)
 * @date 08.06.14
 */
public class RegexUtilTest extends TestHelper{

    public static final String CANONICAL_CLASS_TEXT = "de.foo.Bar";
    private RegexUtil regexUtil = RegexUtil.INSTANCE;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * public List<String> findAllMethodsWithIndex(String mapperMethodPrefix, String methodName, PsiClass editorClass)
     * public String calculateClassName(String canonicalText) {
     * public String calculateVariableNameFromClass(String canonicalText) {
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
    * test calculateVariableNameFromClass
    *
    * @author sscheffler
    * @date 08.06.14
    */
    @Test
    public final void testCalculateVariableName() {
        String toTestCanonical = regexUtil.calculateVariableNameFromClass(CANONICAL_CLASS_TEXT);
        String toTestNonCanonical = regexUtil.calculateVariableNameFromClass("Bar");
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
        List<PsiMethod> toTest = regexUtil.findAllMethodsWithIndex("", "", null);
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
        PsiMethod[] methods = createInputMethodTestData();
        PsiClass psiClass = mock(PsiClass.class);

        when(psiClass.getMethods()).thenReturn(methods);

        List<PsiMethod> toTest = regexUtil.findAllMethodsWithIndex("set", "Method", psiClass);
        assertNotNull(toTest);
        assertEquals(3, toTest.size());
        for(PsiMethod method : toTest){
            if(!StringUtils.startsWith(method.getName(), "setMethod")){
                fail(method.getName()+" does not match");
            }
        }
    }

    /**
    * tests if the new index of a List is calculated correctly
    *
    * @author sscheffler
    * @date 08.06.14
    */
    @Test
    public final void testGetIncrementIndex() {
        List<PsiMethod> testData = createOutputMethodTestData();
        int toTest = regexUtil.getIncrementIndex(testData);
        assertEquals(3, toTest);

    }


}