package com.penglecode.xmodule.myexample.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

@RestController
@RequestMapping("/api")
public class ConsumerExampleController extends HttpAPIResourceSupport {

	private static final String PROVIDER_SERVICE_NAME = "XMODULE-EXAMPLES-EUREKA-SERVICE-PROVIDER";
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value="/now", method=GET, produces=APPLICATION_JSON)
	public Object nowTime(HttpServletRequest request, HttpServletResponse response) {
		String url = "http://" + PROVIDER_SERVICE_NAME + "/api/now";
		return restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Result<Object>>() {});
	}
	
	@RequestMapping(value="/java/envs", method=GET, produces=APPLICATION_JSON)
	public Object getJavaEnvs() throws Exception {
		String url = "http://" + PROVIDER_SERVICE_NAME + "/api/java/envs";
		return restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Result<Object>>() {});
	}
	
	@RequestMapping(value="/java/props", method=GET, produces=APPLICATION_JSON)
	public Object getJavaProps() throws Exception {
		String url = "http://" + PROVIDER_SERVICE_NAME + "/api/java/props";
		return restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Result<Object>>() {});
	}
	
}
