
package org.javaosc.galaxy.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class ResultSetIterator implements Iterator<Object[]> {

    
    private final ResultSet rs;

    
    private final RowConvert convert;

    
    public ResultSetIterator(ResultSet rs) {
        this(rs, new RowConvertHandler());
    }

    
    public ResultSetIterator(ResultSet rs, RowConvert convert) {
        this.rs = rs;
        this.convert = convert;
    }

    
    @Override
    public boolean hasNext() {
        try {
            return !rs.isLast();
        } catch (SQLException e) {
            rethrow(e);
            return false;
        }
    }

    
    @Override
    public Object[] next() {
        try {
            rs.next();
            return this.convert.toArray(rs);
        } catch (SQLException e) {
            rethrow(e);
            return null;
        }
    }

    
    @Override
    public void remove() {
        try {
            this.rs.deleteRow();
        } catch (SQLException e) {
            rethrow(e);
        }
    }

    
    protected void rethrow(SQLException e) {
        throw new RuntimeException(e.getMessage());
    }

    
    public static Iterable<Object[]> iterable(final ResultSet rs) {
        return new Iterable<Object[]>() {

            @Override
            public Iterator<Object[]> iterator() {
                return new ResultSetIterator(rs);
            }

        };
    }

}
