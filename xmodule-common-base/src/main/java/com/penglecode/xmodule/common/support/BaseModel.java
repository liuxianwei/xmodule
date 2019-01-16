package com.penglecode.xmodule.common.support;

/**
 * 基础数据模型
 * 
 * @author 	pengpeng
 * @date   		2017年6月6日 下午5:17:39
 * @version 	1.0
 */
public interface BaseModel<T extends BaseModel<T>> extends DtoModel {

	/**
	 * 对数据模型的decode
	 * 例如根据statusCode(状态码字段)设置statusName(状态码的字面意思字段)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	default public T decode() {
		return (T) this;
	}
	
}
