package com.penglecode.xmodule.common.boot.config;

import java.nio.charset.CodingErrorAction;

import org.apache.http.Consts;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate自定义配置
 * 
 * @author 	pengpeng
 * @date	2018年9月4日 上午10:21:47
 */
@Configuration
@ConditionalOnClass(RestTemplate.class)
public class DefaultRestTemplateConfiguration extends AbstractSpringConfiguration {

	@Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
		try {
			PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
			connectionManager.setMaxTotal(200);
			connectionManager.setDefaultMaxPerRoute(20);
			connectionManager.setDefaultConnectionConfig(ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE).setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8).build());
			
			HttpClient httpClient = HttpClients.custom()
				.setConnectionManager(connectionManager)
				.setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(15000).setSocketTimeout(15000).setConnectionRequestTimeout(6000).build()).build();
			return new HttpComponentsClientHttpRequestFactory(httpClient);
		} catch (Exception e) {
			throw new BeanInstantiationException(HttpComponentsClientHttpRequestFactory.class, "Error create bean HttpComponentsClientHttpRequestFactory!");
		}
    }
	
	@Bean
	public RestTemplateCustomizer restTemplateCustomizer() {
		return new RestTemplateCustomizer() {
			public void customize(RestTemplate restTemplate) {
				restTemplate.setRequestFactory(clientHttpRequestFactory());
			}
		};
	}
	
}
