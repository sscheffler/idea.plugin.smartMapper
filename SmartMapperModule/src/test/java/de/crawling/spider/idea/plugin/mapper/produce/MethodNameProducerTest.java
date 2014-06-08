package de.crawling.spider.idea.plugin.mapper.produce;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import de.crawling.spider.idea.plugin.mapper.TestHelper;
import de.crawling.spider.idea.plugin.mapper.util.MapperProperties;
import de.crawling.spider.idea.plugin.mapper.util.RegexUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MethodNameProducerTest extends TestHelper{

    public static final String SET = "set";
    public static final String METHOD = "Method";


    @Mock
    private RegexUtil regexUtil;

    @Mock
    PsiClass psiClass;

    @InjectMocks
    private MethodNameProducer methodNameProducer = MethodNameProducer.INSTANCE;

    PsiMethod[] methods;
    List<PsiMethod> retrieveMethods;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        methods = createInputMethodTestData();
        retrieveMethods = createOutputMethodTestData();

        when(psiClass.getMethods()).thenReturn(methods);
        when(regexUtil.findAllMethodsWithIndex(SET, METHOD, psiClass)).thenReturn(retrieveMethods);

    }

    /**
    * tests if the method neme will be generated correctly
    *
    * @author sscheffler
    * @date 08.06.14
    */
    @Test
    public final void testCreateMethodName() {
        MapperProperties properties = mock(MapperProperties.class);


    }


}