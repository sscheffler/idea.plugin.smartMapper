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
public class RegexUtilTest {

    public static final String CANONICAL_CLASS_TEXT = "de.foo.Bar";
    private RegexUtil regexUtil = RegexUtil.INSTANCE;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
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
        PsiMethod[] methods = createMethodTestData();
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

    private PsiMethod[] createMethodTestData() {
        PsiMethod[] methods = new PsiMethod[4];

        PsiMethod m0 = mock(PsiMethod.class);
        when(m0.getName()).thenReturn("setMethod");
        methods[0] = m0;

        PsiMethod m1 = mock(PsiMethod.class);
        when(m1.getName()).thenReturn("setMethod1");
        methods[1] = m1;

        PsiMethod m2 = mock(PsiMethod.class);
        when(m2.getName()).thenReturn("setMethod2");
        methods[2] = m2;

        PsiMethod m3 = mock(PsiMethod.class);
        when(m3.getName()).thenReturn("setDoNotReturn");
        methods[3] = m3;
        return methods;
    }
}