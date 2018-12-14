package com.penglecode.xmodule.springsecurity.upms.web.security.filter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.MapUtils;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.util.IOUtils;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.util.StringUtils;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException(
					"Authentication method not supported: " + request.getMethod());
		}
		
		MediaType requestContentType = getRequestContentType(request);
		
		if(requestContentType != null) {
			if(requestContentType.includes(MediaType.APPLICATION_JSON)) {
				return attemptRestAuthentication(request, response, requestContentType);
			}
		}
		return attemptFormAuthentication(request, response, requestContentType);
	}
	
	protected Authentication attemptFormAuthentication(HttpServletRequest request, HttpServletResponse response, MediaType requestContentType)
			throws AuthenticationException {
		return super.attemptAuthentication(request, response);
	}
	
	protected Authentication attemptRestAuthentication(HttpServletRequest request, HttpServletResponse response, MediaType requestContentType)
			throws AuthenticationException {
		try {
			Charset charset = Charset.forName(GlobalConstants.DEFAULT_CHARSET);
			String encoding = requestContentType.getParameter("charset");
			if(!StringUtils.isEmpty(encoding)) {
				try {
					charset = Charset.forName(encoding);
				} catch (Exception e) {
				}
			}
			String requestBody = IOUtils.toString(request.getInputStream(), charset);
			Map<String,Object> requestParameters = JsonUtils.json2Object(requestBody, new TypeReference<Map<String,Object>>(){});
			
			String username = MapUtils.getString(requestParameters, getUsernameParameter());
			String password = MapUtils.getString(requestParameters, getPasswordParameter());
			
			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
			// Allow subclasses to set the "details" property
			setDetails(request, authRequest);

			return this.getAuthenticationManager().authenticate(authRequest);
		} catch (IOException e) {
			throw new InternalAuthenticationServiceException(e.getMessage(), e);
		}
	}
	
	protected MediaType getRequestContentType(HttpServletRequest request) {
		try {
			String contentTypeValue = request.getContentType();
			if(!StringUtils.isEmpty(contentTypeValue)) {
				return MediaType.valueOf(contentTypeValue);
			}
		} catch (Exception e) {
		}
		return null;
	}
	
}
