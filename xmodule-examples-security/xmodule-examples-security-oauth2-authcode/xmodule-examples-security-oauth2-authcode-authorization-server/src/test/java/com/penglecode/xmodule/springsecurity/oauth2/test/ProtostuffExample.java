package com.penglecode.xmodule.springsecurity.oauth2.test;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;

import com.penglecode.xmodule.common.serializer.protostuff.ProtostuffSerializer;


public class ProtostuffExample {

	private static ProtostuffSerializer serializer = ProtostuffSerializer.INSTANCE;
	
	public static void test1() {
		Map<String,String> requestParameters = new HashMap<String,String>();
		requestParameters.put("code", "H89Qg2");
		requestParameters.put("grant_type", "authorization_code");
		requestParameters.put("scope", "app");
		requestParameters.put("response_type", "code");
		requestParameters.put("redirect_uri", "http://www.baidu.com");
		requestParameters.put("client_secret", "d0763edaa9d9bd2a9516280e9044d885");
		requestParameters.put("client_id", "monkey");
		String clientId = "monkey";
		Set<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
		boolean approved = true;
		Set<String> scope = Collections.singleton("app");
		Set<String> resourceIds = Collections.singleton("123");
		String redirectUri = "http://www.baidu.com";
		Set<String> responseTypes = Collections.singleton("code");
		Map<String,Serializable> extensionProperties = Collections.singletonMap("foo", "bar");
		OAuth2Request request1 = new OAuth2Request(requestParameters, clientId, authorities, approved, scope, resourceIds, redirectUri, responseTypes, extensionProperties);
		TokenRequest refresh = new TokenRequest(requestParameters, clientId, scope, "code");
		request1 = request1.refresh(refresh);
		byte[] bytes = serializer.serialize(request1);
		System.out.println(bytes);
		OAuth2Request request2 = serializer.deserialize(bytes);
		System.out.println(request2);
		System.out.println(request1.equals(request2));
	}
	
	public static void test2() {
		Map<String,String> requestParameters = new HashMap<String,String>();
		requestParameters.put("code", "H89Qg2");
		requestParameters.put("grant_type", "authorization_code");
		requestParameters.put("scope", "app");
		requestParameters.put("response_type", "code");
		requestParameters.put("redirect_uri", "http://www.baidu.com");
		requestParameters.put("client_secret", "d0763edaa9d9bd2a9516280e9044d885");
		requestParameters.put("client_id", "monkey");
		String clientId = "monkey";
		Set<String> scope = Collections.singleton("app");
		TokenRequest refresh1 = new TokenRequest(requestParameters, clientId, scope, "code");
		byte[] bytes = serializer.serialize(refresh1);
		System.out.println(bytes);
		TokenRequest refresh2 = serializer.deserialize(bytes);
		System.out.println(refresh2);
		System.out.println(refresh1.equals(refresh2));
	}
	
	public static void main(String[] args) {
		//test1();
		test2();
	}

}
