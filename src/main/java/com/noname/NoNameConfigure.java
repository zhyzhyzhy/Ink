package com.noname;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by zhuyichen on 2017/7/12.
 */

public abstract class NoNameConfigure {

    public abstract String[] beansPackage();

    //jwt的key
    public String SecurityKey() {
        return "Love";
    }

    public boolean anthenticationOpen() {
        return false;
    }

    public String mybatisConfigFileName() {
        return "mybatis-config.xml";
    }
    public DataSource mybatisDataSource() {
        PooledDataSourceFactory dataSourceFactory = new PooledDataSourceFactory();
        Properties properties = new Properties();
        properties.setProperty("url","jdbc:mysql://localhost:3306/mybatis");
        properties.setProperty("driver", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("username", "root");
        properties.setProperty("password", "");
        dataSourceFactory.setProperties(properties);
        return dataSourceFactory.getDataSource();
    }

    public String mybatisEnvironment() {
        return "development";
    }
}
