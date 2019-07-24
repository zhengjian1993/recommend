package com.hjucook.recommend.common.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.hjucook.recommend.common.interceptor.PerformanceInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * mybatis 配置数据源类
 * 
 * @author wanghaobin
 * @date 2016年12月15日
 * @since 1.7
 */
@Configuration
@EnableTransactionManagement
public class MybatisConfiguration implements EnvironmentAware {
    private String driveClassName;
    private String url;
    private String userName;
    private String password;
    private String xmlLocation;
    private String typeAliasesPackage;
    /**
     * druid参数
     */
    private String maxActive;
    private String initialSize;
    private String maxWait;
    private String minIdle;
    private String timeBetweenEvictionRunsMillis;
    private String minEvictableIdleTimeMillis;
    private String validationQuery;
    private String testWhileIdle;
    private String testOnBorrow;
    private String testOnReturn;
    private String poolPreparedStatements;
    private String maxOpenPreparedStatements;
    @Bean
    public DataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(userName);
        druidDataSource.setPassword(password);
        druidDataSource.setDriverClassName(StringUtils.isNotBlank(driveClassName)?driveClassName:"com.mysql.jdbc.Driver");
        druidDataSource.setMaxActive(StringUtils.isNotBlank(maxActive)? Integer.parseInt(maxActive):10);
        druidDataSource.setInitialSize(StringUtils.isNotBlank(initialSize)? Integer.parseInt(initialSize):1);
        druidDataSource.setMaxWait(StringUtils.isNotBlank(maxWait)? Integer.parseInt(maxWait):60000);
        druidDataSource.setMinIdle(StringUtils.isNotBlank(minIdle)? Integer.parseInt(minIdle):3);
        druidDataSource.setTimeBetweenEvictionRunsMillis(StringUtils.isNotBlank(timeBetweenEvictionRunsMillis)?
                Integer.parseInt(timeBetweenEvictionRunsMillis):60000);
        druidDataSource.setMinEvictableIdleTimeMillis(StringUtils.isNotBlank(minEvictableIdleTimeMillis)?
                Integer.parseInt(minEvictableIdleTimeMillis):300000);
        druidDataSource.setValidationQuery(StringUtils.isNotBlank(validationQuery)?validationQuery:"select 'x'");
        druidDataSource.setTestWhileIdle(Boolean.parseBoolean(testWhileIdle));
        druidDataSource.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));
        druidDataSource.setTestOnReturn(Boolean.parseBoolean(testOnReturn));
        druidDataSource.setPoolPreparedStatements(Boolean.parseBoolean(poolPreparedStatements));
        druidDataSource.setMaxOpenPreparedStatements(StringUtils.isNotBlank(maxOpenPreparedStatements)? Integer.parseInt(maxOpenPreparedStatements):20);
        try {
            druidDataSource.setFilters("stat, wall");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return druidDataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(DataSource druidDataSource) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(druidDataSource);
        if(StringUtils.isNotBlank(typeAliasesPackage)){
            bean.setTypeAliasesPackage(typeAliasesPackage);
        }
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Interceptor[] plugins =  new Interceptor[]{new PerformanceInterceptor()};
        bean.setPlugins(plugins);
        try {
            bean.setMapperLocations(resolver.getResources(xmlLocation));
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.url = environment.getProperty("spring.datasource.url");
        this.userName= environment.getProperty("spring.datasource.username");
        this.password = environment.getProperty("spring.datasource.password");
        this.driveClassName = environment.getProperty("spring.datasource.driver-class-name");
        this.maxActive = environment.getProperty("spring.datasource.dbcp2.max-total");
        this.initialSize = environment.getProperty("spring.datasource.dbcp2.initial-size");
        this.maxWait = environment.getProperty("spring.datasource.dbcp2.max-wait-millis");
        this.minIdle = environment.getProperty("spring.datasource.dbcp2.min-idle");
        this.timeBetweenEvictionRunsMillis = environment.getProperty("spring.datasource.dbcp2.time-between-eviction-runs-millis");
        this.minEvictableIdleTimeMillis = environment.getProperty("spring.datasource.dbcp2.min-evictable-idle-time-millis");
        this.validationQuery = environment.getProperty("spring.datasource.dbcp2.validation-query");
        this.testWhileIdle = environment.getProperty("spring.datasource.dbcp2.test-while-idle");
        this.testOnBorrow = environment.getProperty("spring.datasource.dbcp2.test-on-borrow");
        this.testOnReturn = environment.getProperty("spring.datasource.dbcp2.test-on-return");
        this.poolPreparedStatements = environment.getProperty("spring.datasource.dbcp2.pool-prepared-statements");
        this.maxOpenPreparedStatements = environment.getProperty("spring.datasource.dbcp2.max-open-prepared-statements");
        this.typeAliasesPackage = environment.getProperty("mybatis.type-aliases-package");
        this.xmlLocation = environment.getProperty("mybatis.mapper-locations");
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource druidDataSource) {
        return new DataSourceTransactionManager(druidDataSource);
    }

}
