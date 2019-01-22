package com.penglecode.xmodule.upms.support;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.penglecode.xmodule.common.support.TreeNodeConverter;
import com.penglecode.xmodule.upms.model.UpmsResource;

/**
 * 基于ElementUI.Tree组件的资源节点树转换器
 * 
 * @author	  	pengpeng
 * @date	  	2015年11月15日 下午8:03:51
 * @version  	1.0
 */
public class UpmsResourceTreeNodeConverter implements TreeNodeConverter<UpmsResource, Map<String,Object>> {

	public UpmsResourceTreeNodeConverter() {
		super();
	}

	public Map<String, Object> convertTreeNode(UpmsResource targetTreeNode) {
		Map<String,Object> treeNodeMap = new LinkedHashMap<String,Object>();
		treeNodeMap.put("id", targetTreeNode.getResourceId());
		treeNodeMap.put("label", targetTreeNode.getResourceName());
		treeNodeMap.put("parentResourceId", targetTreeNode.getParentResourceId());
		treeNodeMap.put("parentResourceName", targetTreeNode.getParentResourceName());
		treeNodeMap.put("siblingsIndex", targetTreeNode.getSiblingsIndex());
		treeNodeMap.put("actionType", targetTreeNode.getActionType());
		treeNodeMap.put("actionTypeName", targetTreeNode.getActionTypeName());
		treeNodeMap.put("resourceUrl", targetTreeNode.getResourceUrl());
		treeNodeMap.put("resourceType", targetTreeNode.getResourceType());
		treeNodeMap.put("resourceTypeName", targetTreeNode.getResourceTypeName());
		treeNodeMap.put("resourceIcon", targetTreeNode.getResourceIcon());
		treeNodeMap.put("permissionExpression", targetTreeNode.getPermissionExpression());
		treeNodeMap.put("appId", targetTreeNode.getAppId());
		treeNodeMap.put("appName", targetTreeNode.getAppName());
		treeNodeMap.put("indexPage", targetTreeNode.getIndexPage());
		treeNodeMap.put("inuse", targetTreeNode.isInuse());
		return treeNodeMap;
	}

	public void setSubTreeNodeList(Map<String, Object> resultTreeNode, List<Map<String, Object>> subTreeNodeList) {
		resultTreeNode.put("children", subTreeNodeList);
	}
	
}