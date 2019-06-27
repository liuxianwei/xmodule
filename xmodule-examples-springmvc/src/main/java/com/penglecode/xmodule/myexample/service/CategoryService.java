package com.penglecode.xmodule.myexample.service;

import java.util.List;


import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.myexample.model.Category;

/**
 * 商品分类服务
 * 
 * @author 	AutoGen
 * @date	2019年06月27日 下午 13:34:08
 */
public interface CategoryService {

	/**
	 * 创建商品分类
	 * @param parameter
	 */
	public void createCategory(Category parameter);
	
	/**
	 * 根据ID更新商品分类
	 * @param parameter
	 */
	public void updateCategory(Category parameter);
	
	/**
	 * 根据ID删除商品分类
	 * @param id
	 */
	public void deleteCategoryById(Long id);
	
	/**
	 * 根据ID获取商品分类
	 * @param id
	 * @return
	 */
	public Category getCategoryById(Long id);
	
	/**
	 * 根据条件查询商品分类列表(排序、分页)
	 * @param condition		- 查询条件
	 * @param page			- 分页参数
	 * @param sort			- 排序参数
	 * @return
	 */
	public List<Category> getCategoryListByPage(Category condition, Page page, Sort sort);
	
	/**
	 * 获取所有商品分类列表
	 * @return
	 */
	public List<Category> getAllCategoryList();
	
}