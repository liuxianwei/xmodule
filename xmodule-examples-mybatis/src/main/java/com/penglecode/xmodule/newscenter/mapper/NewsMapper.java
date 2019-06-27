package com.penglecode.xmodule.newscenter.mapper;

import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.support.DefaultDatabase;
import com.penglecode.xmodule.newscenter.model.News;

@DefaultDatabase
public interface NewsMapper extends BaseMybatisMapper<News> {
}