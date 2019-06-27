package com.penglecode.xmodule.newscenter.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.penglecode.xmodule.common.support.BusinessAssert;
import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.ModelDecodeUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.util.UUIDUtils;
import com.penglecode.xmodule.newscenter.consts.em.NewsAuditStatusEnum;
import com.penglecode.xmodule.newscenter.mapper.NewsMapper;
import com.penglecode.xmodule.newscenter.model.News;
import com.penglecode.xmodule.newscenter.service.NewsService;

@Service("newsService")
public class NewsServiceImpl implements NewsService {

	@Autowired
	private NewsMapper newsMapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void createNews(News parameter) {
		ValidationAssert.notNull(parameter, "参数不能为空");
		parameter.setNewsId(StringUtils.defaultIfEmpty(parameter.getNewsId(), UUIDUtils.uuid()));
		parameter.setDeleted(Boolean.FALSE);
		parameter.setAuditStatus(NewsAuditStatusEnum.AUDIT_WAITING.getStatusCode());
		parameter.setCreateTime(DateTimeUtils.formatNow());
		parameter.setCreateBy(1L);
		try {
			newsMapper.insertModel(parameter);
		} catch (DuplicateKeyException e) {
            BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("NEWS_TITLE"), "对不起,重复的新闻标题!");
            throw e;
        }
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updateNews(News parameter) {
		ValidationAssert.notNull(parameter, "参数不能为空");
		parameter.setUpdateTime(DateTimeUtils.formatNow());
		parameter.setUpdateBy(1L);
		Map<String, Object> paramMap = parameter.mapBuilder()
												.withUpdateBy()
												.withUpdateTime()
												.withNewsTitle()
												.withNewsTags()
												.withNewsContent()
												.build();
		try {
			newsMapper.updateModelById(parameter.getNewsId(), paramMap);
		} catch (DuplicateKeyException e) {
            BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("NEWS_TITLE"), "对不起,重复的新闻标题!");
            throw e;
        }
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void deleteNewsById(String id) {
		ValidationAssert.notNull(id, "id不能为空");
		newsMapper.deleteModelById(id);
	}

	@Override
	public News getNewsById(String id) {
		return ModelDecodeUtils.decodeModel(newsMapper.selectModelById(id));
	}

	@Override
	public List<News> getNewsListByPage(News condition, Page page, Sort sort) {
		List<News> dataList = ModelDecodeUtils.decodeModel(newsMapper.selectModelPageListByExample(condition, sort, new RowBounds(page.getOffset(), page.getLimit())));
    	page.setTotalRowCount(newsMapper.countModelPageListByExample(condition));
		return dataList;
	}

	@Override
	public List<News> getAllNewsList() {
		return ModelDecodeUtils.decodeModel(newsMapper.selectAllModelList());
	}

}