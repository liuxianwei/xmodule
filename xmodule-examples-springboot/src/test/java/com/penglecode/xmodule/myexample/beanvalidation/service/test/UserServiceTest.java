package com.penglecode.xmodule.myexample.beanvalidation.service.test;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationConstants;
import com.penglecode.xmodule.myexample.beanvalidation.model.User;
import com.penglecode.xmodule.myexample.beanvalidation.service.UserService;
import com.penglecode.xmodule.myexample.boot.SpringBootExampleApplication;
import com.penglecode.xmodule.myexample.config.SpringBeanValidationExampleConfiguration;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.NONE, classes={SpringBootExampleApplication.class})
public class UserServiceTest {

	@Autowired
	private UserService userService;
	
	@BeforeClass
	public static void beforeClass() {
		System.setProperty("example.running", SpringBeanValidationExampleConfiguration.EXAMPLE_APP_NAME);
	}
	
	@Test
	public void createUser() throws Exception {
		System.out.println(ValidationConstants.USER_HOME);
		System.out.println(ValidationConstants.TMP_DIR);
		User user = new User();
		user.setUserId(null);
		user.setUserName("a");
		user.setPassword("123456");
		try {
			userService.createUser(user);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
}
