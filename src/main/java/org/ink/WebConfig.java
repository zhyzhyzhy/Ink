package org.ink;

import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * load the configure file
 * the main method is in the static block
 *
 * @author zhuyichen
 */
public class WebConfig {

    private static final Properties properties = new Properties();
    private static final Logger log = LoggerFactory.getLogger(WebConfig.class);

    //securitykey =
    public static String SECURITY_KEY;
    private static final String DEFAULT_SECURITY = "Ink";

    //datasource_url =
    public static String DATASOURCE_URL;
    //datasource_driver =
    public static String DATASOURCE_DRIVER;
    //datasource_username =
    public static String DATASOURCE_USERNAME;
    //datasource_password =
    public static String DATASOURCE_PASSWORD;

    //mybatis_config_file_name =
    public static String MYBATIS_CONFIG_FILE_NAME;
    //mybatis_environment =
    public static String MYBATIS_ENVIRONMENT;

    static {
        try {
            InputStream inputStream = getClassLoader().getResourceAsStream("app.properties");
            InputStreamReader reader = new InputStreamReader(inputStream);
            properties.load(reader);
        } catch (IOException e) {
            log.info("No Config File Mode");
        }
    }

    /**
     * load SecurityKey if you open anthentication mode
     */
    public void loadSecurityKey() {
        SECURITY_KEY = properties.getProperty("SecurityKey");
        if (SECURITY_KEY == null) {
            log.info("No SecurityKey Is Set, Use Default key \" {} \"", DEFAULT_SECURITY);
            SECURITY_KEY = DEFAULT_SECURITY;
        }
    }

    /**
     * load data source config
     */
    public void loadDataSource() {
        DATASOURCE_URL = properties.getProperty("datasource_url");
        DATASOURCE_DRIVER = properties.getProperty("datasource_driver");
        DATASOURCE_USERNAME = properties.getProperty("datasource_username");
        DATASOURCE_PASSWORD = properties.getProperty("datasource_password");
    }

    /**
     * load mybatis config file
     */
    public void loadMybatisConfig() {
        MYBATIS_CONFIG_FILE_NAME = properties.getProperty("mybatis_config_file_name");
    }

    /**
     * load mybatis config environment
     */
    public void loadMybatisEnv() {
        MYBATIS_ENVIRONMENT = properties.getProperty("mybatis_environment");
    }

    /**
     * get the query result
     *
     * @param name query keyword
     * @return result of the query word
     */
    public static String getConfig(String name) {
        return properties.getProperty(name);
    }

    /**
     * @return configure file
     */
    public static Properties getConfigProperties() {
        return properties;
    }

    /**
     * get classLoader to loader configure file
     *
     * @return classloader
     */
    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
