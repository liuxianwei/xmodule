package com.penglecode.xmodule.newscloud.newscenter.service.api;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.PageResult;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.newscloud.newscenter.model.News;

/**
 * 新闻API服务
 * 
 * @author 	pengpeng
 * @date	2018年10月8日 上午11:21:07
 */
@FeignClient(name="newscloud-newscenter-service", qualifier="newsApiService")
@RequestMapping("/api/news")
public interface NewsApiService {

	/**
	 * 创建新闻稿
	 * @param news
	 */
	@RequestMapping(value="/create", method=POST, produces=APPLICATION_JSON, consumes=APPLICATION_JSON)
	public Result<String> createNews(@RequestBody News news);
	
	/**
	 * 修改新闻稿
	 * @param news
	 */
	@RequestMapping(value="/update", method=PUT, produces=APPLICATION_JSON, consumes=APPLICATION_JSON)
	public Result<Object> updateNews(@RequestBody News news);
	
	/**
	 * 根据ID删除新闻
	 * @param newsId
	 */
	@RequestMapping(value="/delete/{newsId}", method=DELETE, produces=APPLICATION_JSON)
	public Result<Object> deleteNewsById(@PathVariable("newsId") String newsId, Boolean forceDelete);
	
	/**
	 * 发布新闻
	 * @param parameter[newsId, publishTime]
	 */
	@RequestMapping(value="/publish", method=POST, produces=APPLICATION_JSON, consumes=APPLICATION_JSON)
	public Result<Object> publishNews(@RequestBody News news);
	
	/**
	 * 审核新闻
	 * @param news
	 */
	@RequestMapping(value="/audit", method=POST, produces=APPLICATION_JSON, consumes=APPLICATION_JSON)
	public Result<Object> auditNews(@RequestBody News news);
	
	/**
	 * 根据ID获取新闻
	 * @param newsId
	 * @return
	 */
	@RequestMapping(value="/{newsId}", method=GET, produces=APPLICATION_JSON)
	public Result<News> getNewsById(@PathVariable("newsId") String newsId);
	
	/**
	 * 根据条件查询新闻列表(分页、排序)
	 * @param condition
	 * @param page
	 * @param sort
	 * @return
	 */
	@RequestMapping(value="/list", method=GET, produces=APPLICATION_JSON)
	public PageResult<List<News>> getNewsListByPage(News condition, Page page, Sort sort);
	
	/**
	 * 根据发布者用户ID获取新闻列表
	 * @param userId
	 * @param deleted
	 * @param auditStatuses
	 * @return
	 */
	@RequestMapping(value="/list/{userId}", method=GET, produces=APPLICATION_JSON)
	public Result<List<News>> getNewsListByUserId(@PathVariable("userId") Long userId, Boolean deleted, Integer... auditStatuses);
	
}
