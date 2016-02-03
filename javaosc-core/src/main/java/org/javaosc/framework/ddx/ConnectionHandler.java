package org.javaosc.framework.ddx;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.javaosc.framework.assist.ClassHandler;
import org.javaosc.framework.constant.Constant;
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
		if(poolName.indexOf("java:")==0){ //tomcat jdbc
			try {
				Context c = new InitialContext();
				ds = (DataSource)c.lookup(poolName);
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}else{ //jdbc pool
			Class<?> poolObj = ClassHandler.load(poolName);
			Object bean = BeanFactory.getBean(poolObj,false);
			try {
				BeanUtils.populate(bean, Configuration.getPoolPrm());
				log.info("initializing the data source property");
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			ds = (DataSource)bean;
		}
		log.info("initializing the data source connection ~");
		InitialzeConnection();
	}
	
	public static Connection getConnection(){
		Connection conn = connManger.get();
		if(conn == null){
			conn = getDataSourceConn();
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
				log.error(Constant.JAVAOSC_EXCEPTION, e);
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
				log.error(Constant.JAVAOSC_EXCEPTION, e);
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
				log.error(Constant.JAVAOSC_EXCEPTION, e);
			}
		}
	}
	
	public static void close(){
		Connection conn = connManger.get();
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(Constant.JAVAOSC_EXCEPTION, e);
			}finally{
				connManger.remove();
			}
		}
	}
	
	public static void destroy(){
		close();
		connManger = null;
	}
	
	private static Connection getDataSourceConn(){
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			log.error(Constant.JAVAOSC_EXCEPTION, e);
		}
		return null;
	}
	
	private static void InitialzeConnection(){
		getConnection();
		close();
	}
}
