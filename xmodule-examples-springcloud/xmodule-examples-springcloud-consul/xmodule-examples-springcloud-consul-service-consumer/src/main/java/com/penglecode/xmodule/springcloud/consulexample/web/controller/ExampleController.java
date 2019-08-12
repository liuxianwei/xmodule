package com.penglecode.xmodule.springcloud.consulexample.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.PageResult;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

@RestController
public class ExampleController extends HttpAPIResourceSupport {

	@GetMapping(value="/api/hello", produces=APPLICATION_JSON)
	public Object hello() {
		return "hello";
	}
	
	@GetMapping(value="/api/now", produces=APPLICATION_JSON)
	public Object getNowTime() {
		return Result.success().message("OK").data(DateTimeUtils.formatNow()).build();
	}
	
	@GetMapping(value="/api/props", produces=APPLICATION_JSON)
	public Result<Object> getProps() {
		return Result.success().message("OK").data(System.getProperties()).build();
	}
	
	@GetMapping(value="/api/envs", produces=APPLICATION_JSON)
	public PageResult<List<Map<String,Object>>> getEnvs() {
		Map<String,String> props = System.getenv();
		List<Map<String,Object>> dataList = new ArrayList<>();
		for(Map.Entry<String,String> entry : props.entrySet()) {
			dataList.add(Collections.singletonMap(entry.getKey(), entry.getValue()));
		}
		return PageResult.success().message("OK").data(dataList).totalRowCount(dataList.size()).build();
	}
	
	@GetMapping(value="/api/random/error", produces=APPLICATION_JSON)
	public Result<Object> randomErrors(String code) {
		code = StringUtils.defaultIfEmpty(code, "200");
		int statusCode = Integer.valueOf(code);
		return Result.success().message("OK").code(statusCode).build();
	}
	
}
