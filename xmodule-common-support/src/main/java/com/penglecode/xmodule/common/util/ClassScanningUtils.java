package com.penglecode.xmodule.common.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.Assert;

/**
 * 搜索class的工具类
 * 
 * @author 	pengpeng
 * @date	2019年2月23日 下午1:54:42
 */
@SuppressWarnings("all")
public class ClassScanningUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClassScanningUtils.class);
	
	private static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();
	
	private static final MetadataReaderFactory METADATA_READER_FACTORY = new CachingMetadataReaderFactory(RESOURCE_PATTERN_RESOLVER); 
	
	/**
	 * 根据多个包名搜索class的类名
	 * 例如: ClassScanningUtils.scanPakcages("com.xxx.**.*");
	 * 
	 * @param basePackages 包名(可以有通配符)
	 * @return 类名的集合
	 */
	public static Set<String> scanPackageClassNames(String... basePackages) {
		Assert.notEmpty(basePackages,"'basePakcages' must be not empty");
		
		Set<String> classNames = new LinkedHashSet<String>();
		try {
			for(String packageToScan : basePackages) {
				String packagePart = packageToScan.replace('.', '/');
				String classPattern = "classpath*:/" + packagePart + "/**/*.class";
				Resource[] resources = RESOURCE_PATTERN_RESOLVER.getResources(classPattern);
				for (int i = 0; i < resources.length; i++) {
					Resource resource = resources[i];
					MetadataReader metadataReader = METADATA_READER_FACTORY.getMetadataReader(resource);   
					String className = metadataReader.getClassMetadata().getClassName();
					classNames.add(className);
				}
			}
		} catch(IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return classNames;
	}
	
	/**
	 * 根据多个包名搜索class类
	 * 例如: ClassScanningUtils.scanPakcages("com.xxx.**.*");
	 * (注意：该方法忽略java.lang.NoClassDefFoundError错误)
	 * @param basePackages 包名(可以有通配符)
	 * @return 类的集合
	 */
	public static Set<Class<?>> scanPackageClasses(String... basePackages) {
		Set<String> classNames = scanPackageClassNames(basePackages);
		if(!CollectionUtils.isEmpty(classNames)) {
			return classNames.stream().map(ClassScanningUtils::classForName).filter(Objects::nonNull).collect(Collectors.toSet());
		}
		return Collections.EMPTY_SET;
	}
	
	/**
	 * 根据多个包名搜索class类
	 * 例如: ClassScanningUtils.scanPakcages("com.xxx.**.*");
	 * (注意：该方法忽略java.lang.NoClassDefFoundError错误)
	 * @param basePackages 包名(可以有通配符)
	 * @return 类的集合
	 */
	public static <T> Set<Class<? extends T>> scanPackageSubClasses(Class<T> superType, String... basePackages) {
		Assert.notNull(superType, "Parameter 'superType' can not be null!");
		Set<String> classNames = scanPackageClassNames(basePackages);
		if(!CollectionUtils.isEmpty(classNames)) {
			return classNames.stream().map(ClassScanningUtils::classForName).filter(clazz -> {
				return clazz != null && superType.isAssignableFrom(clazz);
			}).collect(Collectors.toCollection(LinkedHashSet<Class<? extends T>>::new));
		}
		return Collections.EMPTY_SET;
	}
	
	/**
	 * 根据多个包名搜索class类
	 * 例如: ClassScanningUtils.scanPakcages("com.xxx.**.*");
	 * (注意：该方法忽略java.lang.NoClassDefFoundError错误)
	 * @param basePackages 包名(可以有通配符)
	 * @return 类的集合
	 */
	public static Set<Class<?>> scanPackageAnnotatedClasses(Class<? extends Annotation> classAnnotation, String... basePackages) {
		Assert.notNull(classAnnotation, "Parameter 'classAnnotation' can not be null!");
		Set<String> classNames = scanPackageClassNames(basePackages);
		if(!CollectionUtils.isEmpty(classNames)) {
			return classNames.stream().map(ClassScanningUtils::classForName).filter(clazz -> {
				return clazz != null && AnnotationUtils.findAnnotation(clazz, classAnnotation) != null;
			}).collect(Collectors.toSet());
		}
		return Collections.EMPTY_SET;
	}
	
	protected static <T> Class<T> classForName(String className) {
		try {
			return (Class<T>) ClassUtils.forName(className);
		} catch (Throwable e) {
			LOGGER.error(e.getClass() + " : " + e.getMessage());
		}
		return null;
	}
	
}
