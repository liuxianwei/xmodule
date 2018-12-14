package com.penglecode.xmodule.springsecurity.upms.model;

import com.penglecode.xmodule.common.support.BaseModel;

/**
 * UpmsRole (sa_upms_role) 实体类
 * 
 * @author PengPeng
 * @date	2018-10-22 17:35:43
 */
public class UpmsRole implements BaseModel {
     
    private static final long serialVersionUID = 1L;

    /** 角色id */
    private Long roleId;

    /** 角色名称 */
    private String roleName;

    /** 角色代码,由字母、下划线组成 */
    private String roleCode;

    /** 角色类型：0-系统角色,1-普通角色 */
    private Integer roleType;

    /** 角色描述 */
    private String description;

    /** 创建时间 */
    private String createTime;

    /** 创建者,用户表的id */
    private Long createBy;

    /** 最近更新时间 */
    private String updateTime;

    /** update_by */
    private Long updateBy;
    
    //以下属于辅助字段
    
    private String roleTypeName;
    
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

	public String getRoleTypeName() {
		return roleTypeName;
	}

	public void setRoleTypeName(String roleTypeName) {
		this.roleTypeName = roleTypeName;
	}

}