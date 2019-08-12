package com.penglecode.xmodule.springcloud.nacosexample.web.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.cloud.consul.ConsulConfigService;
import com.penglecode.xmodule.common.consts.ContentType;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

@RestController
@RequestMapping("/api/config")
public class ConsulConfigController extends HttpAPIResourceSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConsulConfigController.class);
	
	@Autowired
	private ConsulConfigService consulConfigService;
	
	@PutMapping(value="/publish/{springAppName}/{profile}", consumes=ContentType.APPLICATION_JSON, produces=ContentType.APPLICATION_JSON)
	public Result<Object> publishConfig(@PathVariable("springAppName") String springAppName, @PathVariable("profile") String profile, @RequestBody Map<String,Object> config) throws Exception {
		LOGGER.info(">>> prepare to publish config, {} = {}", profile, config);
		consulConfigService.publishConfig(springAppName, profile, config);
		return Result.success().message("OK").build();
	}
	
	@PostMapping(value="/publish/{springAppName}/{profile}", consumes=ContentType.APPLICATION_YAML, produces=ContentType.APPLICATION_JSON)
	public Result<Object> publishConfig(@PathVariable("springAppName") String springAppName, @PathVariable("profile") String profile, @RequestBody String yamlContent) throws Exception {
		LOGGER.info(">>> prepare to publish config, {} = {}", profile, yamlContent);
		consulConfigService.publishConfig(springAppName, profile, yamlContent);
		return Result.success().message("OK").build();
	}
	
	@GetMapping(value="/{springAppName}/{profile}", produces=ContentType.APPLICATION_JSON)
	public Result<Object> getTextConfig(@PathVariable("springAppName") String springAppName, @PathVariable("profile") String profile) throws Exception {
		LOGGER.info(">>> prepare to get config : {}", profile);
		String yamlContent = consulConfigService.getTextConfig(springAppName, profile);
		return Result.success().message("OK").data(yamlContent).build();
	}
	
	@DeleteMapping(value="/{springAppName}/{profile}", produces=ContentType.APPLICATION_JSON)
	public Result<Object> deleteConfig(@PathVariable("springAppName") String springAppName, @PathVariable("profile") String profile) throws Exception {
		LOGGER.info(">>> prepare to delete config : {}", profile);
		consulConfigService.deleteConfig(springAppName, profile);
		return Result.success().message("OK").build();
	}
	
}
