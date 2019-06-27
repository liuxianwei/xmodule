package com.penglecode.xmodule.newscenter.service;

import java.util.List;


import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.newscenter.model.News;

/**
 * 新闻服务
 * 
 * @author 	AutoGen
 * @date	2019年06月26日 下午 20:41:47
 */
public interface NewsService {

	/**
	 * 创建新闻
	 * @param parameter
	 */
	public void createNews(News parameter);
	
	/**
	 * 根据ID更新新闻
	 * @param parameter
	 */
	public void updateNews(News parameter);
	
	/**
	 * 根据ID删除新闻
	 * @param id
	 */
	public void deleteNewsById(String id);
	
	/**
	 * 根据ID获取新闻
	 * @param id
	 * @return
	 */
	public News getNewsById(String id);
	
	/**
	 * 根据条件查询新闻列表(排序、分页)
	 * @param condition		- 查询条件
	 * @param page			- 分页参数
	 * @param sort			- 排序参数
	 * @return
	 */
	public List<News> getNewsListByPage(News condition, Page page, Sort sort);
	
	/**
	 * 获取所有新闻列表
	 * @return
	 */
	public List<News> getAllNewsList();
	
}