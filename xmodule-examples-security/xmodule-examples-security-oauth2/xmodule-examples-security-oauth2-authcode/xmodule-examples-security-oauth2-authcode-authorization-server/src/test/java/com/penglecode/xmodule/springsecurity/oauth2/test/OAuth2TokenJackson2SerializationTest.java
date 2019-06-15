package com.penglecode.xmodule.springsecurity.oauth2.test;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.penglecode.xmodule.common.security.oauth2.OAuth2TokenJackson2Module;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.springsecurity.oauth2.boot.OAuth2AuthorizationServerApplication;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.NONE, classes={OAuth2AuthorizationServerApplication.class})
public class OAuth2TokenJackson2SerializationTest {

	private static final ObjectMapper objectMapper = JsonUtils.createDefaultObjectMapper();
	
	static {
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.registerModule(new OAuth2TokenJackson2Module());
		/*objectMapper.disable(MapperFeature.AUTO_DETECT_GETTERS);
		objectMapper.disable(MapperFeature.AUTO_DETECT_IS_GETTERS);
		objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);*/
		//OBJECT_MAPPER.setVisibility(PropertyAccessor.SETTER, Visibility.NONE);
		//OBJECT_MAPPER.setVisibility(PropertyAccessor.GETTER, Visibility.NONE);
	}
	
	@Autowired
	private TokenStore tokenStore;
	
	@Test
	public void testAccessToken() {
		String token = "dc103530-d4f8-4dae-a26c-61d58566d895";
		OAuth2AccessToken accessToken1 = tokenStore.readAccessToken(token);
		String json1 = JsonUtils.object2Json(objectMapper, accessToken1);
		System.out.println(json1);
		OAuth2AccessToken accessToken2 = JsonUtils.json2Object(objectMapper, json1, OAuth2AccessToken.class);
		String json2 = JsonUtils.object2Json(objectMapper, accessToken2);
		System.out.println(json2);
		System.out.println(json1.equals(json2));
	}
	
	@Test
	public void testRefreshToken() {
		String token = "562e4b8f-03f5-4360-90f6-86f257e2d196";
		OAuth2RefreshToken refreshToken1 = tokenStore.readRefreshToken(token);
		String json1 = JsonUtils.object2Json(objectMapper, refreshToken1);
		System.out.println(json1);
		OAuth2RefreshToken accessToken2 = JsonUtils.json2Object(objectMapper, json1, OAuth2RefreshToken.class);
		String json2 = JsonUtils.object2Json(objectMapper, accessToken2);
		System.out.println(json2);
		System.out.println(json1.equals(json2));
	}
	
	@Test
	public void testAuthentication() {
		String token = "dc103530-d4f8-4dae-a26c-61d58566d895";
		OAuth2Authentication authentication1 = tokenStore.readAuthentication(token);
		String json1 = JsonUtils.object2Json(objectMapper, authentication1);
		System.out.println(json1);
		OAuth2Authentication authentication2 = JsonUtils.json2Object(objectMapper, json1, OAuth2Authentication.class);
		String json2 = JsonUtils.object2Json(objectMapper, authentication2);
		System.out.println(json2);
		System.out.println(json1.equals(json2));
	}
	
	@Test
	public void testPerformance() {
		String token = "dc103530-d4f8-4dae-a26c-61d58566d895";
		OAuth2Authentication authentication = tokenStore.readAuthentication(token);
		LocalDateTime startTime = LocalDateTime.now();
		int count = 1000000;
		String json = null;
		for(int i = 0; i < count; i++) {
			json = JsonUtils.object2Json(objectMapper, authentication);
			authentication = JsonUtils.json2Object(objectMapper, json, OAuth2Authentication.class);
		}
		LocalDateTime endTime = LocalDateTime.now();
		System.out.println(">>> total cost : " + Duration.between(startTime, endTime).toMillis());
	}
	
}
