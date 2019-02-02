package com.penglecode.xmodule.common.serializer.protostuff;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.penglecode.xmodule.common.util.JsonUtils;

public class ProtostuffExample {

	public static void main(String[] args) {
		User user = new User();
		user.setUserName("admin");
		user.setPassword("123456");
		user.setLocale(Locale.CHINESE);
		user.setHobbies(Arrays.asList("foo", "bar"));
		System.out.println(JsonUtils.object2Json(user));
		byte[] bytes = ProtostuffSerializer.INSTANCE.serialize(user);
		System.out.println(bytes);
		
		User user1 = ProtostuffSerializer.INSTANCE.deserialize(bytes);
		System.out.println(JsonUtils.object2Json(user1));
	}

	public static class User {
		
		private String userName;
		
		private String password;
		
		private Locale locale;
		
		private List<String> hobbies;

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public Locale getLocale() {
			return locale;
		}

		public void setLocale(Locale locale) {
			this.locale = locale;
		}

		public List<String> getHobbies() {
			return hobbies;
		}

		public void setHobbies(List<String> hobbies) {
			this.hobbies = hobbies;
		}
		
	}
	
}
