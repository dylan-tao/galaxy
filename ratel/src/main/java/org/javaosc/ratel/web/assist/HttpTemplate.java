
package org.javaosc.ratel.web.assist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import javax.servlet.http.HttpServletRequest;

import org.javaosc.ratel.constant.Constant;
import org.javaosc.ratel.constant.Constant.CodeType;
import org.javaosc.ratel.constant.Constant.HttpType;
import org.javaosc.ratel.util.StringUtil;
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
	
	private HttpTemplate() {}
	
	/**
	 * https 域名校验
	 */
	private class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
	
	/**
	 * https 证书管理
	 */
	private class TrustAnyTrustManager implements X509TrustManager {
		public X509Certificate[] getAcceptedIssuers(){
			return null;  
		}
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
	}
	
	private static String CHARSET = CodeType.UTF8.name();
	private static final SSLSocketFactory sslSocketFactory = initSSLSocketFactory();
	private static final TrustAnyHostnameVerifier trustAnyHostnameVerifier = new HttpTemplate().new TrustAnyHostnameVerifier();
	
	private static SSLSocketFactory initSSLSocketFactory() {
		try {
			TrustManager[] tm = {new HttpTemplate().new TrustAnyTrustManager() };
			SSLContext sslContext = SSLContext.getInstance("TLS");	// ("TLS", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			return sslContext.getSocketFactory();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void setCharSet(String charSet) {
		if (StringUtil.isBlank(charSet)) {
			throw new IllegalArgumentException("charSet can not be blank.");
		}
		HttpTemplate.CHARSET = charSet;
	}
	
	public static String get(String url) {
		return get(url, null, null);
	}
	
	public static String get(String url, Map<String, String> params) {
		return get(url, params, null);
	}
	
	public static String get(String url, Map<String, String> params, Map<String, String> headers) {
		HttpURLConnection conn = null;
		try {
			conn = buildConnection(appendParam(url, params), HttpType.GET.name(), headers);
			conn.connect();
			return responseDataFormat(conn);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
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
	
	public static String requestDataFormat(HttpServletRequest request) {
		BufferedReader br = null;
		try {
			StringBuilder result = new StringBuilder();
			br = request.getReader();
			for (String line=null; (line=br.readLine())!=null;) {
				result.append(line).append("\n");
			}
			return result.toString();
		}catch (IOException e) {
			throw new RuntimeException(e);
		}finally {
			if (br != null){
				try {
					br.close();
				} catch (IOException e) {
					log.error(Constant.RATEL_EXCEPTION, e);
				}
			}
		}
	}
	
	private static String post(String url, Map<String, String> queryParas, String data, Map<String, String> headers) {
		HttpURLConnection conn = null;
		try {
			conn = buildConnection(appendParam(url, queryParas), HttpType.POST.name(), headers);
			conn.connect();
			
			OutputStream out = conn.getOutputStream();
			if(data==null){
				data = "";
			}
			out.write(data.getBytes(CHARSET));
			out.flush();
			out.close();
			return responseDataFormat(conn);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
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
		
		conn.setConnectTimeout(19000);
		conn.setReadTimeout(19000);
		
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
		
		if (headers != null && !headers.isEmpty()){
			for (Entry<String, String> entry : headers.entrySet()){
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		return conn;
	}
	
	private static String responseDataFormat(HttpURLConnection conn) {
		StringBuilder sb = new StringBuilder();
		InputStream inputStream = null;
		try {
			inputStream = conn.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, CHARSET));
			String line = null;
			while ((line = reader.readLine()) != null){
				sb.append(line).append("\n");
			}
			return sb.toString();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.error(Constant.RATEL_EXCEPTION, e);
				}
			}
		}
	}
	
	private static String appendParam(String url, Map<String, String> params) {
		if (params == null || params.isEmpty()){
			return url;
		}
		StringBuilder sb = new StringBuilder(url);
		boolean isFirst;
		if (url.indexOf("?") == -1) {
			isFirst = true;
			sb.append("?");
		}else {
			isFirst = false;
		}
		
		for (Entry<String, String> entry : params.entrySet()) {
			if (isFirst){
				isFirst = false;
			}else{
				sb.append("&");
			} 
			String key = entry.getKey();
			String value = entry.getValue();
			if (StringUtil.isNotBlank(value)){
				try {
					value = URLEncoder.encode(value, CHARSET);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}
			sb.append(key).append("=").append(value);
		}
		return sb.toString();
	}
	
}






