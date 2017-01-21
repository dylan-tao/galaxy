package org.javaosc.galaxy.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.javaosc.galaxy.constant.Constant;
import org.javaosc.galaxy.constant.Constant.CodeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description
 * @author Dylan Tao
 * @date 2014-10-26
 * Copyright 2014 javaosc.com Team. All Rights Reserved.
 */

public class CodeUtil {
	
	private static final Logger log = LoggerFactory.getLogger(CodeUtil.class);
	
	public static String encodeURL(String str, CodeType encode) {
		try {
			str = URLEncoder.encode(str, encode.getValue());
			log.debug("====== URL Encode Content: {}" ,str);
		} catch (UnsupportedEncodingException e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}
		return str;
	}
	
	public static String decodeURL(String str, CodeType encode) {
		try {
			str = URLDecoder.decode(str, encode.getValue());
			log.debug("====== URL Decode Content: {}" ,str);
		} catch (UnsupportedEncodingException e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}
		return str;
	}
	
	public static String encodeBase64(String str, CodeType encode) {
		try {
			str = new String(Base64.encode(str.getBytes(encode.getValue())));
			log.debug("====== BASE64 Encode Content: {}" ,str);
		} catch (UnsupportedEncodingException e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}
		return str;
	}

	public static String decodeBase64(String str, CodeType decode) {
		try {
			str = new String(Base64.decode(str.toCharArray()),
					decode.getValue());
			log.debug("====== BASE64 Decode Content: {}", str);
		} catch (Exception e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}
		return str;
	}

	public static String encodeMD5(String str, CodeType encode) {
		return encodeMD5(str, true, encode);
	}

	public static String encodeMD5(String str, boolean default32, CodeType encode) {
		try {
			byte[] strByte = str.getBytes(encode.getValue());
			MessageDigest md5 = MessageDigest.getInstance(CoverType.MD5.getValue());
			md5.update(strByte);
			byte[] tmp = md5.digest();
			StringBuilder sb = new StringBuilder();

			for (byte b : tmp) {
				int i = b;
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					sb.append("0");
				}
				sb.append(Integer.toHexString(i));
			}
			if (default32)
				str = sb.toString();
			else {
				str = sb.toString().substring(8, 24);
			}
			log.debug("====== MD5 Encode Content: {}",str);
		} catch (UnsupportedEncodingException e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		} catch (NoSuchAlgorithmException e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}
		return str;
	}

	public static String encodeDES(String str, String key, CodeType encode) {
		try {
			byte[] bt = encrypt(str.getBytes(encode.getValue()),
					key.getBytes(encode.getValue()));
			str = new String(Base64.encode(bt));
			log.debug("====== DES Encode Content: {}",str);
			return str;
		} catch (Exception e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}
		return null;
	}

	public static String decodeDES(String data, String key, CodeType encode) {
		if (data == null)
			return null;
		try {
			byte[] buf = Base64.decode(data.toCharArray());
			byte[] bt = decrypt(buf, key.getBytes(encode.getValue()));
			data = new String(bt);
			log.debug("====== DES Decode Content: {}",data);
			return data;
		} catch (Exception e) {
			log.error(Constant.GALAXY_EXCEPTION, e);
		}
		return null;
	}

	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(CoverType.DES.getValue());
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(CoverType.DES.getValue());
		cipher.init(1, securekey, sr);
		return cipher.doFinal(data);
	}

	private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(CoverType.DES.getValue());
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(CoverType.DES.getValue());
		cipher.init(2, securekey, sr);
		return cipher.doFinal(data);
	}

	private static class Base64 {
		
		private static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();
		private static byte[] bytes = new byte[256];
		
		@SuppressWarnings("unused")
		public static char[] encode(byte[] data) {
			char[] out = new char[(data.length + 2) / 3 * 4];
			int i = 0;
			for (int index = 0; i < data.length; index += 4) {
				
				boolean quad = false;
				boolean trip = false;
				int val = 0xFF & data[i];
				val <<= 8;
				if (i + 1 < data.length) {
					val |= 0xFF & data[(i + 1)];
					trip = true;
				}
				val <<= 8;
				if (i + 2 < data.length) {
					val |= 0xFF & data[(i + 2)];
					quad = true;
				}
				out[(index + 3)] = alphabet[64];
				val >>= 6;
				out[(index + 2)] = alphabet[64];
				val >>= 6;
				out[(index + 1)] = alphabet[(val & 0x3F)];
				val >>= 6;
				out[(index + 0)] = alphabet[(val & 0x3F)];

				i += 3;
			}

			return out;
		}

		public static byte[] decode(char[] data) {
			int len = (data.length + 3) / 4 * 3;
			if ((data.length > 0) && (data[(data.length - 1)] == '='))
				len--;
			if ((data.length > 1) && (data[(data.length - 2)] == '='))
				len--;
			byte[] out = new byte[len];
			int shift = 0;
			int accum = 0;
			int index = 0;
			for (int ix = 0; ix < data.length; ix++) {
				int value = bytes[(data[ix] & 0xFF)];
				if (value >= 0) {
					accum <<= 6;
					shift += 6;
					accum |= value;
					if (shift >= 8) {
						shift -= 8;
						out[(index++)] = (byte) (accum >> shift & 0xFF);
					}
				}
			}
			if (index != out.length)
				throw new Error("miscalculated data length!");
			return out;
		}

		static {
			for (int i = 0; i < 256; i++)
				bytes[i] = -1;
			for (int i = 65; i <= 90; i++)
				bytes[i] = (byte) (i - 65);
			for (int i = 97; i <= 122; i++)
				bytes[i] = (byte) (26 + i - 97);
			for (int i = 48; i <= 57; i++)
				bytes[i] = (byte) (52 + i - 48);
			bytes[43] = 62;
			bytes[47] = 63;
		}
	}

	private static enum CoverType {
		MD5("MD5"), DES("DES"), AES("AES"), Blowfish("Blowfish");

		private final String value;

		private CoverType(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
}
