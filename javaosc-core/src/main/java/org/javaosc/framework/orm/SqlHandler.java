package org.javaosc.framework.orm;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.javaosc.framework.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class SqlHandler {
	
	private static final Logger log = LoggerFactory.getLogger(SqlHandler.class);
	
	private static final String SELECT = "select";
	private static final String COUNT = "count(*)";
	private static final String FROM = "from";
	private static final String LIMIT = "limit";
	
	private static final String ORDER_FILTER = "order\\s*by[\\w|\\W|\\s|\\S]*";
	
	protected static String createCount(String sql){
		sql = sql.toLowerCase();
		int fromIndex = sql.indexOf(FROM);
		if(fromIndex!=-1){
			log.error("sql:" + sql + "must has a keyword 'from'");
		}
		StringBuffer sb = new StringBuffer();
		sql = SqlHandler.removeOrderBy(sql); //移除order by
		String fromAfter = sql.substring(fromIndex);
		sb.append(SELECT).append(Constant.SPACE).append(COUNT);
		sb.append(Constant.SPACE).append(fromAfter);//拼接统计
		sql = sb.toString();	
		return sql;
	}
	
	protected static String removeOrderBy(String sql) {
		Pattern p = Pattern.compile(ORDER_FILTER, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, Constant.EMPTY);
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	protected static String createLimit(String sql,long startIndex,long selectLength){
		StringBuffer sb = new StringBuffer();
		sb.append(sql);
		sb.append(Constant.SPACE).append(LIMIT).append(Constant.SPACE);
		sb.append(startIndex);
		sb.append(Constant.COMMA);
		sb.append(selectLength);
		return sb.toString();
	}
	
}
