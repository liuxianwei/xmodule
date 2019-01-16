package com.penglecode.xmodule.upms.model;

import com.penglecode.xmodule.common.support.BaseModel;
import com.penglecode.xmodule.upms.consts.em.UpmsAppTypeEnum;

/**
 * UpmsApp (upms_app) 实体类
 * 
 * @author PengPeng
 * @date	2019-01-15 10:31:23
 */
public class UpmsApp implements BaseModel<UpmsApp> {
     
    private static final long serialVersionUID = 1L;

    /** 应用ID */
    private Long appId;

    /** 应用名称 */
    private String appName;

    /** 应用描述 */
    private String description;
    
    /** 应用类型：0-核心应用,1-普通应用 */
    private Integer appType;

    /** 应用key */
    private String appKey;

    /** 应用安全码 */
    private String appSecret;

    /** 前台web应用的contextpath */
    private String appWebContextPath;

    /** 后台api应用的contextpath */
    private String appApiContextPath;

    /** 启用/禁用 */
    private Boolean enabled;

    /** 创建时间 */
    private String createTime;
    
    /** 创建者ID */
    private Long createBy;
    
    //以下为辅助字段
    
    private String appTypeName;

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAppType() {
		return appType;
	}

	public void setAppType(Integer appType) {
		this.appType = appType;
	}

	public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppWebContextPath() {
        return appWebContextPath;
    }

    public void setAppWebContextPath(String appWebContextPath) {
        this.appWebContextPath = appWebContextPath;
    }

    public String getAppApiContextPath() {
        return appApiContextPath;
    }

    public void setAppApiContextPath(String appApiContextPath) {
        this.appApiContextPath = appApiContextPath;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

	public String getAppTypeName() {
		return appTypeName;
	}

	public void setAppTypeName(String appTypeName) {
		this.appTypeName = appTypeName;
	}

	@Override
	public UpmsApp decode() {
		if(appType != null){
			UpmsAppTypeEnum em = UpmsAppTypeEnum.getAppType(appType);
			if(em != null){
				setAppTypeName(em.getTypeName());
			}
		}
		return this;
	}
    
}