package com.penglecode.xmodule.common.support;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.zookeeper.ZooKeeperException;

/**
 * 基于Zookeeper的分布式配置同步器
 * 
 * @author 	pengpeng
 * @date	2018年6月28日 下午4:09:17
 */
public abstract class ZooKeeperConfigSynchronizer implements DistributedConfigSynchronizer<byte[]>, InitializingBean , DisposableBean {

	public static final String DEFAULT_CHARSET = GlobalConstants.DEFAULT_CHARSET;
	
	private static final String DEFAULT_CONST_ZK_ROOT_PATH = "/xconfiguration";
	
	private CuratorFramework zooKeeperClient;
	
	private NodeCache configNodeCache;
	
	private String configNodeRootPath = DEFAULT_CONST_ZK_ROOT_PATH;
	
	private String configNodePath;
	
	private String configNodeFullPath;
	
	private volatile Integer configVersion;
	
	protected void init() throws Exception {
		Assert.hasText(configNodePath, "Property 'configNodePath' must be required!");
		Assert.notNull(zooKeeperClient, "Property 'zooKeeperClient' must be required!");
		initConfigNodePath();
		initConfigNodeCache();
	}
	
	protected void initConfigNodePath() {
		try {
			this.configNodeFullPath = StringUtils.stripEnd(configNodeRootPath + configNodePath, "/");
			createConfigNodePathIfNecessary(configNodeFullPath, null); //初始化配置节点
		} catch (Exception e) {
			throw new ZooKeeperException(e);
		}
	}
	
	protected void createConfigNodePathIfNecessary(String path, byte[] data) throws Exception  {
		String zkNodePath = path;
		if(!path.endsWith("/")) {
			zkNodePath += "/";
		}
		int index = -1;
		int start = 1;
		while((index = zkNodePath.indexOf('/', start)) != -1) {
			String subPath = zkNodePath.substring(0, index);
			start = subPath.length() + 1;
			
			Stat stat = null;
			stat = zooKeeperClient.checkExists().forPath(subPath);
			if(stat == null) {
				zooKeeperClient.create()
				  .withMode(CreateMode.PERSISTENT)
				  .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
				  .forPath(subPath, data);
			}
		}
	}
	
	@Override
	public void refreshConfig() {
		try {
			publishConfig(getInitialConfig());
		} catch (Exception e) {
			throw new ZooKeeperException(e);
		}
	}

	@Override
	public void publishConfig(byte[] value) {
		try {
			Stat stat = null;
			stat = zooKeeperClient.checkExists().forPath(configNodeFullPath);
			if(stat == null) {
				zooKeeperClient.create()
				  .withMode(CreateMode.PERSISTENT)
				  .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
				  .forPath(configNodeFullPath, value);
			} else {
				zooKeeperClient.setData().forPath(configNodeFullPath, value);
			}
		} catch (Exception e) {
			throw new ZooKeeperException(e);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void subscribeConfig() {
		NodeCacheListener listener = new NodeCacheListener() {
			public void nodeChanged() throws Exception {
				//String configPath = configNodeCache.getPath();
				ChildData data = configNodeCache.getCurrentData();
				byte[] configValue = null;
				boolean configVersionChanged = true;
				Integer dataVersion = null;
				ConfigEvent configEvent = null;
				if(data == null) { //deleted
					configVersionChanged = true;
					configEvent = ConfigEvent.DELETED;
				} else { //created/updated
					configValue = data.getData();
					dataVersion = data.getStat().getVersion();
					configVersionChanged = dataVersion == null || configVersion == null || !dataVersion.equals(configVersion);
					configEvent = ConfigEvent.CREATED_OR_UPDATED;
				}
				updateLocalConfig(configVersionChanged, configEvent, configValue);
				configVersion = dataVersion;
			}
		};
		configNodeCache.getListenable().addListener(listener);
	}

	@Override
	public void updateLocalConfig() {
		try {
			Stat stat = new Stat();
			byte[] configData = null;
			Integer dataVersion = null;
			ConfigEvent configEvent = ConfigEvent.CREATED_OR_UPDATED;
			try {
				zooKeeperClient.getData().storingStatIn(stat).forPath(configNodeFullPath);
				dataVersion = stat.getVersion();
			} catch (KeeperException.NoNodeException ex) {
				configEvent = ConfigEvent.DELETED;
			}
			configVersion = dataVersion;
			updateLocalConfig(true, configEvent, configData);
		} catch (Exception e) {
			throw new ZooKeeperException(e);
		}
	}

	/**
	 * 更新本地配置
	 * (注意当节点被删除时,即配置被删除时,入参configValue为null)
	 * @param configChanged
	 * @param configEvent
	 * @param configValue
	 * @throws Exception
	 */
	protected abstract void updateLocalConfig(boolean configChanged, ConfigEvent configEvent, byte[] configValue) throws Exception;
	
	/**
	 * 获取初始化的配置(例如从数据库获取初始化配置)
	 * @return
	 * @throws Exception
	 */
	protected abstract byte[] getInitialConfig() throws Exception;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		init();
		subscribeConfig();
		refreshConfig();
	}

	@Override
	public void destroy() throws Exception {
		destroyConfigNodeCache();
	}

	protected CuratorFramework getZooKeeperClient() {
		return zooKeeperClient;
	}

	public void setZooKeeperClient(CuratorFramework zooKeeperClient) {
		this.zooKeeperClient = zooKeeperClient;
	}

	protected NodeCache getConfigNodeCache() {
		return configNodeCache;
	}

	protected void initConfigNodeCache() throws Exception {
		configNodeCache = new NodeCache(zooKeeperClient, configNodeFullPath, false);
		configNodeCache.start();
	}
	
	protected void destroyConfigNodeCache() throws Exception {
		configNodeCache.close();
	}

	public String getConfigNodeRootPath() {
		return configNodeRootPath;
	}

	public void setConfigNodeRootPath(String configNodeRootPath) {
		if(configNodeRootPath != null && !configNodeRootPath.startsWith("/")) {
			configNodeRootPath = "/" + configNodeRootPath;
		}
		this.configNodeRootPath = configNodeRootPath;
	}

	public String getConfigNodePath() {
		return configNodePath;
	}

	public void setConfigNodePath(String configNodePath) {
		if(configNodePath != null && !configNodePath.startsWith("/")) {
			configNodePath = "/" + configNodePath;
		}
		this.configNodePath = configNodePath;
	}

	public String getConfigNodeFullPath() {
		return configNodeFullPath;
	}
	
}
