package com.penglecode.xmodule.common.boot.config;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.WebResourceRoot.ResourceSetType;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11Nio2Protocol;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.AbstractServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.util.ArrayUtils;

/**
 * Servlet容器普通配置(see web.xml)
 * 
 * @author pengpeng
 * @date 2018年2月3日 下午12:33:22
 */
@Configuration
@ConditionalOnClass({ Tomcat.class })
public class DefaultWebServerConfiguration extends AbstractSpringConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWebServerConfiguration.class);
	
	@Bean
	public AbstractServletWebServerFactory tomcatServletWebServerFactory() {
		TomcatServletWebServerFactory tomcatContainerFactory = new TomcatServletWebServerFactory();
		tomcatContainerFactory.setProtocol("org.apache.coyote.http11.Http11Nio2Protocol");
		tomcatContainerFactory.setUriEncoding(Charset.forName("UTF-8"));
		tomcatContainerFactory.addConnectorCustomizers(new CustomTomcatConnectorCustomizer());
		tomcatContainerFactory.addContextCustomizers(new CustomTomcatContextCustomizer());
		return tomcatContainerFactory;
	}

	class CustomTomcatContextCustomizer implements TomcatContextCustomizer {

		public void customize(Context context) {
			context.addLifecycleListener(new TomcatStaticResourceConfigurer(context));
			((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
		}

	}

	class CustomTomcatConnectorCustomizer implements TomcatConnectorCustomizer {

		@Override
		public void customize(Connector connector) {
			Http11Nio2Protocol protocol = (Http11Nio2Protocol) connector.getProtocolHandler();
			// 设置最大连接数
			protocol.setMaxConnections(2000);
			// 设置最大线程数
			protocol.setMaxThreads(1000);
			// 连接超时时间
			protocol.setConnectionTimeout(20000);
		}

	}

	class TomcatStaticResourceConfigurer implements LifecycleListener {

		private final Context context;

		public TomcatStaticResourceConfigurer(Context context) {
			super();
			this.context = context;
		}

		@Override
		public void lifecycleEvent(LifecycleEvent event) {
			if (event.getType().equals(Lifecycle.CONFIGURE_START_EVENT)) {
				try {
					Resource[] resources = ApplicationConstants.RESOURCE_PATTERN_RESOLVER.getResources("classpath*:/META-INF/resources/**");
					Set<String> metaInfoRootLocations = new LinkedHashSet<String>();
					if(!ArrayUtils.isEmpty(resources)) {
						for(Resource resource : resources) {
							metaInfoRootLocations.addAll(getMetaInfoRootLocations(resource));
						}
					}
					for(String location : metaInfoRootLocations) {
						try {
							//LOGGER.info(">>> location = " + location);
							this.context.getResources().createWebResourceSet(ResourceSetType.RESOURCE_JAR, "/", new URL(location), "/META-INF/resources");
						} catch (Exception e) {
							LOGGER.error("Create web resource set for [{}] failed : {}", location, e.getMessage());
						}
					}
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
		
		protected List<String> getMetaInfoRootLocations(Resource resource) {
			List<String> locations = new ArrayList<String>();
			try {
				URL url = resource.getURL();
				String location = url.toString();
				int index = location.indexOf("/META-INF/resources/");
				if(index != -1) {
					location = location.substring(0, index - 1);
					locations.add(location); //add jar in fat jar's lib dir
					
					index = location.indexOf("/BOOT-INF/lib/");
					if(index != -1) {
						location = location.substring(0, index + 1);
						locations.add(location); // add fat jar location
					}
				}
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
			return locations;
		}
		
	}
	
}
