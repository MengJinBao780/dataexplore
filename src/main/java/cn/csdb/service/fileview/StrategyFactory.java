package cn.csdb.service.fileview;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Administrator on 2017/9/7 0007.
 */
@Service
public class StrategyFactory {

    private Properties strategies;
    private static final String STRATEGY_CLASSNAME = "Strategy";


    @PostConstruct
    private void initWac() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("/view.properties");
        strategies = PropertiesLoaderUtils.loadProperties(classPathResource);
    }

    public Strategy getStrategy(String viewType) {
        if (StringUtils.isBlank(viewType)) {
            return null;
        }
        String strategiesProperty = strategies.getProperty(viewType);
        if (StringUtils.isBlank(strategiesProperty)) {
            return null;
        }
        return ApplicationContextProvider.getApplicationContext().getBean(strategiesProperty + STRATEGY_CLASSNAME, Strategy.class);

    }

}
