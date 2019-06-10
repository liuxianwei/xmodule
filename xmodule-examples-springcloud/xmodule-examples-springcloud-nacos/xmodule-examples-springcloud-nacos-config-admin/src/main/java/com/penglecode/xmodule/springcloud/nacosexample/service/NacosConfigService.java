package com.penglecode.xmodule.springcloud.nacosexample.service;

import java.io.StringReader;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.util.YamlPropsUtils;
import com.penglecode.xmodule.springcloud.nacosexample.config.NacosConfigProperties;

@Service("nacosConfigService")
public class NacosConfigService implements InitializingBean {

	public static final String DEFAULT_GROUP = "DEFAULT_GROUP";
	
	@Autowired
	private NacosConfigProperties nacosConfigProperties;
	
	private ConfigService configService;
	
	/**
	 * 发布配置(增量覆盖)
	 * @param dataId
	 * @param config
	 * @throws Exception
	 */
	public void publishConfig(String dataId, Map<String,Object> config) throws Exception {
		ValidationAssert.notEmpty(dataId, "配置参数dataId不能为空!");
		ValidationAssert.notEmpty(config, "配置内容config不能为空!");
		String group = DEFAULT_GROUP;
		String yamlConfigContent = configService.getConfig(dataId, group, nacosConfigProperties.getTimeout());
		//将yaml格式的配置文件转为properties格式的配置文件
		Map<String,Object> mapConfig = YamlPropsUtils.yaml2Map(yamlConfigContent);
		String propsConfigContent = YamlPropsUtils.map2Props(mapConfig);
		
		Properties propsConfig = new Properties();
		propsConfig.load(new StringReader(propsConfigContent));
		
		//合并/覆盖配置
		for(Map.Entry<String,Object> entry : config.entrySet()) {
			propsConfig.put(entry.getKey(), entry.getValue());
		}
		
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<Object,Object> entry : propsConfig.entrySet()) {
			sb.append(entry.getKey() + "=" + entry.getValue() + "\n");
		}
		
		String newPropsConfigContent = sb.toString();
		newPropsConfigContent = StringUtils.stripEnd(newPropsConfigContent, "\n");
		
		//properties配置转map
		Map<String,Object> newMapConfig = YamlPropsUtils.props2Map(newPropsConfigContent);
		//map转yaml
		String newYamlConfigContent = YamlPropsUtils.map2Yaml(newMapConfig);
		
		configService.publishConfig(dataId, group, newYamlConfigContent);
	}
	
	/**
	 * 发布配置(全量覆盖)
	 * @param dataId
	 * @param yamlContent
	 * @throws Exception
	 */
	public void publishConfig(String dataId, String yamlContent) throws Exception {
		ValidationAssert.notEmpty(dataId, "配置参数dataId不能为空!");
		ValidationAssert.notEmpty(yamlContent, "配置内容config不能为空!");
		String group = DEFAULT_GROUP;
		configService.publishConfig(dataId, group, yamlContent);
	}
	
	/**
	 * 获取配置内容
	 * @param dataId
	 * @return
	 * @throws Exception
	 */
	public String getConfig(String dataId) throws Exception {
		ValidationAssert.notEmpty(dataId, "配置参数dataId不能为空!");
		String group = DEFAULT_GROUP;
		return configService.getConfig(dataId, group, nacosConfigProperties.getTimeout());
	}
	
	protected ConfigService createConfigService() throws Exception {
		Properties properties = new Properties();
		properties.put("serverAddr", nacosConfigProperties.getServerAddr());
		return NacosFactory.createConfigService(properties);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		this.configService = createConfigService();
	}
	
}
