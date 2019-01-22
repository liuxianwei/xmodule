package com.penglecode.xmodule.upms.model;

import java.util.List;

import com.penglecode.xmodule.common.support.BaseModel;
import com.penglecode.xmodule.upms.consts.em.UpmsResourceActionTypeEnum;
import com.penglecode.xmodule.upms.consts.em.UpmsResourceTypeEnum;

/**
 * UpmsResource (upms_resource) 实体类
 * 
 * @author PengPeng
 * @date	2019-01-15 10:31:24
 */
public class UpmsResource implements BaseModel<UpmsResource>, Comparable<UpmsResource> {
     
    private static final long serialVersionUID = 1L;

    /** 资源id */
    private Long resourceId;

    /** 资源名称 */
    private String resourceName;

    /** 父级资源id */
    private Long parentResourceId;

    /** 是否是应用的根节点：1-是，0-否 */
    private Boolean appRootResource;

    /** 权限表达式 */
    private String permissionExpression;

    /** 资源URL */
    private String resourceUrl;

    /** 资源类型：0-系统资源,1-普通资源 */
    private Integer resourceType;

    /** HTTP方法(GET,POST,DELETE,PUT等) */
    private String httpMethod;

    /** 功能类型：0-菜单，1-按钮 */
    private Integer actionType;

    /** 兄弟节点间的排序号,asc排序 */
    private Integer siblingsIndex;

    /** 资源菜单ICON(font-awesome类名) */
    private String resourceIcon;

    /** 是否是首页：1-是，0-否 */
    private Boolean indexPage;

    /** 所属应用ID */
    private Long appId;

    /** 创建时间 */
    private String createTime;

    /** 创建者,用户表id */
    private Long createBy;

    /** 更新时间 */
    private String updateTime;

    /** 更新者,用户表的id */
    private Long updateBy;
    
    //以下属于辅助字段
    
    private String parentResourceName;
    
    private String resourceTypeName;
    
    private String actionTypeName;
    
    private Integer resourceLevel;
    
    private String treeNodePath;
    
    private boolean inuse;
    
    private String appName;
    
    private Integer appResourceCount;
    
    private String appWebContextPath;
    
    private List<UpmsResource> subResourceList;

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Long getParentResourceId() {
        return parentResourceId;
    }

    public void setParentResourceId(Long parentResourceId) {
        this.parentResourceId = parentResourceId;
    }

    public Boolean getAppRootResource() {
        return appRootResource;
    }

    public void setAppRootResource(Boolean appRootResource) {
        this.appRootResource = appRootResource;
    }

    public String getPermissionExpression() {
        return permissionExpression;
    }

    public void setPermissionExpression(String permissionExpression) {
        this.permissionExpression = permissionExpression;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public Integer getSiblingsIndex() {
        return siblingsIndex;
    }

    public void setSiblingsIndex(Integer siblingsIndex) {
        this.siblingsIndex = siblingsIndex;
    }

    public String getResourceIcon() {
        return resourceIcon;
    }

    public void setResourceIcon(String resourceIcon) {
        this.resourceIcon = resourceIcon;
    }

    public Boolean getIndexPage() {
        return indexPage;
    }

    public void setIndexPage(Boolean indexPage) {
        this.indexPage = indexPage;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
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

	public String getParentResourceName() {
		return parentResourceName;
	}

	public void setParentResourceName(String parentResourceName) {
		this.parentResourceName = parentResourceName;
	}

	public String getResourceTypeName() {
		return resourceTypeName;
	}

	public void setResourceTypeName(String resourceTypeName) {
		this.resourceTypeName = resourceTypeName;
	}

	public String getActionTypeName() {
		return actionTypeName;
	}

	public void setActionTypeName(String actionTypeName) {
		this.actionTypeName = actionTypeName;
	}

	public Integer getResourceLevel() {
		return resourceLevel;
	}

	public void setResourceLevel(Integer resourceLevel) {
		this.resourceLevel = resourceLevel;
	}

	public String getTreeNodePath() {
		return treeNodePath;
	}

	public void setTreeNodePath(String treeNodePath) {
		this.treeNodePath = treeNodePath;
	}

	public boolean isInuse() {
		return inuse;
	}

	public void setInuse(boolean inuse) {
		this.inuse = inuse;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Integer getAppResourceCount() {
		return appResourceCount;
	}

	public void setAppResourceCount(Integer appResourceCount) {
		this.appResourceCount = appResourceCount;
	}

	public String getAppWebContextPath() {
		return appWebContextPath;
	}

	public void setAppWebContextPath(String appWebContextPath) {
		this.appWebContextPath = appWebContextPath;
	}

	public List<UpmsResource> getSubResourceList() {
		return subResourceList;
	}

	public void setSubResourceList(List<UpmsResource> subResourceList) {
		this.subResourceList = subResourceList;
	}

	@Override
	public UpmsResource decode() {
		if(resourceType != null){
			UpmsResourceTypeEnum em = UpmsResourceTypeEnum.getType(resourceType);
			if(em != null){
				setResourceTypeName(em.getTypeName());
			}
		}
		if(actionType != null){
			UpmsResourceActionTypeEnum em = UpmsResourceActionTypeEnum.getType(actionType);
			if(em != null){
				setActionTypeName(em.getTypeName());
			}
		}
		return this;
	}

	public int compareTo(UpmsResource o) {
		if(o == null){
			return -1;
		}
		if(this.siblingsIndex == null){
			return 1;
		}
		if(o.siblingsIndex == null){
			return -1;
		}
		return this.siblingsIndex - o.siblingsIndex;
	}
	
}