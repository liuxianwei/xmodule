package com.penglecode.xmodule.common.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import com.penglecode.xmodule.common.exception.ApplicationRuntimeException;

/**
 * URI/URL工具类
 * 
 * @author 	pengpeng
 * @date	2018年11月16日 上午9:52:48
 */
public class URIUtils {

	public static final List<String> HTTP_PROTOCOLS = Arrays.asList("http", "https");
	
	/**
	 * 判断字符串是否是URL
	 * @param url
	 * @return
	 */
	public static boolean isURL(String url) {
		try {
			new URL(url);
			return true;
		} catch (MalformedURLException e) {
			return false;
		}
	}
	
	/**
	 * 判断字符串是否是HTTP URL
	 * @param url
	 * @return
	 */
	public static boolean isHttpURL(String url) {
		try {
			URL urlObj = new URL(url);
			return HTTP_PROTOCOLS.contains(urlObj.getProtocol().toLowerCase());
		} catch (MalformedURLException e) {
			return false;
		}
	}
	
	/**
	 * 创建URL
	 * @param url
	 * @return
	 */
	public static URL createURL(String url) {
		try {
			return new URL(url);
		} catch (MalformedURLException e) {
			throw new ApplicationRuntimeException(e);
		}
	}
	
	/**
	 * 判断字符串是否是URI
	 * @param url
	 * @return
	 */
	public static boolean isURI(String uri) {
		try {
			new URI(uri);
			return true;
		} catch (URISyntaxException e) {
			return false;
		}
	}
	
	/**
	 * 创建URI
	 * @param uri
	 * @return
	 * @throws URISyntaxException 
	 */
	public static URI createURI(String uri) {
		try {
			return new URI(uri);
		} catch (URISyntaxException e) {
			throw new ApplicationRuntimeException(e);
		}
	}
	
}
