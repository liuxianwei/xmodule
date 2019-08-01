package com.penglecode.xmodule.myexample.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

@RestController
public class SyncAxiosExampleController extends HttpAPIResourceSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(SyncAxiosExampleController.class);
	
	@GetMapping(value="/api/syncaxios/first", produces=APPLICATION_JSON)
	public Object getFirst() throws Exception {
		LOGGER.info(">>> getFirst");
		Thread.sleep(10000);
		LOGGER.info("<<< getFirst");
		return Result.success().message("OK").data(System.getProperty("os.name")).build();
	}
	
	@GetMapping(value="/api/syncaxios/second", produces=APPLICATION_JSON)
	public Object getSecond() throws Exception {
		LOGGER.info(">>> getSecond");
		Thread.sleep(30000);
		LOGGER.info("<<< getSecond");
		return Result.success().message("OK").data(System.getProperty("user.home")).build();
	}
	
}
