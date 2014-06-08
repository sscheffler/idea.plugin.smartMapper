package de.crawling.spider.idea.plugin.mapper.update;

import com.intellij.psi.PsiClass;

/**
 * Created by sscheffler on 08.06.14.
 */
public interface Updater {

    public void updateClassWithMethod(String methodString, PsiClass updateClass);
}
