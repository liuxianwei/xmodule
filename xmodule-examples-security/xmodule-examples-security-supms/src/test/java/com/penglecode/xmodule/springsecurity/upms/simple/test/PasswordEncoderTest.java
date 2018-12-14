package com.penglecode.xmodule.springsecurity.upms.simple.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.penglecode.xmodule.springsecurity.upms.boot.SimpleUpmsSecurityExampleApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.MOCK, classes={SimpleUpmsSecurityExampleApplication.class})
public class PasswordEncoderTest {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Test
	public void testMessageSource() {
		String rawPassword = "123456";
		System.out.println(passwordEncoder.encode(rawPassword));
	}
	
}
