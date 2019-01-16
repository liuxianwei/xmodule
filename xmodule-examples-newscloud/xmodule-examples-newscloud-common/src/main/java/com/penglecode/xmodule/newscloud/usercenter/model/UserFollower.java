package com.penglecode.xmodule.newscloud.usercenter.model;

import com.penglecode.xmodule.common.support.BaseModel;

/**
 * UserFollower (nc_user_follower) 实体类
 * 
 * @author PengPeng
 * @date	2018-09-30 10:03:21
 */
public class UserFollower implements BaseModel<UserFollower> {
     
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 被关注用户ID */
    private Long userId;

    /** 粉丝ID */
    private Long followerUserId;

    /** 关注时间 */
    private String followTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFollowerUserId() {
        return followerUserId;
    }

    public void setFollowerUserId(Long followerUserId) {
        this.followerUserId = followerUserId;
    }

    public String getFollowTime() {
        return followTime;
    }

    public void setFollowTime(String followTime) {
        this.followTime = followTime;
    }
}