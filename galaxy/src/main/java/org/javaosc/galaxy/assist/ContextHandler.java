package org.javaosc.galaxy.web.assist;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javaosc.galaxy.constant.Constant;
import org.javaosc.galaxy.constant.Constant.ResContentType;
import org.javaosc.galaxy.util.JsonUtil;
import org.javaosc.galaxy.web.ActionContext;
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
	
	/* ========== attribute ========== */
	
	public static void putAttr(Object... param){
		int length = param!=null?param.length : 0;
		if(length==2){
			ActionContext.getContext().getRequest().setAttribute(String.valueOf(param[0]), param[1]);
		}else if(length>2 && length%2==0){
			length = length/2;
			for(int i=0;i<length;i++){
			   ActionContext.getContext().getRequest().setAttribute(String.valueOf(param[2*i]), param[2*i+1]);
			}
		}else{
			log.error(Constant.GALAXY_EXCEPTION, "the number of param must be an even number!");
		}
	}
	
	/* ========== json ========== */
	
	public static void putJson(Object... param) {
		String content = "";
		ActionContext.getContext().getResponse().setContentType(ResContentType.JSON.getValue());
		try {
			PrintWriter out = ActionContext.getContext().getResponse().getWriter();
			int length = param!=null?param.length : 0;
			if(length==1){
				content = JsonUtil.toJson(param[0]);
			}else if(length>1 && length%2==0){
				length = length/2;
				HashMap<String, Object> buildResult = new HashMap<String, Object>();
				for(int i=0;i<length;i++){
					buildResult.put(String.valueOf(param[2*i]), param[2*i+1]);
				}
				content = JsonUtil.toJson(buildResult);
			}else{
				log.error(Constant.GALAXY_EXCEPTION, "the number of param must be an even number!");
			}
			out.write(content);
			out.flush();
			out.close();
		} catch (IOException e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}finally{
			content = null;
		}
	}
	
	/* ========== session ========== */
	
	public static void setSession(String name, Object value){
		ActionContext.getContext().getRequest().getSession().setAttribute(name, value);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getSession(String name, Class<T> cls){
		return (T)ActionContext.getContext().getRequest().getSession(false).getAttribute(name);
	}
	
	public static void removeSession(String name){
		ActionContext.getContext().getRequest().getSession(false).removeAttribute(name);
	}
	
	/* ========== cookie ========== */
	
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
