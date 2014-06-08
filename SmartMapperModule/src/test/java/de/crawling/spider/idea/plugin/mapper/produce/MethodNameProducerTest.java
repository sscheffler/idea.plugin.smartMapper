package de.crawling.spider.idea.plugin.mapper.produce;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import de.crawling.spider.idea.plugin.mapper.TestHelper;
import de.crawling.spider.idea.plugin.mapper.util.MapperProperties;
import de.crawling.spider.idea.plugin.mapper.util.RegexUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MethodNameProducerTest extends TestHelper{

    public static final String SET_PREFIX = "set";
    public static final String METHOD_NAME = "Method";


    @Mock
    private RegexUtil regexUtilMock;

    @Mock
    PsiClass editorClassMock;

    @InjectMocks
    private MethodNameProducer methodNameProducer = MethodNameProducer.INSTANCE;

    PsiMethod[] methods;
    List<PsiMethod> retrieveMethods;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        methods = createInputMethodTestData();
        retrieveMethods = createOutputMethodTestData();

        when(editorClassMock.getMethods()).thenReturn(methods);
        when(regexUtilMock.findAllMethodsWithIndex(SET_PREFIX, METHOD_NAME, editorClassMock)).thenReturn(retrieveMethods);
        when(regexUtilMock.calculateClassName(METHOD_NAME)).thenReturn(METHOD_NAME);
        when(regexUtilMock.getIncrementIndex(anyList())).thenReturn(3);

    }

    /**
    * tests if the method neme will be generated correctly
    *
    * @author sscheffler
    * @date 08.06.14
    */
    @Test
    public final void testCreateMethodName() {
        MapperProperties propertiesMock = mock(MapperProperties.class);
        when(propertiesMock.getMapperMethodPrefix()).thenReturn(SET_PREFIX);
        when(propertiesMock.getSetterCanonicalClassName()).thenReturn(METHOD_NAME);
        when(propertiesMock.getEditorClass()).thenReturn(editorClassMock);

        String toTest = methodNameProducer.produceMethodName(propertiesMock);

        Assert.assertEquals(SET_PREFIX+METHOD_NAME+"3", toTest);


    }


}