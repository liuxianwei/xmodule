package com.penglecode.xmodule.springsecurity.upms.model;

import com.penglecode.xmodule.common.support.BaseModel;

/**
 * UpmsUser (sa_upms_user) 实体类
 * 
 * @author PengPeng
 * @date	2018-10-22 17:35:43
 */
public class UpmsUser implements BaseModel {
     
    private static final long serialVersionUID = 1L;

    /** 用户id */
    private Long userId;

    /** 用户名 */
    private String userName;

    /** 真实姓名 */
    private String realName;

    /** 密码 */
    private String password;

    /** 用户类型：0-系统用户,1-普通用户 */
    private Integer userType;

    /** 昵称 */
    private String nickName;

    /** 手机号码 */
    private String mobilePhone;

    /** 电子邮箱 */
    private String email;

    /** 用户头像 */
    private String userIcon;

    /** 用户状态:0-冻结,1-正常 */
    private Integer status;

    /** 最后登录时间 */
    private String lastLoginTime;

    /** 登录次数 */
    private Integer loginTimes;

    /** 创建时间 */
    private String createTime;

    /** 创建者,用户表的id */
    private Long createBy;

    /** 更新时间 */
    private String updateTime;

    /** 更新者,用户表的id */
    private Long updateBy;

    //以下属于辅助字段
    
    private String statusName;
    
    private String userTypeName;
    
    private String userIconUrl;
    
	public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getLoginTimes() {
        return loginTimes;
    }

    public void setLoginTimes(Integer loginTimes) {
        this.loginTimes = loginTimes;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getUserTypeName() {
		return userTypeName;
	}

	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}

	public String getUserIconUrl() {
		return userIconUrl;
	}

	public void setUserIconUrl(String userIconUrl) {
		this.userIconUrl = userIconUrl;
	}

}