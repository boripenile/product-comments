package com.example.app.products.util;

import java.beans.PropertyVetoException;
import java.util.ResourceBundle;

import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public enum ConnectionUtil {

	INSTANCE;
	
	public Configuration getConfiguration() {
		ResourceBundle persistence = ResourceBundle.getBundle("persistence");
		Configuration configuration = new DefaultConfiguration();
		
		configuration.set(SQLDialect.MYSQL_5_7);
		
		ComboPooledDataSource datasource = new ComboPooledDataSource();
		try {
			datasource.setDriverClass("com.mysql.jdbc.Driver");
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		datasource.setJdbcUrl(persistence.getString("dataSource.url"));
		datasource.setUser(persistence.getString("dataSource.username"));
		datasource.setPassword(persistence.getString("dataSource.password"));

		datasource.setInitialPoolSize(2);
		datasource.setMinPoolSize(2);
		datasource.setAcquireIncrement(1);
		datasource.setMaxPoolSize(10);
		datasource.setAcquireRetryAttempts(5);
		datasource.setAutoCommitOnClose(true);
		datasource.setBreakAfterAcquireFailure(false);
		configuration.set(datasource);
		
		configuration.set(datasource);
		
		return configuration;
	}
	
	
	
}
