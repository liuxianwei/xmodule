package com.penglecode.xmodule.upms.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.upms.model.UpmsUser;
import com.penglecode.xmodule.upms.web.security.authc.UpmsLoginUser;
import com.penglecode.xmodule.upms.web.security.util.UpmsUtils;

@RestController
@RequestMapping("/test/session")
public class UpmsSessionTestController extends HttpAPIResourceSupport {

	@GetMapping(value="/loginuser", produces=APPLICATION_JSON)
	public Result<UpmsUser> getLoginUser() {
		UpmsLoginUser loginUser = UpmsUtils.getCurrentLoginUser();
		return Result.success().message("OK").data(loginUser).build();
	}
	
}
