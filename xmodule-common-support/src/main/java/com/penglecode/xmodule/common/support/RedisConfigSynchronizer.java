package com.penglecode.xmodule.common.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import com.penglecode.xmodule.common.redis.RedisException;
import com.penglecode.xmodule.common.redis.TopicBoundedMessageListener;

/**
 * 基于Redis的分布配置同步器
 * http://doc.redisfans.com/topic/notification.html
 * @param <T>
 * @author 	pengpeng
 * @date	2018年7月5日 下午3:34:15
 */
@SuppressWarnings("unchecked")
public abstract class RedisConfigSynchronizer<T> implements TopicBoundedMessageListener, DistributedConfigSynchronizer<T>, InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfigSynchronizer.class);
	
	private static final String TOPIC_PATTERN = "__keyspace@*__:%s";
	
	private String configRedisKey;
	
	private RedisTemplate<String,Object> redisTemplate;
	
	protected void init() throws Exception {
		Assert.hasText(configRedisKey, "Property 'configRedisKey' must be required!");
		Assert.notNull(redisTemplate, "Property 'redisTemplate' must be required!");
	}
	
	@Override
	public void refreshConfig() {
		try {
			publishConfig(getInitialConfig());
		} catch (Exception e) {
			throw new RedisException(e);
		}
	}

	@Override
	public void publishConfig(T value) {
		setRedisConfigValue(value);
	}

	@Override
	public void subscribeConfig() {
		//nothing to do
	}

	@Override
	public void updateLocalConfig() {
		try {
			T configValue = getRedisConfigValue();
			ConfigEvent configEvent = ConfigEvent.CREATED_OR_UPDATED;
			if(configValue == null) {
				configEvent = ConfigEvent.DELETED;
			}
			updateLocalConfig(true, configEvent, configValue);
		} catch (Exception e) {
			throw new RedisException(e);
		}
	}

	/**
	 * 订阅配置
	 */
	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			String messageTopic = (String) redisTemplate.getKeySerializer().deserialize(message.getChannel());
			String messageValue = (String) redisTemplate.getKeySerializer().deserialize(message.getBody());
			LOGGER.info("【subscribeConfig】>>> messageTopic = {}, messageValue = {}", messageTopic, messageValue);
			ConfigEvent configEvent = getConfigEventByRedisKeyOperation(messageValue);
			if(configEvent != null) {
				updateLocalConfig(true, configEvent, getRedisConfigValue());
			} else {
				LOGGER.warn(">>> Received an unexpected redis key operation: {} bound with key: {}", messageValue, configRedisKey);
			}
		} catch (Exception e) {
			throw new RedisException(e);
		}
	}
	
	/**
	 * 向redis中设置configRedisKey对应的值
	 * @param configValue
	 */
	protected void setRedisConfigValue(T configValue) {
		redisTemplate.opsForValue().set(configRedisKey, configValue);
	}
	
	/**
	 * 从redis中获取configRedisKey对应的值
	 * @return
	 */
	protected T getRedisConfigValue() {
		return (T) redisTemplate.opsForValue().get(configRedisKey);
	}
	
	protected ConfigEvent getConfigEventByRedisKeyOperation(String redisKeyOperation) {
		if("set".equals(redisKeyOperation)) {
			return ConfigEvent.CREATED_OR_UPDATED;
		} else if ("del".equals(redisKeyOperation) || "expired".equals(redisKeyOperation)) {
			return ConfigEvent.DELETED;
		} else {
			return null;
		}
	}
	
	/**
	 * 根据configRedisKey获取TopicPattern
	 * @return
	 */
	public String getBoundedMessageTopic() {
		return String.format(TOPIC_PATTERN, configRedisKey);
	}
	
	/**
	 * 更新本地配置
	 * @param configChanged
	 * @param configEvent
	 * @param configValue
	 * @throws Exception
	 */
	protected abstract void updateLocalConfig(boolean configChanged, ConfigEvent configEvent, T configValue) throws Exception;
	
	/**
	 * 获取初始化的配置(例如从数据库获取初始化配置)
	 * @return
	 * @throws Exception
	 */
	protected abstract T getInitialConfig() throws Exception;

	public String getConfigRedisKey() {
		return configRedisKey;
	}

	public void setConfigRedisKey(String configRedisKey) {
		this.configRedisKey = configRedisKey;
	}

	protected RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
		subscribeConfig();
		refreshConfig();
		updateLocalConfig(); //手动更新一次,解决RedisMessageListenerContainer迟延订阅而导致前面的refreshConfig()触发不到onMessage的问题
	}

}
