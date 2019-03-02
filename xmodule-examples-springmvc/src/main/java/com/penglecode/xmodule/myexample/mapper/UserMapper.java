package com.penglecode.xmodule.myexample.mapper;

import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.support.DefaultDatabase;
import com.penglecode.xmodule.myexample.model.User;

@DefaultDatabase
public interface UserMapper extends BaseMybatisMapper<User> {
}
