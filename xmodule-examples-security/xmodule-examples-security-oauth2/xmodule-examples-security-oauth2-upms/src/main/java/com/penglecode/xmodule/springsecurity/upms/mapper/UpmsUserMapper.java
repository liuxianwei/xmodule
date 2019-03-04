package com.penglecode.xmodule.springsecurity.upms.mapper;

import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.support.DefaultDatabase;
import com.penglecode.xmodule.springsecurity.upms.model.UpmsUser;

@DefaultDatabase
public interface UpmsUserMapper extends BaseMybatisMapper<UpmsUser> {
}