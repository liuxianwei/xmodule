package com.penglecode.xmodule.myexample.mapper;

import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.support.DefaultDatabase;
import com.penglecode.xmodule.myexample.model.Category;

@DefaultDatabase
public interface CategoryMapper extends BaseMybatisMapper<Category> {
}