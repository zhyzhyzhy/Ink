package org.ink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * 读取配置文件配置
 *
 *
 * @author zhuyichen
 */
public class WebConfig {

    private static final Properties properties = new Properties();
    private static final Logger log = LoggerFactory.getLogger(WebConfig.class);

    static {
        InputStream inputStream = getClassLoader().getResourceAsStream("app.properties");
        InputStreamReader reader = new InputStreamReader(inputStream);
        try {
            properties.load(reader);
        } catch (IOException e) {
            log.info("No Config File Mode");
        }
    }

    public static String getConfig(String name) {
        return properties.getProperty(name);
    }

    public static Properties getConfigProperties() {
        return properties;
    }

    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
