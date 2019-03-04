package com.penglecode.xmodule.myexample.service;

import java.util.List;


import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.myexample.model.Product;

/**
 * 商品信息服务
 * 
 * @author 	AutoGen
 * @date	2019年03月04日 上午 10:44:21
 */
public interface ProductService {

	/**
	 * 创建商品信息
	 * @param parameter
	 */
	public void createProduct(Product parameter);
	
	/**
	 * 根据ID更新商品信息
	 * @param parameter
	 */
	public void updateProduct(Product parameter);
	
	/**
	 * 根据ID删除商品信息
	 * @param id
	 */
	public void deleteProductById(Long id);
	
	/**
	 * 根据ID获取商品信息
	 * @param id
	 * @return
	 */
	public Product getProductById(Long id);
	
	/**
	 * 根据条件查询商品信息列表(排序、分页)
	 * @param condition		- 查询条件
	 * @param page			- 分页参数
	 * @param sort			- 排序参数
	 * @return
	 */
	public List<Product> getProductListByPage(Product condition, Page page, Sort sort);
	
	/**
	 * 获取所有商品信息列表
	 * @return
	 */
	public List<Product> getAllProductList();
	
}