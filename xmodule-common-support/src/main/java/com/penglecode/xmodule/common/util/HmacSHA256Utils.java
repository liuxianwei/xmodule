package com.penglecode.xmodule.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

/**
 * HMAC（Hash-based Message Authentication Code）：基于散列的消息认证码，使用一个密钥和一个消息作为输入，生成它们的消息摘要。
 * 注意该密钥只有客户端和服务端知道，其他第三方是不知道的。访问时使用该消息摘要进行传播，服务端然后对该消息摘要进行验证
 * 
 * @author 	pengpeng
 * @date	2018年1月4日 下午12:57:19
 */
public class HmacSHA256Utils {

	public static final String DEFAULT_CHARSET = "UTF-8";
	
	/**
	 * 生成消息摘要
	 * @param key
	 * @param content
	 * @return
	 */
	public static String digest(String key, String content) {
		try {
            Mac mac = Mac.getInstance("HmacSHA256");
            byte[] secretByte = key.getBytes(DEFAULT_CHARSET);
            byte[] dataBytes = content.getBytes(DEFAULT_CHARSET);

            SecretKey secret = new SecretKeySpec(secretByte, "HMACSHA256");
            mac.init(secret);

            byte[] doFinal = mac.doFinal(dataBytes);
            byte[] hexB = new Hex().encode(doFinal);
            return new String(hexB, DEFAULT_CHARSET);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}
	
	/**
	 * 生成消息摘要
	 * @param key
	 * @param params
	 * @return
	 */
	public static String digest(String key, Map<String,String> params) {
		if(!CollectionUtils.isEmpty(params)) {
			//以下功能是将Map类型的参数转换成URL问号后面的GET参数形式，例如：appKey=aaaa&token=bbbb&timestamp=cccc&sign=dddd&...
			List<String> paramNames = new ArrayList<String>(params.keySet());
			Collections.sort(paramNames);
			StringBuilder sb = new StringBuilder();
			int index = 0, size = params.size();
			for(String paramName : paramNames) {
				String paramValue = StringUtils.defaultIfEmpty(params.get(paramName), ""); //如果为null或者空白串一律转为""
				sb.append(paramName + "=" + paramValue.trim());
				if(index++ < size) {
					sb.append("&");
				}
			}
			return digest(key, sb.toString());
		}
		return null;
	}
	
}
