package de.crawling.spider.idea.plugin.mapper.util;

import com.intellij.psi.PsiMethod;

import java.util.Comparator;

/**
 * Created by sscheffler on 08.06.14.
 */
public class PsiMethodComparator implements Comparator<PsiMethod> {

    public static final PsiMethodComparator INSTANCE  = new PsiMethodComparator();




    private PsiMethodComparator() {
    }

    @Override
    public int compare(PsiMethod o1, PsiMethod o2) {
        if (o1.getName() == null && o2.getName() == null) {
            return 0;
        }
        if (o2.getName() == null) {
            return 1;
        }
        if (o1.getName() == null) {
            return -1;
        }
        return o1.getName().compareTo(o2.getName());
    }


}
