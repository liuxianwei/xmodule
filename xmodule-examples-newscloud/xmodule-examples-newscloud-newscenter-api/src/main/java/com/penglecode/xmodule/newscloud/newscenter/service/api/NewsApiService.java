package com.penglecode.xmodule.newscloud.newscenter.service.api;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.PageResult;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.newscloud.newscenter.model.News;

/**
 * 用户API服务
 * 
 * @author 	pengpeng
 * @date	2019年5月20日 下午5:37:21
 */
@FeignClient(name="newscloud-newscenter-service", qualifier="newsApiService")
public interface NewsApiService {

	/**
	 * 创建新闻稿
	 * @param news
	 */
	@PostMapping(value="/api/news/create", produces=APPLICATION_JSON, consumes=APPLICATION_JSON)
	public Result<String> createNews(@RequestBody News news);
	
	/**
	 * 修改新闻稿
	 * @param news
	 */
	@PutMapping(value="/api/news/update", produces=APPLICATION_JSON, consumes=APPLICATION_JSON)
	public Result<Object> updateNews(@RequestBody News news);
	
	/**
	 * 根据ID删除新闻
	 * @param newsId
	 */
	@DeleteMapping(value="/api/news/delete/{newsId}", produces=APPLICATION_JSON)
	public Result<Object> deleteNewsById(@PathVariable("newsId") String newsId, Boolean forceDelete);
	
	/**
	 * 发布新闻
	 * @param parameter[newsId, publishTime]
	 */
	@PostMapping(value="/api/news/publish", produces=APPLICATION_JSON, consumes=APPLICATION_JSON)
	public Result<Object> publishNews(@RequestBody News news);
	
	/**
	 * 审核新闻
	 * @param news
	 */
	@PostMapping(value="/api/news/audit", produces=APPLICATION_JSON, consumes=APPLICATION_JSON)
	public Result<Object> auditNews(@RequestBody News news);
	
	/**
	 * 根据ID获取新闻
	 * @param newsId
	 * @return
	 */
	@GetMapping(value="/api/news/{newsId}", produces=APPLICATION_JSON)
	public Result<News> getNewsById(@PathVariable("newsId") String newsId);
	
	/**
	 * 根据条件查询新闻列表(分页、排序)
	 * @param condition
	 * @param page
	 * @param sort
	 * @return
	 */
	@GetMapping(value="/api/news/list", produces=APPLICATION_JSON)
	public PageResult<List<News>> getNewsListByPage(News condition, Page page, Sort sort);
	
	/**
	 * 根据发布者用户ID获取新闻列表
	 * @param userId
	 * @param deleted
	 * @param auditStatuses
	 * @return
	 */
	@GetMapping(value="/api/news/list/{userId}", produces=APPLICATION_JSON)
	public Result<List<News>> getNewsListByUserId(@PathVariable("userId") Long userId, @RequestParam("deleted")Boolean deleted, @RequestParam("auditStatuses")Integer... auditStatuses);
	
}
