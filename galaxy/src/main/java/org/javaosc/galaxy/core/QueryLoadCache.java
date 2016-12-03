
package org.javaosc.galaxy.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * 
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class QueryLoadCache {

    
    private static final QueryLoadCache instance = new QueryLoadCache();

    
    private static final Pattern dotXml = Pattern.compile(".+\\.[xX][mM][lL]");

    
    public static QueryLoadCache instance() {
        return instance;
    }

    
    private final Map<String, Map<String, String>> queries = new HashMap<String, Map<String, String>>();

    
    protected QueryLoadCache() {
        super();
    }

    
    public synchronized Map<String, String> load(String path) throws IOException {

        Map<String, String> queryMap = this.queries.get(path);

        if (queryMap == null) {
            queryMap = this.loadQueries(path);
            this.queries.put(path, queryMap);
        }

        return queryMap;
    }

    
    protected Map<String, String> loadQueries(String path) throws IOException {
        // Findbugs flags getClass().getResource as a bad practice; maybe we should change the API?
        InputStream in = getClass().getResourceAsStream(path);

        if (in == null) {
            throw new IllegalArgumentException(path + " not found.");
        }

        Properties props = new Properties();
        try {
            if (dotXml.matcher(path).matches()) {
                props.loadFromXML(in);
            } else {
                props.load(in);
            }
        } finally {
            in.close();
        }

        // Copy to HashMap for better performance

        @SuppressWarnings({ "rawtypes", "unchecked" }) // load() always creates <String,String> entries
        HashMap<String, String> hashMap = new HashMap(props);
        return hashMap;
    }

    
    public synchronized void unload(String path) {
        this.queries.remove(path);
    }

}
