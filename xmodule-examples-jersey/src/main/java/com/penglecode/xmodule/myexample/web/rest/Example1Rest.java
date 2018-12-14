package com.penglecode.xmodule.myexample.web.rest;

import static com.penglecode.xmodule.common.consts.ContentType.APPLICATION_JSON;
import static com.penglecode.xmodule.common.consts.ContentType.TEXT_HTML;

import java.util.Map;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;

import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.common.web.support.HttpAccessLogging;

@Component
@Path("/api/example1")
public class Example1Rest extends HttpAPIResourceSupport {

	@GET
	@Path("/java/envs")
	@Consumes(TEXT_HTML)
	@Produces(APPLICATION_JSON)
	@HttpAccessLogging(title="JAVA_ENVS")
	public Object getJavaEnvs() throws Exception {
		Map<String,String> envs = System.getenv();
		return Result.success().message("OK").data(envs).build();
	}
	
	@GET
	@Path("/java/env/{envName}")
	@Consumes(TEXT_HTML)
	@Produces(APPLICATION_JSON)
	@HttpAccessLogging(title="JAVA_ENV_BY_NAME")
	public Object getJavaEnv(@PathParam("envName")String envName) throws Exception {
		Map<String,String> envs = System.getenv();
		return Result.success().message("OK").data(envs.get(envName)).build();
	}
	
	@GET
	@Path("/java/props")
	@Consumes(TEXT_HTML)
	@Produces(APPLICATION_JSON)
	@HttpAccessLogging(title="JAVA_PROPS")
	public Object getJavaProps() throws Exception {
		Properties props = System.getProperties();
		return Result.success().message("OK").data(props).build();
	}
	
	@GET
	@Path("/java/prop/{propName}")
	@Consumes(TEXT_HTML)
	@Produces(APPLICATION_JSON)
	@HttpAccessLogging(title="JAVA_PROP_BY_NAME")
	public Object getJavaProp(@PathParam("propName")String propName) throws Exception {
		Properties props = System.getProperties();
		return Result.success().message("OK").data(props.get(propName)).build();
	}
	
	@GET
	@Path("/java/version")
	@Consumes(TEXT_HTML)
	@Produces(APPLICATION_JSON)
	public Object getJavaVersion() throws Exception {
		return Result.success().message("OK").data("1.8").build();
	}
	
}
