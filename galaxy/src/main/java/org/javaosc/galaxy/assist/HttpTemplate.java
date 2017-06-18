
package org.javaosc.galaxy.web.assist;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.javaosc.galaxy.constant.Constant;
import org.javaosc.galaxy.constant.Constant.CodeType;
import org.javaosc.galaxy.constant.Constant.HttpType;
import org.javaosc.galaxy.util.GalaxyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @description
 * @author Dylan Tao
 * @date 2014-09-09
 * Copyright 2014 Javaosc Team. All Rights Reserved.
 */
public class HttpTemplate {
	
	private static final Logger log = LoggerFactory.getLogger(HttpTemplate.class);
	
	public static int buffer_size = 4096;
	public static int conn_timeout = 6000;
	public static int read_timeout = 12000;
	public static String charset = CodeType.UTF8.getValue();
	
	
	static final SSLSocketFactory sslSocketFactory = initSSLSocketFactory();
	static final TrustAnyHostnameVerifier trustAnyHostnameVerifier = new HttpTemplate().new TrustAnyHostnameVerifier();

	public static String get(String url) {
		return get(url, null, null);
	}
	
	public static String get(String url, Map<String, String> params) {
		return get(url, params, null);
	}
	
	public static String get(String url, Map<String, String> params, Map<String, String> headers) {
		HttpURLConnection conn = null;
		try {
			conn = buildConnection(buildParam(url, params), HttpType.GET.name(), headers);
			conn.connect();
			return buildResult(conn);
		}
		catch (Exception e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}
		finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return null;
	}
	
	public static String post(String url, Map<String, String> params) {
		return post(url, params, null, null);
	}
	
	public static String post(String url, String data) {
		return post(url, null, data, null);
	}
	
	public static String post(String url, Map<String, String> params, Map<String, String> headers) {
		return post(url, params, null, headers);
	}
	
	public static String post(String url, String data, Map<String, String> headers) {
		return post(url, null, data, headers);
	}
	
	private static String post(String url, Map<String, String> params, String data, Map<String, String> headers) {
		HttpURLConnection conn = null;
		try {
			conn = buildConnection(buildParam(url, params), HttpType.POST.name(), headers);
			conn.connect();
			
			if(!GalaxyUtil.isEmpty(data)){
				DataOutputStream out = new DataOutputStream(conn.getOutputStream());
				out.write(data.getBytes(charset));
				out.flush();
				out.close();
			}
			
			return buildResult(conn);
		}
		catch (Exception e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}
		finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return null;
	}
	
	private static HttpURLConnection buildConnection(String url, String method, Map<String, String> headers) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
		URL _url = new URL(url);
		HttpURLConnection conn = (HttpURLConnection)_url.openConnection();
		if (conn instanceof HttpsURLConnection) {
			((HttpsURLConnection)conn).setSSLSocketFactory(sslSocketFactory);
			((HttpsURLConnection)conn).setHostnameVerifier(trustAnyHostnameVerifier);
		}
		
		conn.setRequestMethod(method);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		
		conn.setConnectTimeout(conn_timeout);
		conn.setReadTimeout(read_timeout);
		
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
		
		if (headers != null && !headers.isEmpty()){
			for (Entry<String, String> entry : headers.entrySet()){
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		return conn;
	}
	
	private static String buildResult(HttpURLConnection conn) {
		StringBuilder sb = new StringBuilder();
		InputStream inputStream = null;
		try {
			int code = conn.getResponseCode();
	        String message = conn.getResponseMessage();
	        
	        inputStream = conn.getErrorStream();
            if (inputStream == null) {
            	inputStream = conn.getInputStream();
            }
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
			char[] cbuf = new char[buffer_size];
            while (true) {
                int len = reader.read(cbuf);
                if (len < 0) {
                    break;
                }
                sb.append(cbuf, 0, len);
            }
            if(code==200){
            	return sb.toString();
            }else{
            	log.error("code: {}, message: {} ,result: {}", code, message, sb.toString());
            }
		}catch (Exception e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error(Constant.GALAXY_EXCEPTION, e);
				}
			}
		}
		return null;
	}
	
	private static String buildParam(String url, Map<String, String> params) {
		if (GalaxyUtil.isEmpty(params)){
			return url;
		}
		StringBuilder sb = new StringBuilder(url);
		boolean isFirst;
		if (url.indexOf(Constant.QM) == -1) {
			isFirst = true;
			sb.append(Constant.QM);
		}else {
			isFirst = false;
		}
		
		for (Entry<String, String> entry : params.entrySet()) {
			if (isFirst){
				isFirst = false;
			}else{
				sb.append(Constant.AM);
			} 
			String key = entry.getKey();
			String value = entry.getValue();
			if (!GalaxyUtil.isEmpty(value)){
				try {
					value = URLEncoder.encode(value, charset);
				} catch (UnsupportedEncodingException e) {
					log.error(Constant.GALAXY_EXCEPTION, e);
				}
			}
			sb.append(key).append(Constant.EM).append(value);
		}
		return sb.toString();
	}
	
	private static SSLSocketFactory initSSLSocketFactory() {
		try {
			TrustManager[] tm = {new HttpTemplate().new TrustAnyTrustManager() };
			SSLContext sslContext = SSLContext.getInstance("TLS");	// ("TLS", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			return sslContext.getSocketFactory();
		}
		catch (Exception e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}
		return null;
	}
	
	private class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
	
	private class TrustAnyTrustManager implements X509TrustManager {
		public X509Certificate[] getAcceptedIssuers(){
			return null;  
		}
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
	}
	
}






