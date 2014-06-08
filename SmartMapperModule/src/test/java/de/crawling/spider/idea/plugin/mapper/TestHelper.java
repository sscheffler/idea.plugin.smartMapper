package de.crawling.spider.idea.plugin.mapper;

import com.intellij.psi.PsiMethod;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by sscheffler on 08.06.14.
 */
public class TestHelper {

    protected PsiMethod[] createInputMethodTestData() {
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

    protected PsiMethod[] createOutputMethodTestData() {
        PsiMethod[] methods = new PsiMethod[3];

        PsiMethod m0 = mock(PsiMethod.class);
        when(m0.getName()).thenReturn("setMethod");
        methods[0] = m0;

        PsiMethod m1 = mock(PsiMethod.class);
        when(m1.getName()).thenReturn("setMethod1");
        methods[1] = m1;

        PsiMethod m2 = mock(PsiMethod.class);
        when(m2.getName()).thenReturn("setMethod2");
        methods[2] = m2;
        return methods;
    }
}
