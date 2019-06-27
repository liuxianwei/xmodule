package com.penglecode.xmodule.newscloud.newscenter.service.api;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.BusinessAssert;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.PageResult;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.ModelDecodeUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.util.UUIDUtils;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.newscloud.newscenter.consts.em.NewsAuditStatusEnum;
import com.penglecode.xmodule.newscloud.newscenter.mapper.NewsMapper;
import com.penglecode.xmodule.newscloud.newscenter.model.News;
import com.penglecode.xmodule.newscloud.usercenter.consts.em.UserStatusEnum;

@RestController("defaultNewsApiService")
public class NewsApiServiceImpl extends HttpAPIResourceSupport implements NewsApiService {

	public static final Consumer<News> NEWS_DECODER = model -> {
        if (model.getAuditStatus() != null) {
            UserStatusEnum em = UserStatusEnum.getStatus(model.getAuditStatus());
            if (em != null) {
                model.setAuditStatusName(em.getStatusName());
            }
        }
	};
	
	@Autowired
	private NewsMapper newsMapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Result<String> createNews(News news) {
		ValidationAssert.notNull(news, "参数不能为空!");
		ValidationAssert.notEmpty(news.getNewsTitle(), "新闻标题不能为空!");
		ValidationAssert.notEmpty(news.getNewsContent(), "新闻内容不能为空!");
		ValidationAssert.notEmpty(news.getNewsTags(), "新闻关键字不能为空!");
		ValidationAssert.notNull(news.getPublisherId(), "发布者ID不能为空!");
		news.setNewsId(StringUtils.defaultIfEmpty(news.getNewsId(), UUIDUtils.uuid()));
		news.setDeleted(Boolean.FALSE);
		news.setAuditStatus(NewsAuditStatusEnum.AUDIT_WAITING.getStatusCode());
		news.setCreateTime(DateTimeUtils.formatNow());
		news.setCreateBy(1L);
		try {
			newsMapper.insertModel(news);
		} catch (DuplicateKeyException e) {
            BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("NEWS_TITLE"), "对不起,重复的新闻标题!");
            throw e;
        }
		return Result.success().message("OK").data(news.getNewsId()).build();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Result<Object> updateNews(News news) {
		ValidationAssert.notNull(news, "参数不能为空!");
		news.setUpdateTime(DateTimeUtils.formatNow());
		news.setUpdateBy(1L);
		try {
			Map<String,Object> paramMap = news.mapBuilder().withNewsTitle().withNewsTags().withNewsContent().withUpdateBy().withUpdateTime().build();
			newsMapper.updateModelById(news.getNewsId(), paramMap);
		} catch (DuplicateKeyException e) {
            BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("NEWS_TITLE"), "对不起,重复的新闻标题!");
            throw e;
        }
		return Result.success().message("OK").build();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Result<Object> deleteNewsById(String newsId, Boolean forceDelete) {
		ValidationAssert.notEmpty(newsId, "新闻ID不能为空!");
		if(ObjectUtils.defaultIfNull(forceDelete, Boolean.FALSE)) {
			newsMapper.deleteModelById(newsId);
		} else {
			Map<String,Object> paramMap = new News().mapBuilder().withDeleted(Boolean.TRUE).build();
			newsMapper.updateModelById(newsId, paramMap);
		}
		return Result.success().message("OK").build();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Result<Object> publishNews(News news) {
		ValidationAssert.notNull(news, "参数不能为空!");
		ValidationAssert.notEmpty(news.getNewsId(), "新闻ID不能为空!");
		news.setPublishTime(DateTimeUtils.formatNow());
		News parameter = new News();
		parameter.setNewsId(news.getNewsId());
		parameter.setPublishTime(news.getPublishTime());
		parameter.setPublisherId(1L);
		Map<String,Object> paramMap = news.mapBuilder().withPublisherId().withPublishTime().build();
		newsMapper.updateModelById(news.getNewsId(), paramMap);
		return Result.success().message("OK").build();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Result<Object> auditNews(News news) {
		ValidationAssert.notNull(news, "参数不能为空!");
		ValidationAssert.notEmpty(news.getNewsId(), "新闻ID不能为空!");
		ValidationAssert.notNull(NewsAuditStatusEnum.getStatus(news.getAuditStatus()), "审核状态不能为空!");
		news.setAuditTime(DateTimeUtils.formatNow());
		News parameter = new News();
		parameter.setNewsId(news.getNewsId());
		parameter.setAuditBy(1L);
		parameter.setAuditStatus(news.getAuditStatus());
		parameter.setAuditTime(news.getAuditTime());
		parameter.setAuditRemark(news.getAuditRemark());
		Map<String,Object> paramMap = news.mapBuilder().withAuditBy().withAuditRemark().withAuditStatus().withAuditTime().build();
		newsMapper.updateModelById(news.getNewsId(), paramMap);
		return Result.success().message("OK").build();
	}

	@Override
	public Result<News> getNewsById(String newsId) {
		News data = ModelDecodeUtils.decodeModel(newsMapper.selectModelById(newsId), NEWS_DECODER);
		return Result.success().message("OK").data(data).build();
	}

	@Override
	public PageResult<List<News>> getNewsListByPage(News condition, Page page, Sort sort) {
		List<News> dataList = ModelDecodeUtils.decodeModel(newsMapper.selectModelPageListByExample(condition, sort, new RowBounds(page.getOffset(), page.getLimit())), NEWS_DECODER);
		page.setTotalRowCount(newsMapper.countModelPageListByExample(condition));
		return PageResult.success().message("OK").data(dataList).totalRowCount(page.getTotalRowCount()).build();
	}

	@Override
	public Result<List<News>> getNewsListByUserId(Long userId, Boolean deleted, Integer... auditStatuses) {
		ValidationAssert.notNull(userId, "发布人ID不能为空!");
		List<News> dataList = ModelDecodeUtils.decodeModel(newsMapper.selectNewsListByUserId(userId, deleted, Arrays.asList(auditStatuses)), NEWS_DECODER);
		return Result.success().message("OK").data(dataList).build();
	}

}
