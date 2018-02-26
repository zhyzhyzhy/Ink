package org.ink.db.mybatis;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.ink.WebConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * Created by zhuyichen on 2017/8/9.
 */
public class MybatisConfig {

    private static DataSource dataSource;
    private static Configuration configuration;
    private static SqlSessionFactory sqlSessionFactory;

    public static final Logger log = LoggerFactory.getLogger(MybatisConfig.class);

    public static void configure(Object object) {
        if (WebConfig.SECURITY_KEY == null) {
            log.info("no db configure");
            return;
        }
        if (WebConfig.MYBATIS_CONFIG_FILE_NAME == null) {
            log.info("no datasource found");
            return;
        }
        MybatisConfig.dataSource = null;

        TransactionFactory transactionFactory = new JdbcTransactionFactory();
//        Environment environment = new Environment("development", transactionFactory, dataSource);
//        MybatisConfig.configuration = new Configuration(environment);
    }

    public static void addMapper(Class<?> clazz) {
        MybatisConfig.configuration.addMapper(clazz);
    }

    public static void buildSessionFactory() {
        MybatisConfig.sqlSessionFactory = new SqlSessionFactoryBuilder().build(MybatisConfig.configuration);
    }

    public static <T> T getMapper(Class<? extends T> clazz) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession.getMapper(clazz);
    }


}
