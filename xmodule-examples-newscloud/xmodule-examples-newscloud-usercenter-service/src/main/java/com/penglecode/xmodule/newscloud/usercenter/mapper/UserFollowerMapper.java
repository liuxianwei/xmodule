package com.penglecode.xmodule.newscloud.usercenter.mapper;

import org.apache.ibatis.annotations.Param;

import com.penglecode.xmodule.common.mybatis.mapper.BaseMybatisMapper;
import com.penglecode.xmodule.common.support.DefaultDatabase;
import com.penglecode.xmodule.newscloud.usercenter.model.UserFollower;

@DefaultDatabase
public interface UserFollowerMapper extends BaseMybatisMapper<UserFollower> {
	
	public int deleteUserFollower(@Param("userId") Long userId, @Param("followerUserId") Long followerUserId);
	
}