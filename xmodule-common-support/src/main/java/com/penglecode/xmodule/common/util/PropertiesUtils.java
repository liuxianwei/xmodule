package com.penglecode.xmodule.common.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

/**
 * Properties属性文件操作工具类
 * 
 * @author 	pengpeng
 * @date	2018年11月9日 下午4:48:51
 */
public class PropertiesUtils extends PropertiesLoaderUtils {

	private static final PropertiesPersister defaultPropertiesPersister = new DefaultPropertiesPersister();
	
	/**
	 * 保存properties数据到指定的properties文件
	 * @param properties
	 * @param propsFile			
	 */
	public static void storeProperties(Properties properties, File propsFile) throws IOException {
		storeProperties(properties, propsFile, false);
	}
	
	/**
	 * 保存properties数据到指定的properties文件
	 * @param properties
	 * @param propsFile			
	 * @param mergeProps		- 如果已经存在properties文件则是否两者合并?
	 * @throws IOException
	 */
	public static void storeProperties(Properties properties, File propsFile, boolean mergeProps) throws IOException {
		Assert.notNull(properties, "Property 'properties' can not be null!");
		Assert.notNull(propsFile, "Property 'propsFile' can not be null!");
		if(!propsFile.exists()) {
			Files.createFile(Paths.get(propsFile.getPath()));
		} else {
			if(mergeProps) {
				Properties oldProperties = loadProperties(new FileSystemResource(propsFile));
				oldProperties.putAll(properties);
				properties = oldProperties;
			}
		}
		storeProperties(properties, propsFile, defaultPropertiesPersister);
	}
	
	static void storeProperties(Properties properties, File propFile, PropertiesPersister persister)
			throws IOException {
		OutputStream stream = null;
		try {
			stream = FileUtils.openOutputStream(propFile);
			persister.store(properties, stream, "");
		}
		finally {
			if (stream != null) {
				stream.close();
			}
		}
	}
	
}
