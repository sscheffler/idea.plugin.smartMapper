package de.crawling.spider.idea.plugin.mapper;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import de.crawling.spider.idea.plugin.mapper.gui.PluginMainDialog;
import de.crawling.spider.idea.plugin.mapper.map.DefaultSmartMapperImpl;
import de.crawling.spider.idea.plugin.mapper.map.SmartMapper;
import de.crawling.spider.idea.plugin.mapper.update.DefaultUpdaterImpl;
import de.crawling.spider.idea.plugin.mapper.update.Updater;
import de.crawling.spider.idea.plugin.mapper.util.MapperHelper;
import de.crawling.spider.idea.plugin.mapper.model.MapperProperties;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * Created by sscheffler on 24.05.14.
 */
public class SmartMapperEditorPopupAction extends AnAction {

    private final static Logger LOGGER = LoggerFactory.getLogger(SmartMapperEditorPopupAction.class);
    private Updater updater;
    private SmartMapper smartMapper = new DefaultSmartMapperImpl();
    private MapperHelper mapperHelper = MapperHelper.INSTANCE;

    public void actionPerformed(AnActionEvent e) {
        startMainDialog(e);
    }

    private void startMainDialog(AnActionEvent e){
        try {
            PropertyConfigurator.configure("/home/sscheffler/.repository/idea.plugin.smartMapper/SmartMapperModule/src/main/resources/log4j.properties");
            final Project project = e.getProject();
            PluginMainDialog dialog = new PluginMainDialog(project);

            dialog.show();
            MapperProperties properties = dialog.getMapperProperties();

            if(dialog.isOK() && properties.propertiesValid()){
                LOGGER.debug("Mapper properties are valid. Method will be build");

                properties.setEditorClass(mapperHelper.retrieveEditorClass(e));
                updater = new DefaultUpdaterImpl(project);
                String methodString = smartMapper.buildMapperMethod(properties);
                updater.updateClassWithMethod(methodString, dialog.getSelectedSetterClass());
            }

        }catch(IllegalArgumentException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "No editor or wrong file open", JOptionPane.INFORMATION_MESSAGE);
        }
    }



}
