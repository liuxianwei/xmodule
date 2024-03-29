package com.penglecode.xmodule.newscloud.usercenter.model;

import com.penglecode.xmodule.common.codegen.Id;
import com.penglecode.xmodule.common.codegen.Model;
import com.penglecode.xmodule.common.support.BaseModel;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用户 (nc_user) 实体类
 * 
 * @author Mybatis-Generator
 * @date	2019年06月27日 上午 11:18:55
 */
@Model(name="用户")
public class User implements BaseModel<User> {
     
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @Id
    private Long userId;

    /** 用户名 */
    private String userName;

    /** 昵称 */
    private String nickName;

    /** 手机号码 */
    private String mobilePhone;

    /** 用户头像URL */
    private String userIconUrl;

    /** 电子邮箱 */
    private String email;

    /** 性别:男，女 */
    private String sex;

    /** 出生日期 */
    private String birthday;

    /** 账户状态：0-禁用一切,1-正常,2-禁言 */
    private Integer status;

    /** 是否已认证：1-是，0-否 */
    private Boolean authorized;

    /** 已认证身份证号码 */
    private String authIdcard;

    /** 已认证真实姓名 */
    private String authRealName;

    /** 登录次数 */
    private Integer loginTimes;

    /** 最后登录时间 */
    private String lastLoginTime;

    /** 关注数(关注他人) */
    private Integer follows;

    /** 粉丝数(被被人关注) */
    private Integer followers;

    /** 创建时间 */
    private String createTime;

    /** 最近修改时间 */
    private String updateTime;

    //以下属于辅助字段
    
    private String statusName;
    
    private String serviceProvider;
    
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

    public String getUserIconUrl() {
        return userIconUrl;
    }

    public void setUserIconUrl(String userIconUrl) {
        this.userIconUrl = userIconUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }

    public String getAuthIdcard() {
        return authIdcard;
    }

    public void setAuthIdcard(String authIdcard) {
        this.authIdcard = authIdcard;
    }

    public String getAuthRealName() {
        return authRealName;
    }

    public void setAuthRealName(String authRealName) {
        this.authRealName = authRealName;
    }

    public Integer getLoginTimes() {
        return loginTimes;
    }

    public void setLoginTimes(Integer loginTimes) {
        this.loginTimes = loginTimes;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getFollows() {
        return follows;
    }

    public void setFollows(Integer follows) {
        this.follows = follows;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public MapBuilder mapBuilder() {
        return new MapBuilder();
    }

    /**
     * Auto generated by Mybatis Generator
     */
    public class MapBuilder {
         
        private final Map<String, Object> modelProperties = new LinkedHashMap<String,Object>();

        MapBuilder() {
            
        }

        public MapBuilder withUserId(Long ... userId) {
            modelProperties.put("userId", BaseModel.first(userId, getUserId()));
            return this;
        }

        public MapBuilder withUserName(String ... userName) {
            modelProperties.put("userName", BaseModel.first(userName, getUserName()));
            return this;
        }

        public MapBuilder withNickName(String ... nickName) {
            modelProperties.put("nickName", BaseModel.first(nickName, getNickName()));
            return this;
        }

        public MapBuilder withMobilePhone(String ... mobilePhone) {
            modelProperties.put("mobilePhone", BaseModel.first(mobilePhone, getMobilePhone()));
            return this;
        }

        public MapBuilder withUserIconUrl(String ... userIconUrl) {
            modelProperties.put("userIconUrl", BaseModel.first(userIconUrl, getUserIconUrl()));
            return this;
        }

        public MapBuilder withEmail(String ... email) {
            modelProperties.put("email", BaseModel.first(email, getEmail()));
            return this;
        }

        public MapBuilder withSex(String ... sex) {
            modelProperties.put("sex", BaseModel.first(sex, getSex()));
            return this;
        }

        public MapBuilder withBirthday(String ... birthday) {
            modelProperties.put("birthday", BaseModel.first(birthday, getBirthday()));
            return this;
        }

        public MapBuilder withStatus(Integer ... status) {
            modelProperties.put("status", BaseModel.first(status, getStatus()));
            return this;
        }

        public MapBuilder withAuthorized(Boolean ... authorized) {
            modelProperties.put("authorized", BaseModel.first(authorized, getAuthorized()));
            return this;
        }

        public MapBuilder withAuthIdcard(String ... authIdcard) {
            modelProperties.put("authIdcard", BaseModel.first(authIdcard, getAuthIdcard()));
            return this;
        }

        public MapBuilder withAuthRealName(String ... authRealName) {
            modelProperties.put("authRealName", BaseModel.first(authRealName, getAuthRealName()));
            return this;
        }

        public MapBuilder withLoginTimes(Integer ... loginTimes) {
            modelProperties.put("loginTimes", BaseModel.first(loginTimes, getLoginTimes()));
            return this;
        }

        public MapBuilder withLastLoginTime(String ... lastLoginTime) {
            modelProperties.put("lastLoginTime", BaseModel.first(lastLoginTime, getLastLoginTime()));
            return this;
        }

        public MapBuilder withFollows(Integer ... follows) {
            modelProperties.put("follows", BaseModel.first(follows, getFollows()));
            return this;
        }

        public MapBuilder withFollowers(Integer ... followers) {
            modelProperties.put("followers", BaseModel.first(followers, getFollowers()));
            return this;
        }

        public MapBuilder withCreateTime(String ... createTime) {
            modelProperties.put("createTime", BaseModel.first(createTime, getCreateTime()));
            return this;
        }

        public MapBuilder withUpdateTime(String ... updateTime) {
            modelProperties.put("updateTime", BaseModel.first(updateTime, getUpdateTime()));
            return this;
        }

        public Map<String, Object> build() {
            return modelProperties;
        }
    }
}