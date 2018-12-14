package com.penglecode.xmodule.myexample.web.rest;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;
import static com.penglecode.xmodule.common.consts.ContentType.TEXT_HTML;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.common.web.support.HttpAccessLogging;
import com.penglecode.xmodule.myexample.model.User;

@Component
@Path("/api/example2")
public class Example2Rest extends HttpAPIResourceSupport {

	@GET
	@Path("/nowtime")
	@Consumes(TEXT_HTML)
	@Produces(APPLICATION_JSON)
	public Object getJavaProps() throws Exception {
		return Result.success().message("OK").data(DateTimeUtils.formatNow()).build();
	}
	
	@GET
	@Path("/app_{appId}/user/{userId}")
	@Consumes(TEXT_HTML)
	@Produces(APPLICATION_JSON)
	@HttpAccessLogging(title="GET_APP_USER")
	public Object getAppUser(@PathParam("appId")String appId, @PathParam("userId")String userId) throws Exception {
		return Result.success().message("OK").data("(" + appId + "," + userId + ")").build();
	}
	
	@POST
	@Path("/user/login")
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@HttpAccessLogging(title="USER_LOGIN")
	public Object userLogin(User user) throws Exception {
		return Result.success().message("OK").data(user).build();
	}
	
}
