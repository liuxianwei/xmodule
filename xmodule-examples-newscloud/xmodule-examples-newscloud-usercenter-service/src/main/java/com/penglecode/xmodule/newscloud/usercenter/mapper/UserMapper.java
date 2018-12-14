package com.penglecode.xmodule.newscloud.usercenter.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.support.DefaultDatabase;
import com.penglecode.xmodule.newscloud.usercenter.model.User;

@DefaultDatabase
public interface UserMapper extends BaseMybatisMapper<User> {
	
	public List<User> selectUserListByStatus(@Param("authorized")Boolean authorized, @Param("statuses")List<Integer> statuses);
	
}