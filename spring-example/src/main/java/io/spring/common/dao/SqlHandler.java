package io.spring.common.dao;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;

/**
 * sql适配转换
 * @author Dylan Tao
 * @date 2015-4-29
 * Copyright 2015 javaosc.com Team. All Rights Reserved.
 */
public class SqlHandler {
	
	private static final String SELECT = "select";
	private static final String COUNT = "count(*)";
	private static final String FROM = "from";
	private static final String LIMIT = "limit";
	
	private static final String ORDER_FILTER_PATTERN = "order\\s*by[\\w|\\W|\\s|\\S]*";
	
	protected static String createCount(String sql){
		int fromIndex = sql.indexOf(FROM);
		Assert.isTrue(fromIndex != -1, "sql:" + sql + "must has a keyword 'from'");
		StringBuffer sb = new StringBuffer().append(SELECT).append(" ").append(COUNT).append(" ");
		sql = SqlHandler.removeOrderBy(sql); //移除order by
		sb.append(sql.substring(fromIndex));//拼接统计
		return sb.toString();
	}
	
	protected static String removeOrderBy(String sql) {
		Pattern p = Pattern.compile(ORDER_FILTER_PATTERN, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	protected static String createLimit(String sql,long startIndex,long selectLength){
		StringBuffer sb = new StringBuffer(sql);
		sb.append(" ").append(LIMIT).append(" ");
		sb.append(startIndex).append(",").append(selectLength);
		return sb.toString();
	}
	
	public static String createIn(String sql,int count){
		StringBuffer buffer = new StringBuffer(sql);
		for(int i=0;i<count;i++){
			if(i==0){
				buffer.append(" in(?");
			}else{
				buffer.append(",?");
			}
		}
		return buffer.append(")").toString();
	}
	
}
