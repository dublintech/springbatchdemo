package com.prototypes.aug.extract.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatasourceConfiguration {
	
    @Autowired
    private Environment env;    
   
    @Bean
	JdbcTemplate dwJdbcTemplate() {
    		JdbcTemplate dwDatasource = new JdbcTemplate(dwDatasource());
    		return dwDatasource;
    }
    
    
    @Bean
    @ConfigurationProperties(prefix="spring.oltp.datasource")
    public DataSource oltpDataSource(){
        return DataSourceBuilder.create().build();
    }
	
    @Bean
    @ConfigurationProperties(prefix="spring.batch.datasource")
    public DataSource dataSource(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(env.getProperty("spring.batch.datasource.driver-class-name"));
	    dataSource.setUrl(env.getProperty("spring.batch.datasource.url"));
	    dataSource.setUsername(env.getProperty("spring.batch.datasource.username"));
	    dataSource.setPassword(env.getProperty("spring.batch.datasource.password"));
	    
	    try {
    			System.out.println("dataSource(), batchmetadata" + dataSource.getConnection().getMetaData());
	    } catch (Exception ex) {
	    		ex.printStackTrace();
	    }
	    return dataSource;
    }
    
    
    @Bean
    @ConfigurationProperties(prefix="spring.dw.database")
    public DataSource dwDatasource(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName(env.getProperty("spring.dw.datasource.driver-class-name"));
	    dataSource.setUrl(env.getProperty("spring.dw.datasource.url"));
	    dataSource.setUsername(env.getProperty("spring.dw.datasource.username"));
	    dataSource.setPassword(env.getProperty("spring.dw.datasource.password"));
	    
	    try {
			System.out.println("dwDatasource(), batchmetadata" + dataSource.getConnection().getMetaData());
	    } catch (Exception ex) {
	    		ex.printStackTrace();
	    }
        return dataSource;
    }

}
