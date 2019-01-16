package com.penglecode.xmodule.java8.newfeatures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.penglecode.xmodule.java8.model.StudentScore;

public class ComparatorExample {

	private static final List<StudentScore> ALL_STUDENTSCORE_LIST = new ArrayList<StudentScore>();
	
	static {
		ALL_STUDENTSCORE_LIST.add(new StudentScore("张三", "语文", 78.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("张三", "数学", 89.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("张三", "英语", 66.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("李四", "语文", 67.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("李四", "数学", 92.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("李四", "英语", 56.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("王五", "语文", 96.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("王五", "数学", 58.0));
		ALL_STUDENTSCORE_LIST.add(new StudentScore("王五", "英语", 89.0));
	}
	
	/**
	 * 按分数排名
	 */
	public static void compare1() {
		Collections.sort(ALL_STUDENTSCORE_LIST, Comparator.comparing(StudentScore::getScore).reversed());
		ALL_STUDENTSCORE_LIST.forEach(System.out::println);
	}
	
	/**
	 * 按课程、分数排名
	 */
	public static void compare2() {
		Collections.sort(ALL_STUDENTSCORE_LIST, Comparator.comparing(StudentScore::getCourse).thenComparing(StudentScore::getScore).reversed());
		ALL_STUDENTSCORE_LIST.forEach(System.out::println);
	}
	
	public static void main(String[] args) {
		//compare1();
		compare2();
	}

}
