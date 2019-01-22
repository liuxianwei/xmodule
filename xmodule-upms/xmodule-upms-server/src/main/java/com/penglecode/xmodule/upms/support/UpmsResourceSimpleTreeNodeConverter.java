package com.penglecode.xmodule.upms.support;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.penglecode.xmodule.common.support.TreeNodeConverter;
import com.penglecode.xmodule.upms.model.UpmsResource;

/**
 * 基于ElementUI.Tree组件的资源节点树转换器
 * 
 * @author 	pengpeng
 * @date	2018年5月29日 下午6:05:29
 */
public class UpmsResourceSimpleTreeNodeConverter implements TreeNodeConverter<UpmsResource, Map<String,Object>> {

	public UpmsResourceSimpleTreeNodeConverter() {
		super();
	}

	public Map<String, Object> convertTreeNode(UpmsResource targetTreeNode) {
		Map<String,Object> treeNodeMap = new LinkedHashMap<String,Object>();
		treeNodeMap.put("id", targetTreeNode.getResourceId());
		treeNodeMap.put("label", targetTreeNode.getResourceName());
		treeNodeMap.put("actionType", targetTreeNode.getActionType());
		treeNodeMap.put("actionTypeName", targetTreeNode.getActionTypeName());
		treeNodeMap.put("resourceType", targetTreeNode.getResourceType());
		return treeNodeMap;
	}

	public void setSubTreeNodeList(Map<String, Object> resultTreeNode, List<Map<String, Object>> subTreeNodeList) {
		resultTreeNode.put("children", subTreeNodeList);
	}
	
}