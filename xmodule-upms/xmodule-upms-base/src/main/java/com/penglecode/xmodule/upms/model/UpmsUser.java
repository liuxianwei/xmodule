package com.penglecode.xmodule.upms.model;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.support.BaseModel;
import com.penglecode.xmodule.upms.consts.em.UpmsUserStatusEnum;
import com.penglecode.xmodule.upms.consts.em.UpmsUserTypeEnum;

/**
 * UpmsUser (upms_user) 实体类
 * 
 * @author PengPeng
 * @date	2019-01-15 10:31:24
 */
public class UpmsUser implements BaseModel<UpmsUser> {
     
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
    
    private String repassword;
    
    private String oldpassword;
    
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

	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	public String getOldpassword() {
		return oldpassword;
	}

	public void setOldpassword(String oldpassword) {
		this.oldpassword = oldpassword;
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

	@Override
	public UpmsUser decode() {
		if (status != null) {
            UpmsUserStatusEnum em = UpmsUserStatusEnum.getStatus(status);
            if (em != null) {
                setStatusName(em.getStatusName());
            }
        }
        if (userType != null) {
            UpmsUserTypeEnum em = UpmsUserTypeEnum.getUserType(userType);
            if (em != null) {
                setUserTypeName(em.getTypeName());
            }
        }
        if (userIcon != null && !userIcon.trim().equals("")) {
        	String userIconUrl = userIcon;
        	if(userIconUrl.toLowerCase().startsWith("/static/")){ //upms UI工程目录下的本地静态图片
        		userIconUrl = GlobalConstants.MVVM_APP_CONFIG.getAppWebServerUrl() + userIconUrl;
        	} else if (userIconUrl.toLowerCase().startsWith("http")){
        		//nothing to do
        	} else { //默认以全局静态文件来处理
        		userIconUrl = GlobalConstants.MVVM_APP_CONFIG.getGlobalFileServerUrl() + userIconUrl;
        	}
        	setUserIconUrl(userIconUrl);
        }
		return this;
	}
    
}