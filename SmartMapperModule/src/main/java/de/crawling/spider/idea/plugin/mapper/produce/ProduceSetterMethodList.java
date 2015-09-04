package de.crawling.spider.idea.plugin.mapper.produce;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.util.containers.SortedList;
import de.crawling.spider.idea.plugin.mapper.util.PsiMethodComparator;

/**
 * @author Stefan scheffler (sscheffler@avantgarde-labs.de)
 * @date 17.06.14
 */
public class ProduceSetterMethodList {
    /**
     * The logger of this class.
     */
    public java.util.List<PsiMethod> produceSetterMethodLists(PsiClass psiClass){
        java.util.List<PsiMethod> sortedList = new SortedList<>(PsiMethodComparator.INSTANCE);
        for (PsiMethod setterMethod : psiClass.getAllMethods()) {

            if (setterMethod.getName().startsWith("set")) {
                sortedList.add(setterMethod);
            }
        }
        return sortedList;
    }
}
