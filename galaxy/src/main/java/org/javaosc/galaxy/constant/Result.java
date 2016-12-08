package org.javaosc.galaxy.constant;

import java.io.Serializable;

import org.javaosc.galaxy.assist.ServiceException;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09 Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class Result<T> implements Serializable {
	
	private static final long serialVersionUID = -2586624936313267449L;
	
	private long code;
	private String message;
	private T result;

	public Result(Exception e) {
	   ServiceException se = (ServiceException)e;
	   this.code = se.getErrorCode();
	   this.message = se.getMessage();
	}

	public Result(long code, String message, T result) {
	   this.code = code;
	   this.message = message;
	   this.result = result;
	}

	public Result(long code, T result) {
	   this.code = code;
	   this.message = "";
	   this.result = result;
	}

	public Result(long code, String message) {
	   this.code = code;
	   this.message = message;
	}

	public Result(long code) {
	   this.code = code;
	   this.message = "";
	}

	public long getCode() {
	   return this.code;
	}
	
	public void setCode(long code) {
	   this.code = code;
	}
	
	public String getMessage() {
	   return this.message;
	}

	public void setMessage(String message) {
	   this.message = message;
	}
	
	public T getResult() {
	   return this.result;
	}
	
	public void setResult(T result) {
	   this.result = result;
	}
	
}