package com.penglecode.xmodule.common.util;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.Assert;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.penglecode.xmodule.common.consts.GlobalConstants;

/**
 * 一致性hash算法工具类
 * 
 * @author 	pengpeng
 * @date	2019年8月16日 上午10:11:54
 */
public class ConsistentHashUtils {

	/**
	 * 通过一致性算法选择一个由给定key决定的命中节点
	 * @param key
	 * @param nodes
	 * @return
	 */
	public static <T> T chooseNode(String key, List<T> nodes) {
		Assert.hasText(key, "Parameter 'key' must be required!");
		int prime = 31; //always used in hashcode method
		return chooseNode(Hashing.murmur3_128(prime).hashString(key, Charset.forName(GlobalConstants.DEFAULT_CHARSET)), nodes);
	}
	
	/**
	 * 通过一致性算法获取由给定key决定的命中节点
	 * @param key
	 * @param nodes
	 * @return
	 */
	public static <T> T chooseNode(HashCode keyHash, List<T> nodes) {
		Assert.notNull(keyHash, "Parameter 'keyHash' must be required!");
		if(!CollectionUtils.isEmpty(nodes)) {
			final List<T> nodeList = new ArrayList<T>(nodes);
			int hitIndex = Hashing.consistentHash(keyHash, nodeList.size());
			return nodes.get(hitIndex);
		}
		return null;
	}

	public static void main(String[] args) {
		List<Node> nodes = new ArrayList<Node>();
		
		nodes.add(new Node("172.16.96.18", 8500));
		nodes.add(new Node("172.16.96.19", 8500));
		nodes.add(new Node("172.16.96.20", 8500));
		nodes.add(new Node("172.16.96.21", 8500));
		nodes.add(new Node("172.16.96.57", 8500));
		
		List<String> clientIds = Arrays.asList("172.16.18.170", "172.16.18.171", "172.16.18.172", "172.16.18.173", "172.16.18.174", "172.16.18.175", "172.16.18.176", "172.16.18.177", "172.16.18.178");
		
		Map<String,Set<Node>> hitNodesOfClient = new LinkedHashMap<String,Set<Node>>();
		
		int tests = 1000;
		
		for(String clientId : clientIds) {
			for(int i = 0; i < tests; i++) {
				Node hitNode = chooseNode(clientId, nodes);
				Set<Node> hitNodes = hitNodesOfClient.get(clientId);
				if(hitNodes == null) {
					hitNodes = new HashSet<Node>();
					hitNodesOfClient.put(clientId, hitNodes);
				}
				hitNodes.add(hitNode);
			}
		}
		
		for(Map.Entry<String,Set<Node>> entry : hitNodesOfClient.entrySet()) {
			System.out.println(">>> " + entry.getKey() + " = " + entry.getValue());
		}
	}
	
	public static class Node {
		
		private final String host;
		
		private final int port;

		public Node(String host, int port) {
			super();
			this.host = host;
			this.port = port;
		}

		public String getHost() {
			return host;
		}

		public int getPort() {
			return port;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((host == null) ? 0 : host.hashCode());
			result = prime * result + port;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Node other = (Node) obj;
			if (host == null) {
				if (other.host != null)
					return false;
			} else if (!host.equals(other.host))
				return false;
			if (port != other.port)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Node [host=" + host + ", port=" + port + "]";
		}
		
	}
	
}
