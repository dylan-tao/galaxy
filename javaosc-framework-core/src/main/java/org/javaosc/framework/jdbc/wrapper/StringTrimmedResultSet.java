
package org.javaosc.framework.jdbc.wrapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;

import org.javaosc.framework.jdbc.core.ProxyFactory;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class StringTrimmedResultSet implements InvocationHandler {

    
    private static final ProxyFactory factory = ProxyFactory.instance();

    
    public static ResultSet wrap(ResultSet rs) {
        return factory.createResultSet(new StringTrimmedResultSet(rs));
    }

    
    private final ResultSet rs;

    
    public StringTrimmedResultSet(ResultSet rs) {
        super();
        this.rs = rs;
    }

    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable {

        Object result = method.invoke(this.rs, args);

        if ((method.getName().equals("getObject")
            || method.getName().equals("getString"))
                && result instanceof String) {
            result = ((String) result).trim();
        }

        return result;
    }

}
