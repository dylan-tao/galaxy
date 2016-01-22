package org.javaosc.framework.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
public class ContextHandler {
	
	private static final Logger log = LoggerFactory.getLogger(ContextHandler.class);
	
	public static void setAttribute(String key, Object value){
		 ActionContext.getContext().getRequest().setAttribute(key, value);
	}
	
	public static void setSession(String name, Object value){
		HttpSession session = ActionContext.getContext().getRequest().getSession();
		session.setAttribute(name, value);
		log.debug("====== Set Session Over: Key=" + name + ",Value=" + value);
	}
	
	public static HttpSession getSession(){
		return ActionContext.getContext().getRequest().getSession();
	}
	
	public static void removeSession(String name){
		HttpSession session = ActionContext.getContext().getRequest().getSession();
		session.removeAttribute(name);
		log.debug("====== Remove Session Over: Key=" + name);
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
        log.debug("====== Set Cookie Over: Key=" + name + ",Value=" + value + ",MaxAge=" + maxSecond + "seconds");
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
                	log.debug("====== Get Cookie Over: Key=" + name + ",Value=" + cookie.getValue());
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
