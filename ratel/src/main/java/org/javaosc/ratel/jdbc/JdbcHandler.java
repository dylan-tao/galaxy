package org.javaosc.ratel.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.javaosc.ratel.constant.Constant;
import org.javaosc.ratel.constant.Page;
import org.javaosc.ratel.jdbc.core.JdbcTemplate;
import org.javaosc.ratel.jdbc.handler.BeanHandler;
import org.javaosc.ratel.jdbc.handler.BeanListHandler;
import org.javaosc.ratel.jdbc.handler.ColumnListHandler;
import org.javaosc.ratel.jdbc.handler.MapHandler;
import org.javaosc.ratel.jdbc.handler.MapListHandler;
import org.javaosc.ratel.jdbc.handler.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class JdbcHandler{
	
	private static final Logger log = LoggerFactory.getLogger(JdbcHandler.class);
	
	private static JdbcTemplate jdbcTemplate;
	
	/*=================== 查询单列操作 =====================*/
	
	static{
		jdbcTemplate = new JdbcTemplate();
	}

	public <T> T getForColumn(String sql,String columnName, Class<T> cls, Object... param){
		T obj = null;
		try {
			if(param != null && param.length > 0){
				obj = jdbcTemplate.query(ConnectionHandler.getConnection(), sql, new ScalarHandler<T>(columnName), param);
			}else{
				obj = jdbcTemplate.query(ConnectionHandler.getConnection(), sql, new ScalarHandler<T>(columnName));
			}
		} catch (SQLException e) {
			log.error(Constant.RATEL_EXCEPTION, e);
		}
		return obj;
	}
	
	public <T> List<T> getForColumnList(String sql,String columnName,Class<T> cls, Object... param){
		List<T> list = null;
		try {
			if(param != null && param.length > 0){
				list = jdbcTemplate.query(ConnectionHandler.getConnection(), sql, new ColumnListHandler<T>(columnName), param);
			}else{
				list = jdbcTemplate.query(ConnectionHandler.getConnection(), sql, new ColumnListHandler<T>(columnName));
			}
		} catch (SQLException e) {
			log.error(Constant.RATEL_EXCEPTION, e);
		}
		return list;
	}
	
	/*=================== 查询多列操作 =====================*/
	
	public Map<String, Object> getForMap(String sql, Object... param){
		 Map<String, Object> map = null;
		 try {
			if(param != null && param.length > 0){
				map = jdbcTemplate.query(ConnectionHandler.getConnection(), sql, new MapHandler(), param);
			}else{
				map = jdbcTemplate.query(ConnectionHandler.getConnection(), sql, new MapHandler());
			}
		 } catch (SQLException e) {
			 log.error(Constant.RATEL_EXCEPTION, e);
		 }
		 return map;	
	}
	
	public List<Map<String, Object>> getForMapList(String sql, Object... param){
		 List<Map<String, Object>> list = null;
		 try {
			if(param != null && param.length > 0){
				list = jdbcTemplate.query(ConnectionHandler.getConnection(), sql, new MapListHandler(), param);
			}else{
				list = jdbcTemplate.query(ConnectionHandler.getConnection(), sql, new MapListHandler());
			}
		 } catch (SQLException e) {
			 log.error(Constant.RATEL_EXCEPTION, e);
		 }
		 return list;	
	}
	
	/*=================== 查询单表或多表操作 =====================*/
	
	public <T> T getForObject(String sql, Class<T> cls, Object... param){
		T obj = null;
		try {
			if(param != null && param.length > 0){
				obj = jdbcTemplate.query(ConnectionHandler.getConnection(), sql, new BeanHandler<T>(cls), param);
			}else{
				obj = jdbcTemplate.query(ConnectionHandler.getConnection(), sql, new BeanHandler<T>(cls));
			}
		} catch (SQLException e) {
			log.error(Constant.RATEL_EXCEPTION, e);
		}
		return obj;
	}
	
	public <T> List<T> getForList(String sql, Class<T> cls, Object... param){
		List<T> list = null;
		try {
			if(param != null && param.length > 0){
				list = jdbcTemplate.query(ConnectionHandler.getConnection(), sql, new BeanListHandler<T>(cls), param);
			}else{
				list = jdbcTemplate.query(ConnectionHandler.getConnection(), sql, new BeanListHandler<T>(cls));
			}
		} catch (SQLException e) {
			log.error(Constant.RATEL_EXCEPTION, e);
		}
		return list;
	}
	
	public <T> Page<T> getForPage(String sql, Page<T> page,Class<T> cls, Object... param){
		List<T> list = null;
		
		//where->group by->having-order by->limit
		if(page.isAutoCount()){
			String countSql = SqlHandler.createCount(sql);
			long count = getCount(countSql, Long.class, param);
			page.setTotalCount(count);
		}
		int pageNo = page.getPageNo();
		int pageSize = page.getPageSize();
		int startIndex =(pageNo-1) * pageSize;
		sql = SqlHandler.createLimit(sql, startIndex, pageSize); //limit
		
		list = getForList(sql, cls, param);
		page.setResult(list);
		return page;
	}
	
	
	/*=================== 扩展操作 =====================*/
	
	public <T> T getCount(String sql, Class<T> cls, Object... param){
		T count = cls.cast(0);
		try {
			if(param != null && param.length > 0){
				count = jdbcTemplate.query(ConnectionHandler.getConnection(), sql, new ScalarHandler<T>(1), param);
			}else{
				count = jdbcTemplate.query(ConnectionHandler.getConnection(), sql, new ScalarHandler<T>(1));
			}
		} catch (SQLException e) {
			log.error(Constant.RATEL_EXCEPTION, e);
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
				index = jdbcTemplate.update(ConnectionHandler.getConnection(), sql, param);
			}else{
				index = jdbcTemplate.update(ConnectionHandler.getConnection(), sql);
			}
		} catch (SQLException e) {
			log.error(Constant.RATEL_EXCEPTION, e);
		}
		return index > 0 ? true : false;
	}
	
	private boolean batchHandler(String sql, Object[][] params){
		try {
			int[] index = jdbcTemplate.batch(ConnectionHandler.getConnection(), sql, params);
			return index.length == params.length;	
		} catch (SQLException e) {
			log.error(Constant.RATEL_EXCEPTION, e);
		}
		return false;
	}
}
