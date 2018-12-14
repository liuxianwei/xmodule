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
public interface BaseMybatisMapper<T extends BaseModel> {

	public int insertModel(T model);
	
	public int updateModelById(T model);
	
	public int dynamicUpdateModelById(T model);
	
	public int deleteModelById(Serializable id);
	
	public T selectModelById(Serializable id);
	
	public T selectModelByExample(T example);
	
	public List<T> selectModelListByIds(List<?> ids);
	
	public List<T> selectAllModelList();
	
	public int countAllModelList();
	
	public List<T> selectModelListByExample(@Param("example")T example, @Param("sort")Sort sort);
	
	public List<T> selectModelPageListByExample(@Param("example")T example, @Param("sort")Sort sort, RowBounds rowBounds);
	
	public int countModelPageListByExample(@Param("example")T example);
	
}
