package de.crawling.spider.idea.plugin.mapper.update;

import com.intellij.psi.PsiClass;
import de.crawling.spider.idea.plugin.mapper.model.MapperProperties;

/**
 * Created by sscheffler on 08.06.14.
 */
public interface Updater {

    public void updateClassWithMethod(String methodString, MapperProperties properties);
}
