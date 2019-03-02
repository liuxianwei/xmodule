package com.penglecode.xmodule.common.mybatis.mapper;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.penglecode.xmodule.common.support.BaseModel;
import com.penglecode.xmodule.common.support.Sort;

/**
 * Mybatis基础DAO
 * 
 * @author 	pengpeng
 * @date	2018年2月9日 下午3:43:08
 */
public interface BaseMybatisMapper<T extends BaseModel<T>> {

	/**
	 * 插入实体
	 * @param model
	 * @return
	 */
	public int insertModel(T model);
	
	/**
	 * 根据ID修改实体的字段
	 * @param model
	 * @return
	 */
	public int updateModelById(T model);
	
	/**
	 * 根据ID动态修改实体的字段，即有值的字段会被更新到数据库，无值的字段忽略
	 * @param model
	 * @return
	 */
	public int dynamicUpdateModelById(T model);
	
	/**
	 * 根据ID删除实体
	 * @param id
	 * @return
	 */
	public int deleteModelById(Serializable id);
	
	/**
	 * 根据ID查询单个结果集
	 * @param id
	 * @return
	 */
	public T selectModelById(Serializable id);
	
	/**
	 * 根据样例获取查询单个结果集
	 * (注意：如果匹配到多个结果集将报错)
	 * @param example	- 样例参数
	 * @return
	 */
	public T selectModelByExample(T example);
	
	/**
	 * 根据多个ID查询结果集
	 * @param ids
	 * @return
	 */
	public List<T> selectModelListByIds(List<?> ids);
	
	/**
	 * 查询所有结果集
	 * @return
	 */
	public List<T> selectAllModelList();
	
	/**
	 * 查询所有结果集计数
	 * @return
	 */
	public int countAllModelList();
	
	/**
	 * 根据样例查询结果集
	 * @param example	- 样例参数
	 * @param sort		- 排序参数
	 * @return
	 */
	public List<T> selectModelListByExample(@Param("example")T example, @Param("sort")Sort sort);
	
	/**
	 * 根据样例查询结果集(分页、排序)
	 * @param example	- 样例参数
	 * @param sort		- 排序参数
	 * @param rowBounds	- 分页参数
	 * @return
	 */
	public List<T> selectModelPageListByExample(@Param("example")T example, @Param("sort")Sort sort, RowBounds rowBounds);
	
	/**
	 * 根据样例查询结果集计数
	 * @param example
	 * @return
	 */
	public int countModelPageListByExample(@Param("example")T example);
	
}
