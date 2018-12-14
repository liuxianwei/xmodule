package com.penglecode.xmodule.common.support;

/**
 * 单值Holder类，解决匿名内部类使用外部变量问题
 * @param <T>
 * @author 	pengpeng
 * @date	2018年2月8日 上午11:05:45
 */
public class KeyValueHolder<K,V> {

	private K key;
	
	private V value;

	public KeyValueHolder() {
		super();
	}

	public KeyValueHolder(K key, V value) {
		super();
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

}
