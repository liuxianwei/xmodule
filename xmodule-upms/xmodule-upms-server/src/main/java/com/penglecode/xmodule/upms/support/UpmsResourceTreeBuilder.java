package com.penglecode.xmodule.upms.support;

import java.util.List;

import com.penglecode.xmodule.common.support.AbstractXTreeBuilder;
import com.penglecode.xmodule.upms.model.UpmsResource;

/**
 * 资源树结构builder
 * 
 * @author	  	pengpeng
 * @date	  	2015年11月15日 下午8:04:19
 * @version  	1.0
 */
public class UpmsResourceTreeBuilder extends AbstractXTreeBuilder<Long, UpmsResource> {

	protected Long getParentTreeNodeId(UpmsResource treeObj) {
		return treeObj.getParentResourceId();
	}

	protected Long getTreeNodeId(UpmsResource treeObj) {
		return treeObj.getResourceId();
	}

	protected void setSubTreeNodeList(UpmsResource treeObj, List<UpmsResource> directChildList) {
		treeObj.setSubResourceList(directChildList);
	}

	protected List<UpmsResource> getSubTreeNodeList(UpmsResource treeObj) {
		return treeObj.getSubResourceList();
	}

	protected void setTreeNodeLevel(UpmsResource treeObj, Integer level) {
		treeObj.setResourceLevel(level);
	}

	protected void setTreeNodePath(UpmsResource treeNode, String path) {
		treeNode.setTreeNodePath(path);
	}

	protected String getTreeNodePath(UpmsResource treeNode) {
		return treeNode.getTreeNodePath();
	}

}
