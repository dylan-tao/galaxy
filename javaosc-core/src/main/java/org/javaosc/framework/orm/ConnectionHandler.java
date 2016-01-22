package org.javaosc.framework.orm;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.javaosc.framework.assist.ClassLoader;
import org.javaosc.framework.constant.ProperConstant;
import org.javaosc.framework.context.BeanFactory;
import org.javaosc.framework.context.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ConnectionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(ConnectionHandler.class);
	
	private static DataSource ds;
	
	private static ThreadLocal<Connection> connManger = new ThreadLocal<Connection>();
	
	public static void init(){
		String poolName = Configuration.getValue(ProperConstant.POOL_DATASOURCE);
		Class<?> poolObj = ClassLoader.load(poolName);
		Object bean = BeanFactory.getBean(poolObj,false);
		try {
			BeanUtils.populate(bean, Configuration.getPoolPrm());
		} catch (IllegalAccessException e) {
			log.error(e);
		} catch (InvocationTargetException e) {
			log.error(e);
		}
		ds = (DataSource)bean;
		
		TestConn();
	}
	
	public static Connection getConnection(){
		Connection conn = connManger.get();
		if(conn == null){
			conn = newConn();
			connManger.set(conn);
		}
		return conn;
	}
	
	public static void beginTransaction(){
		Connection conn = getConnection();
		if(conn != null){
			try {
				if(conn.getAutoCommit()){
					conn.setAutoCommit(false);
				}
			} catch (SQLException e) {
				log.error(e);
			}
		}
	}
	
	public static void commit(){
		Connection conn = getConnection();
		if(conn != null){
			try {
				if(!conn.getAutoCommit()){
					conn.commit();
				}
			} catch (SQLException e) {
				log.error(e);
			}
		}
	}
	
	public static void rollback(){
		Connection conn = getConnection();
		if(conn != null){
			try {
				if(!conn.getAutoCommit()){
					conn.rollback();
				}
			} catch (SQLException e) {
				log.error(e);
			}
		}
	}
	
	public static void close(){
		Connection conn = connManger.get();
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e);
			}finally{
				connManger.remove();
			}
		}
	}
	
	public static void destroy(){
		close();
		connManger = null;
	}
	
	private static Connection newConn(){
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			log.error(e);
		}
		return null;
	}
	
	private static void TestConn(){
		getConnection();
		close();
	}
}
