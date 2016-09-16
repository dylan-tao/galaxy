
package org.javaosc.ratel.jdbc.core;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.javaosc.ratel.assist.PropertyConvert;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class RowConvertHandler implements RowConvert {

    
    @Override
    public Object[] toArray(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();
        Object[] result = new Object[cols];

        for (int i = 0; i < cols; i++) {
            result[i] = rs.getObject(i + 1);
        }

        return result;
    }

    
    @Override
    public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {
        return PropertyConvert.convertResultSetToEntity(rs, type);
    }

    @Override
    public <T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException {
    	List<T> results = new ArrayList<T>();
        if (!rs.next()) {
            return results;
        }
        do {
        	T t = PropertyConvert.convertResultSetToEntity(rs, type);
            results.add(t);
        } while (rs.next());
        return results;
    }
    
    @Override
    public Map<String, Object> toMap(ResultSet rs) throws SQLException {
        Map<String, Object> result = new CaseInsensitiveHashMap();
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();

        for (int i = 1; i <= cols; i++) {
            String columnName = rsmd.getColumnLabel(i);
            if (null == columnName || 0 == columnName.length()) {
              columnName = rsmd.getColumnName(i);
            }
            result.put(columnName, rs.getObject(i));
        }

        return result;
    }

    
    private static class CaseInsensitiveHashMap extends LinkedHashMap<String, Object> {
        
        private final Map<String, String> lowerCaseMap = new HashMap<String, String>();

        
        private static final long serialVersionUID = -2848100435296897392L;

        
        @Override
        public boolean containsKey(Object key) {
            Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
            return super.containsKey(realKey);
            // Possible optimisation here:
            // Since the lowerCaseMap contains a mapping for all the keys,
            // we could just do this:
            // return lowerCaseMap.containsKey(key.toString().toLowerCase());
        }

        
        @Override
        public Object get(Object key) {
            Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
            return super.get(realKey);
        }

        
        @Override
        public Object put(String key, Object value) {
            
            Object oldKey = lowerCaseMap.put(key.toLowerCase(Locale.ENGLISH), key);
            Object oldValue = super.remove(oldKey);
            super.put(key, value);
            return oldValue;
        }

        
        @Override
        public void putAll(Map<? extends String, ?> m) {
            for (Map.Entry<? extends String, ?> entry : m.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                this.put(key, value);
            }
        }

        
        @Override
        public Object remove(Object key) {
            Object realKey = lowerCaseMap.remove(key.toString().toLowerCase(Locale.ENGLISH));
            return super.remove(realKey);
        }
    }

}
