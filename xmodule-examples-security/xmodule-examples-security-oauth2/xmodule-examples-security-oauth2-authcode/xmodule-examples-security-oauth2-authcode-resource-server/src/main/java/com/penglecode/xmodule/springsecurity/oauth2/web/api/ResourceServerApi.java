package com.penglecode.xmodule.springsecurity.oauth2.web.api;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import java.time.Instant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;

@RestController
@RequestMapping("/api/server")
public class ResourceServerApi extends HttpAPIResourceSupport {

	@GetMapping(value="/nowtime", produces=APPLICATION_JSON)
	public Result<String> getServerNowTime() {
		return Result.success().message("OK").data(Instant.now().toString()).build();
	}
	
}
