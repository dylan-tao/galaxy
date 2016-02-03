package org.javaosc.framework.web.assist;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaosc.framework.constant.Constant;
import org.javaosc.framework.constant.Constant.ContentType;
import org.javaosc.framework.web.ActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ContextHandler {
	
	private static final Logger log = LoggerFactory.getLogger(ContextHandler.class);
	
	public static void setAttribute(String key, Object value){
		 ActionContext.getContext().getRequest().setAttribute(key, value);
	}
	
	public static void setJsonAttribute(String content) {
		setTextAttribute(content,ContentType.JSON);
	}
	
	public static void setTextAttribute(String content,ContentType contentType) {
		ActionContext.getContext().getResponse().setContentType(contentType.getValue());
		try {
			PrintWriter out = ActionContext.getContext().getResponse().getWriter();
			out.write(content);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			log.debug(content);
		}
	}
	
	public static void setSession(String name, Object value){
		ActionContext.getContext().getRequest().getSession().setAttribute(name, value);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getSession(String name, Class<T> cls){
		return (T)ActionContext.getContext().getRequest().getSession().getAttribute(name);
	}
	
	public static void removeSession(String name){
		ActionContext.getContext().getRequest().getSession().removeAttribute(name);
	}
	
    public static void setCookie(String name, String value, int maxSecond) {
        setCookie(name, value, maxSecond, Constant.LINE);
    }

    public static void setCookie(String name, String value, int maxSecond, String path) {
        HttpServletResponse response = ActionContext.getContext().getResponse();
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxSecond);
        cookie.setPath(path);
        response.addCookie(cookie);
    }
	
    public static String getCookie(String name) {
        return getCookie(name, null);
    }

    public static String getCookie(String name, String defaultValue) {
        HttpServletRequest request = ActionContext.getContext().getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies!=null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return defaultValue;
    }

    public static void removeCookie(String name) {
        setCookie(name, Constant.EMPTY, 0, Constant.LINE);
    }

    public static void removeCookie(String name, String path) {
        setCookie(name, Constant.EMPTY, 0, path);
    }
    
}
