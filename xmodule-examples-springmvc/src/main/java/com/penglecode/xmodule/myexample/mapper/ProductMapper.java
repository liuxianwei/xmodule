package com.penglecode.xmodule.myexample.mapper;

import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.support.DefaultDatabase;
import com.penglecode.xmodule.myexample.model.Product;

@DefaultDatabase
public interface ProductMapper extends BaseMybatisMapper<Product> {
}