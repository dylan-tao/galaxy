package org.javaosc.ratel.jdbc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.javaosc.ratel.constant.Constant;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class SqlHandler {
	
	static String SELECT_COUNT_FROM = "select count(*) from ";
	static String LIMIT = " limit "; 
	static String ORDER_FILTER_PATTERN = "order\\s*by[\\w|\\W|\\s|\\S]*";
	
	protected static String createCount(String sql){
		StringBuffer sb = SqlHandler.removeOrderBy(sql); //移除order by
		sb.insert(0, SELECT_COUNT_FROM).append("(").append(sql).append(")").append(" as count");
		return sb.toString();
	}
	
	protected static StringBuffer removeOrderBy(String sql) {
		Pattern p = Pattern.compile(ORDER_FILTER_PATTERN, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, Constant.EMPTY);
		}
		m.appendTail(sb);
		return sb;
	}
	
	protected static String createLimit(String sql,long startIndex,long endIndex){
		StringBuilder sb = new StringBuilder(sql);
		sb.append(LIMIT).append(startIndex).append(Constant.COMMA).append(endIndex);
		return sb.toString();
	}
	
}
