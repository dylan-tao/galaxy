package org.javaosc.framework.jdbc.pool;

import javax.sql.DataSource;

import org.javaosc.framework.util.StringUtil;

import com.mchange.v2.c3p0.ComboPooledDataSource;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class C3p0Handler implements PoolHandler{
	
	private String driverClass;
	private String jdbcUrl;
	private String user;
	private String password;
	
	private Integer minPoolSize;
	private Integer maxPoolSize;
	private Integer initialPoolSize;
	private Integer maxIdleTime;
	private Integer acquireIncrement;
	private Integer maxStatements;
	private Integer idleConnectionTestPeriod;
	private Integer acquireRetryAttempts;
	private Boolean breakAfterAcquireFailure;
	private Boolean testConnectionOnCheckout;
	
	@Override
	public DataSource getDataSource() throws Exception {
		if(StringUtil.isBlank(driverClass) || StringUtil.isBlank(jdbcUrl) || StringUtil.isBlank(user)){
			throw new Exception("db.driverClass,db.jdbcUrl,db.user can not be null! ");
		}
		ComboPooledDataSource ds = new ComboPooledDataSource(true);
		ds.setDriverClass(driverClass);
		ds.setJdbcUrl(jdbcUrl);
		ds.setUser(user);
		ds.setPassword(password);
		
		ds.setMinPoolSize(minPoolSize==null?5:minPoolSize);
		ds.setMaxPoolSize(maxPoolSize==null?30:maxPoolSize);
		ds.setInitialPoolSize(initialPoolSize==null?10:initialPoolSize);
		ds.setMaxIdleTime(maxIdleTime==null?60:maxIdleTime);
		ds.setAcquireIncrement(acquireIncrement==null?5:acquireIncrement);
		ds.setMaxStatements(maxStatements==null?0:maxStatements);
		ds.setIdleConnectionTestPeriod(idleConnectionTestPeriod==null?60:idleConnectionTestPeriod);
		ds.setAcquireRetryAttempts(acquireRetryAttempts==null?30:acquireRetryAttempts);
		ds.setBreakAfterAcquireFailure(breakAfterAcquireFailure==null?false:breakAfterAcquireFailure);
		ds.setTestConnectionOnCheckout(testConnectionOnCheckout==null?false:testConnectionOnCheckout);
		return ds;
	}
	
	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getMinPoolSize() {
		return minPoolSize;
	}

	public void setMinPoolSize(Integer minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	public Integer getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(Integer maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public Integer getInitialPoolSize() {
		return initialPoolSize;
	}

	public void setInitialPoolSize(Integer initialPoolSize) {
		this.initialPoolSize = initialPoolSize;
	}

	public Integer getMaxIdleTime() {
		return maxIdleTime;
	}

	public void setMaxIdleTime(Integer maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}

	public Integer getAcquireIncrement() {
		return acquireIncrement;
	}

	public void setAcquireIncrement(Integer acquireIncrement) {
		this.acquireIncrement = acquireIncrement;
	}

	public Integer getMaxStatements() {
		return maxStatements;
	}

	public void setMaxStatements(Integer maxStatements) {
		this.maxStatements = maxStatements;
	}

	public Integer getIdleConnectionTestPeriod() {
		return idleConnectionTestPeriod;
	}

	public void setIdleConnectionTestPeriod(Integer idleConnectionTestPeriod) {
		this.idleConnectionTestPeriod = idleConnectionTestPeriod;
	}

	public Integer getAcquireRetryAttempts() {
		return acquireRetryAttempts;
	}

	public void setAcquireRetryAttempts(Integer acquireRetryAttempts) {
		this.acquireRetryAttempts = acquireRetryAttempts;
	}

	public Boolean getBreakAfterAcquireFailure() {
		return breakAfterAcquireFailure;
	}

	public void setBreakAfterAcquireFailure(Boolean breakAfterAcquireFailure) {
		this.breakAfterAcquireFailure = breakAfterAcquireFailure;
	}

	public Boolean getTestConnectionOnCheckout() {
		return testConnectionOnCheckout;
	}

	public void setTestConnectionOnCheckout(Boolean testConnectionOnCheckout) {
		this.testConnectionOnCheckout = testConnectionOnCheckout;
	}

}
