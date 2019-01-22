package com.penglecode.xmodule.upms.support;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.penglecode.xmodule.common.support.TreeNodeConverter;
import com.penglecode.xmodule.upms.model.UpmsResource;

/**
 * 基于ElementUI.NavMenu组件的资源菜单转换器
 * 
 * @author	  	pengpeng
 * @date	  	2015年11月15日 下午8:03:51
 * @version  	1.0
 */
public class UpmsResourceNavMenuNodeConverter implements TreeNodeConverter<UpmsResource, Map<String,Object>> {

	public UpmsResourceNavMenuNodeConverter() {
		super();
	}

	public Map<String, Object> convertTreeNode(UpmsResource targetTreeNode) {
		Map<String,Object> treeNodeMap = new LinkedHashMap<String,Object>();
		treeNodeMap.put("menuId", targetTreeNode.getResourceId());
		treeNodeMap.put("menuName", targetTreeNode.getResourceName());
		treeNodeMap.put("menuUrl", targetTreeNode.getResourceUrl());
		treeNodeMap.put("menuIcon", targetTreeNode.getResourceIcon());
		treeNodeMap.put("menuLevel", targetTreeNode.getResourceLevel());
		treeNodeMap.put("menuPath", targetTreeNode.getTreeNodePath());
		treeNodeMap.put("appWebContextPath", targetTreeNode.getAppWebContextPath());
		treeNodeMap.put("parentMenuId", targetTreeNode.getParentResourceId());
		return treeNodeMap;
	}

	public void setSubTreeNodeList(Map<String, Object> resultTreeNode, List<Map<String, Object>> subTreeNodeList) {
		resultTreeNode.put("subMenuList", subTreeNodeList);
	}
	
}