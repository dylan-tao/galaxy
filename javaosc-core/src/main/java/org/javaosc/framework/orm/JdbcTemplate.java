package org.javaosc.framework.orm;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javaosc.framework.constant.Page;


/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-10-26
 * Copyright 2014 uufast Team. All Rights Reserved.
 */
public class JdbcTemplate{
	
	private static final Log log = LogFactory.getLog(JdbcTemplate.class);
	
	private static QueryRunner runner;
	
	static{
		runner = new QueryRunner();
	}
	
	/*=================== 查询单列操作 =====================*/
	
	public <T> T queryForColumn(String sql,String columnName, Class<T> cls, Object... param){
		T obj = null;
		try {
			if(param != null && param.length > 0){
				obj = runner.query(ConnectionHandler.getConnection(), sql, new ScalarHandler<T>(columnName), param);
			}else{
				obj = runner.query(ConnectionHandler.getConnection(), sql, new ScalarHandler<T>(columnName));
			}
		} catch (SQLException e) {
			log.error(e);
		}
		return obj;
	}
	
	public <T> List<T> queryForColumnList(String sql,String columnName,Class<T> cls, Object... param){
		List<T> list = null;
		try {
			if(param != null && param.length > 0){
				list = runner.query(ConnectionHandler.getConnection(), sql, new ColumnListHandler<T>(columnName), param);
			}else{
				list = runner.query(ConnectionHandler.getConnection(), sql, new ColumnListHandler<T>(columnName));
			}
		} catch (SQLException e) {
			log.error(e);
		}
		return list;
	}
	
	/*=================== 查询多列操作 =====================*/
	
	public Map<String, Object> queryForMap(String sql, Object... param){
		 Map<String, Object> map = null;
		 try {
			if(param != null && param.length > 0){
				map = runner.query(ConnectionHandler.getConnection(), sql, new MapHandler(), param);
			}else{
				map = runner.query(ConnectionHandler.getConnection(), sql, new MapHandler());
			}
		 } catch (SQLException e) {
			log.error(e);
		 }
		 return map;	
	}
	
	public List<Map<String, Object>> queryForMapList(String sql, Object... param){
		 List<Map<String, Object>> list = null;
		 try {
			if(param != null && param.length > 0){
				list = runner.query(ConnectionHandler.getConnection(), sql, new MapListHandler(), param);
			}else{
				list = runner.query(ConnectionHandler.getConnection(), sql, new MapListHandler());
			}
		 } catch (SQLException e) {
			log.error(e);
		 }
		 return list;	
	}
	
	/*=================== 查询单表或多表操作 =====================*/
	
	public <T> T queryForObject(String sql, Class<T> cls, Object... param){
		T obj = null;
		try {
			if(param != null && param.length > 0){
				obj = runner.query(ConnectionHandler.getConnection(), sql, new BeanHandler<T>(cls), param);
			}else{
				obj = runner.query(ConnectionHandler.getConnection(), sql, new BeanHandler<T>(cls));
			}
		} catch (SQLException e) {
			log.error(e);
		}
		return obj;
	}
	
	public <T> List<T> queryForList(String sql, Class<T> cls, Object... param){
		List<T> list = null;
		try {
			if(param != null && param.length > 0){
				list = runner.query(ConnectionHandler.getConnection(), sql, new BeanListHandler<T>(cls), param);
			}else{
				list = runner.query(ConnectionHandler.getConnection(), sql, new BeanListHandler<T>(cls));
			}
		} catch (SQLException e) {
			log.error(e);
		}
		return list;
	}
	
	public <T> Page<T> queryForPage(String sql, Page<T> page,Class<T> cls, Object... param){
		if(null == page){
			page = new Page<T>();
		}
		//执行顺序：where->group by->having-order by->limit
		if(page.isAutoCount()){
			String countSql = SqlHandler.createCount(sql);
			long count = getCount(countSql, Long.class, param);
			page.setTotalCount(count);
		}
		int pageNo = page.getPageNo();
		int pageSize = page.getPageSize();
		int startIndex =(pageNo-1) * pageSize;
		sql = SqlHandler.createLimit(sql, startIndex, pageSize); //拼组limit	
		List<T> list = queryForList(sql, cls, param);
		page.setResult(list);
		return page;
	}
	
	
	/*=================== 扩展操作 =====================*/
	
	public <T> T getCount(String sql, Class<T> cls, Object... param){
		T count = cls.cast(0);
		try {
			if(param != null && param.length > 0){
				count = runner.query(ConnectionHandler.getConnection(), sql, new ScalarHandler<T>(1), param);
			}else{
				count = runner.query(ConnectionHandler.getConnection(), sql, new ScalarHandler<T>(1));
			}
		} catch (SQLException e) {
			log.error(e);
		}
		return count;
	}
	
	/*=================== 单条记录增删改=====================*/
	
	public boolean save(String sql, Object... param){
		return updateHandler(sql, param);
	}
	
	public boolean update(String sql, Object... param){
		return updateHandler(sql, param);
	}
	
	public boolean delete(String sql, Object... param){
		return updateHandler(sql, param);
	}
	
	/*=================== 多条记录增删改=====================*/
	
	public boolean batchSave(String sql, Object[][] params){
		return batchHandler(sql, params);
	}
	
	public boolean batchUpdate(String sql, Object[][] params){
		return batchHandler(sql, params);
	}
	
	public boolean batchDelete(String sql, Object[][] params){
		return batchHandler(sql, params);
	}
	
	
	private boolean updateHandler(String sql, Object... param){
		int index = 0;
		try {
			if(param != null && param.length > 0){
				index = runner.update(ConnectionHandler.getConnection(), sql, param);
			}else{
				index = runner.update(ConnectionHandler.getConnection(), sql);
			}
		} catch (SQLException e) {
			log.error(e);
		}
		return index > 0 ? true : false;
	}
	
	private boolean batchHandler(String sql, Object[][] params){
		try {
			int[] index = runner.batch(ConnectionHandler.getConnection(), sql, params);
			return index.length == params.length;	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
