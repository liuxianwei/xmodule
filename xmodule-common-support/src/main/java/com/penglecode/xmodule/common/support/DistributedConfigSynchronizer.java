package com.penglecode.xmodule.common.support;

/**
 * 分布式配置同步器
 * 例如通过Zookeeper来实现
 * 
 * @author 	pengpeng
 * @date	2018年6月28日 下午3:32:20
 */
public interface DistributedConfigSynchronizer<T> {

	/**
     * 刷新配置(例如从数据库中读取，并写入Zookeeper/Redis中去)然后发出通知
     */
    public void refreshConfig();
	
	/**
	 * 发布通知所有订阅者重新加配置
	 * @return
	 */
	public void publishConfig(T value);
	
	/**
	 * 订阅重新加载配置的通知
	 * @return
	 */
	public void subscribeConfig();
	
	/**
     * 主动从远程(例如Zookeeper)重新加载配置并强制更新到本地
     */
    public void updateLocalConfig();
    
    public static enum ConfigEvent {
		
		CREATED_OR_UPDATED, DELETED
		
	}
    
}
