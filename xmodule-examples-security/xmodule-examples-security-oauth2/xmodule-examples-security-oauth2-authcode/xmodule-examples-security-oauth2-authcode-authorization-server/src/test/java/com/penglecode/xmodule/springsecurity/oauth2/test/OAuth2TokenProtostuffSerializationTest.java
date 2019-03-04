package com.penglecode.xmodule.springsecurity.oauth2.test;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.context.junit4.SpringRunner;

import com.penglecode.xmodule.common.serializer.protostuff.ProtostuffSerializer;
import com.penglecode.xmodule.springsecurity.oauth2.boot.OAuth2AuthorizationServerApplication;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.NONE, classes={OAuth2AuthorizationServerApplication.class})
public class OAuth2TokenProtostuffSerializationTest {

	private static ProtostuffSerializer serializer = ProtostuffSerializer.INSTANCE;
	
	@Autowired
	private TokenStore tokenStore;
	
	@Test
	public void testPerformance() {
		String token = "0df6906b-efc2-406f-94ba-1d6bb27808ef";
		OAuth2Authentication authentication = tokenStore.readAuthentication(token);
		LocalDateTime startTime = LocalDateTime.now();
		int count = 1000000;
		byte[] bytes = null;
		for(int i = 0; i < count; i++) {
			bytes = serializer.serialize(authentication);
			authentication = serializer.deserialize(bytes);
		}
		LocalDateTime endTime = LocalDateTime.now();
		System.out.println(">>> total cost : " + Duration.between(startTime, endTime).toMillis());
	}
	
}
