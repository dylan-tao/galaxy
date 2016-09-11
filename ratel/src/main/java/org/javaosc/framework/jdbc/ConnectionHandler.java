package org.javaosc.framework.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.jdbc.pool.DataSourceMatch;
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
		ds = DataSourceMatch.get();
		if(ds!=null){
			log.info("initializing the data source connection ~");
			initialzeConnection();
		}
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
		ds = null;
	}
	
	private static Connection getDataSourceConn(){
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			log.error(Constant.JAVAOSC_EXCEPTION, e);
		}
		return null;
	}
	
	private static void initialzeConnection(){
		getConnection();
		close();
	}
}
