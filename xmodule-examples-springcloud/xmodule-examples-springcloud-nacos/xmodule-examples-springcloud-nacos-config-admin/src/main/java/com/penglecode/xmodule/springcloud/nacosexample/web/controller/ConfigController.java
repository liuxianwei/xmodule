package com.penglecode.xmodule.springcloud.nacosexample.web.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.consts.ContentType;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.springcloud.nacosexample.service.NacosConfigService;

@RestController
@RequestMapping("/api/config")
public class ConfigController extends HttpAPIResourceSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigController.class);
	
	@Autowired
	private NacosConfigService nacosConfigService;
	
	@PutMapping(value="/publish/{dataId}", consumes=ContentType.APPLICATION_JSON, produces=ContentType.APPLICATION_JSON)
	public Result<Object> publishConfig(@PathVariable("dataId") String dataId, @RequestBody Map<String,Object> config) throws Exception {
		LOGGER.info(">>> prepare to publish config : {}", config);
		nacosConfigService.publishConfig(dataId, config);
		return Result.success().message("OK").build();
	}
	
	@PostMapping(value="/publish/{dataId}", consumes=ContentType.APPLICATION_YAML, produces=ContentType.APPLICATION_JSON)
	public Result<Object> publishConfig(@PathVariable("dataId") String dataId, @RequestBody String yamlContent) throws Exception {
		LOGGER.info(">>> prepare to publish config : {}", yamlContent);
		nacosConfigService.publishConfig(dataId, yamlContent);
		return Result.success().message("OK").build();
	}
	
	@GetMapping(value="/{dataId}", produces=ContentType.APPLICATION_JSON)
	public Result<Object> getConfig(@PathVariable("dataId") String dataId) throws Exception {
		LOGGER.info(">>> prepare to get config : {}", dataId);
		String yamlContent = nacosConfigService.getConfig(dataId);
		return Result.success().message("OK").data(yamlContent).build();
	}
	
}
