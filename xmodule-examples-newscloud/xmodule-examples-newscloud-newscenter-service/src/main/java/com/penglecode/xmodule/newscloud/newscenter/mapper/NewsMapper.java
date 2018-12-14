package com.penglecode.xmodule.newscloud.newscenter.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.support.DefaultDatabase;
import com.penglecode.xmodule.newscloud.newscenter.model.News;

@DefaultDatabase
public interface NewsMapper extends BaseMybatisMapper<News> {
	
	/**
	 * 软删除
	 * @param newsId
	 * @return
	 */
	public int removeNewsById(@Param("newsId") String newsId);
	
	public List<News> selectNewsListByUserId(@Param("userId") Long userId, @Param("deleted") Boolean deleted, @Param("auditStatuses") List<Integer> auditStatuses);
	
}