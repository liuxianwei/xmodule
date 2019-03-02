package com.penglecode.xmodule.myexample.test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.penglecode.xmodule.myexample.mapper.UserMapper;

public class GenericTest {

	public static void main(String[] args) {
		Type[] types = UserMapper.class.getGenericInterfaces();
		System.out.println(types);
		ParameterizedType type = (ParameterizedType) types[0];
		System.out.println(type.getActualTypeArguments()[0]);
	}

}
