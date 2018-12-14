package com.penglecode.xmodule.springsecurity.upms.web.controller;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.PageResult;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.springsecurity.upms.model.UpmsUser;
import com.penglecode.xmodule.springsecurity.upms.service.UpmsUserService;

@RestController
public class UpmsUserController extends HttpAPIResourceSupport {

	@Resource(name="upmsUserService")
	private UpmsUserService upmsUserService;
	
	@GetMapping(value="/user/list", produces=APPLICATION_JSON)
	public PageResult<List<UpmsUser>> getUserListByPage(UpmsUser condition, Page page, Sort sort) {
		List<UpmsUser> dataList = upmsUserService.getUserListByPage(condition, page, sort);
		return PageResult.success().message("OK").data(dataList).totalRowCount(page.getTotalRowCount()).build();
	}
	
}
