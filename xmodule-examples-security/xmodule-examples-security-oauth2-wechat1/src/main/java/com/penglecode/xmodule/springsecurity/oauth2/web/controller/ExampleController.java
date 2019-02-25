package com.penglecode.xmodule.springsecurity.oauth2.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.penglecode.xmodule.common.util.URIUtils;
import com.penglecode.xmodule.common.util.UUIDUtils;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.springsecurity.oauth2.config.WechatOAuth2ConfigProperties;

@Controller
public class ExampleController extends HttpAPIResourceSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExampleController.class);
	
	private static final String WECHAT_OAUTH2_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid={appid}&redirect_uri={redirect_uri}&response_type={response_type}&scope={scope}&state={state}#wechat_redirect";
	
	@Autowired
	private WechatOAuth2ConfigProperties wechatOAuth2Config;
	
	/*@RequestMapping("/login")
	public String goToLogin() {
		String state = UUIDUtils.uuid();
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("appid", wechatOAuth2Config.getAppid());
		parameter.put("redirect_uri", wechatOAuth2Config.getRedirect_uri());
		parameter.put("response_type", "code");
		parameter.put("scope", "snsapi_userinfo");
		parameter.put("state", state);
		String url = URIUtils.createURI(WECHAT_OAUTH2_URL, parameter).toString();
		System.out.println(">>> url = " + url);
		return "redirect:" + url;
	}*/
	
	@RequestMapping("/home")
	public String goToHome() {
		return "home";
	}
	
	@RequestMapping("/welcome")
	public String welcome() {
		return "welcome";
	}
	
	@RequestMapping("/oauth2")
	public String oauth2(String code, String state) {
		LOGGER.info(">>> code = {}, state = {}", code, state);
		return "oauth2";
	}
	
}
