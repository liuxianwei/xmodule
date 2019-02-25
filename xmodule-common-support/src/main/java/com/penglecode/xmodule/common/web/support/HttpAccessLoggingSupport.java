package com.penglecode.xmodule.common.web.support;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.http.MediaType;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.support.AbstractSpringTypedBeanManager;
import com.penglecode.xmodule.common.support.NamedThreadFactory;
import com.penglecode.xmodule.common.util.ArrayUtils;
import com.penglecode.xmodule.common.util.CollectionUtils;
import com.penglecode.xmodule.common.util.HttpServletUtils;
import com.penglecode.xmodule.common.util.JsonUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.support.HttpAccessLog.HttpRequestParameter;
import com.penglecode.xmodule.common.web.support.HttpAccessLogging.LoggingMode;

/**
 * Http请求日志记录辅助类
 * 
 * @author 	pengpeng
 * @date	2018年8月8日 上午10:11:30
 */
@SuppressWarnings("unchecked")
public abstract class HttpAccessLoggingSupport extends AbstractSpringTypedBeanManager<HttpAccessLogDAO,HttpAccessLogging.LoggingMode> implements DisposableBean {

	private static final Pattern MESSAGE_SOURCE_CODE_PATTERN = Pattern.compile("\\$\\{([a-zA-Z0-9_.]+)\\}");
	
	private static final Logger logger = LoggerFactory.getLogger(HttpAccessLoggingSupport.class);
	
	private static final ExecutorService httpAccessLogHandlerExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2, new NamedThreadFactory("HTTP-REQUEST-LOGGING-EXECUTE-WORKER-"));
	
	private static final String DEFAULT_LOG_TITLE = "用户访问日志";
	
	private String defaultLogTitle = DEFAULT_LOG_TITLE;
	
	private boolean loggingRequestHeader = false;
	
	public String getDefaultLogTitle() {
		return defaultLogTitle;
	}

	public void setDefaultLogTitle(String defaultLogTitle) {
		this.defaultLogTitle = defaultLogTitle;
	}

	public boolean isLoggingRequestHeader() {
		return loggingRequestHeader;
	}

	public void setLoggingRequestHeader(boolean loggingRequestHeader) {
		this.loggingRequestHeader = loggingRequestHeader;
	}

	public static ExecutorService getHttpAccessLogHandlerExecutor() {
		return httpAccessLogHandlerExecutor;
	}

	/**
	 * 获取日志标题
	 * @param title
	 * @return
	 */
	protected String getLogTitle(String title) {
		if(!StringUtils.isEmpty(title)){
			Matcher matcher = MESSAGE_SOURCE_CODE_PATTERN.matcher(title);
			if(matcher.find()){
				title = getMessage(matcher.group(1));
			}
		}
		return StringUtils.defaultIfEmpty(title, defaultLogTitle);
	}
	
	/**
	 * 从国际化资源文件中获取message
	 * @param code
	 * @return
	 */
	protected String getMessage(String code) {
		return ApplicationConstants.MESSAGE_SOURCE_ACCESSOR.getMessage(code);
	}
	
	protected String getStringParameterValue(String[] values){
		if(values == null){
			return null;
		}else{
			return values.length == 1 ? values[0] : Arrays.toString(values);
		}
	}
	
	/**
	 * 从HttpServletRequest中提取请求参数(包括请求体中的参数)
	 * @param request
	 * @param context
	 * @return
	 * @throws Exception
	 */
	protected HttpRequestParameter extractRequestParameter(HttpServletRequest request, HttpAccessLogContext context) throws IOException {
		HttpRequestParameter parameter = new HttpRequestParameter();
		Map<String,String[]> originalParamMap = request.getParameterMap();
		Map<String,String> paramMap = new HashMap<String,String>();
		if(originalParamMap != null && !originalParamMap.isEmpty()){
			for(Map.Entry<String, String[]> entry : originalParamMap.entrySet()){
				paramMap.put(entry.getKey(), getStringParameterValue(entry.getValue()));
			}
		}
		parameter.setParameter(paramMap);
		MediaType contentType = context.getHttpAccessLog().getRequestContentType();
		if(contentType != null){
			if(contentType.includes(MediaType.APPLICATION_JSON)) { //目前只处理JSON类型的数据
				ContentCachingRequestWrapper requestToUse = HttpServletUtils.getContentCachingRequestWrapper(request);
				if(requestToUse != null){
					String charset = HttpServletUtils.getCharacterEncoding(request, GlobalConstants.DEFAULT_CHARSET);
					byte[] bytes = requestToUse.getContentAsByteArray();
					if(!ArrayUtils.isEmpty(bytes)){
						String body = IOUtils.toString(bytes, charset);
						Object bodyObj = body;
						if(body.startsWith("[") && body.endsWith("]")){ //JSON Array String -> List<Map<String,Object>>
							bodyObj = JsonUtils.json2Object(body, new TypeReference<List<Map<String,Object>>>(){});
						}else if(body.startsWith("{") && body.endsWith("}")){ //JSON Object String -> Map<String,Object>
							bodyObj = JsonUtils.json2Object(body, new TypeReference<Map<String,Object>>(){});
						}
						parameter.setBody(bodyObj);
					}
				}
			}
		}
		return excludeRequestParameter(parameter, context);
	}
	
	/**
	 * 记录请求参数时剔除一些敏感数据,如用户密码明文
	 * @param parameter
	 * @param context
	 * @return
	 */
	protected HttpRequestParameter excludeRequestParameter(HttpRequestParameter parameter, HttpAccessLogContext context) {
		HttpAccessLogging httpAccessLogging = context.getHttpAccessLogging();
		String[] excludeNameParams = httpAccessLogging.excludeParams();
		if(excludeNameParams != null && excludeNameParams.length > 0){
			try {
				for(String paramName : excludeNameParams){
					if(parameter.getParameter() != null){
						parameter.getParameter().remove(paramName);
					}
					MediaType contentType = context.getHttpAccessLog().getRequestContentType();
					if (contentType != null && parameter.getBody() != null) {
						if(parameter.getBody() instanceof List){ //JSON Array
							for(Map<String,Object> item : (List<Map<String,Object>>)parameter.getBody()){
								excludeParameter(item, paramName);
							}
						}else if(parameter.getBody() instanceof Map){ //JSON Object
							excludeParameter((Map<String,Object>)parameter.getBody(), paramName);
						}
					}
				}
			} catch (Exception e) {
				logger.error(">>> 排除参数出错! " + e.getMessage());
			}
		}
		return parameter;
	}
	
	/**
	 * 递归剔除参数
	 * @param parameter
	 * @param paramName
	 */
	protected void excludeParameter(Map<String,Object> parameter, String paramName) {
		if(!CollectionUtils.isEmpty(parameter)){
			parameter.remove(paramName);
			for(Map.Entry<String,Object> entry : parameter.entrySet()){
				Object value = entry.getValue();
				if(value != null){
					if(value instanceof List){
						List<Map<String,Object>> list = (List<Map<String, Object>>) value;
						for(Map<String,Object> item : list){
							excludeParameter(item, paramName);
						}
					}else if(value instanceof Map){
						Map<String,Object> item = (Map<String, Object>) value;
						excludeParameter(item, paramName);
					}
				}
			}
		}
	}
	
	/**
	 * 从HttpServletRequest中提取请求头信息
	 * @param request
	 * @param context
	 * @return
	 */
	protected Map<String,String> extractRequestHeader(HttpServletRequest request, HttpAccessLogContext context) {
		Map<String,String> headerMap = new HashMap<String,String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		if(headerNames != null){
			while(headerNames.hasMoreElements()){
				String headerName = headerNames.nextElement();
				headerMap.put(headerName, request.getHeader(headerName));
			}
		}
		return headerMap;
	}
	
	/**
	 * 提取请求结果
	 * @param request
	 * @param context
	 * @return
	 */
	protected Object extractResponseResult(HttpServletResponse response, HttpAccessLogContext context) throws IOException {
		MediaType contentType = context.getHttpAccessLog().getResponseContentType();
		if(contentType != null){
			if(contentType.includes(MediaType.APPLICATION_JSON)) { //目前只处理JSON类型的数据
				ContentCachingResponseWrapper responseToUse = HttpServletUtils.getContentCachingResponseWrapper(response);
				if(responseToUse != null){
					String charset = HttpServletUtils.getCharacterEncoding(response, GlobalConstants.DEFAULT_CHARSET);
					byte[] bytes = responseToUse.getContentAsByteArray();
					if(bytes != null){
						return IOUtils.toString(bytes, charset);
					}
				}
			}
		}
		return null;
	}
	
	public void destroy() throws Exception {
		getHttpAccessLogHandlerExecutor().shutdown();
	}
	
	protected boolean filterBean(HttpAccessLogDAO httpAccessLogDAO, LoggingMode parameter) {
		if(parameter != null){
			return parameter.equals(httpAccessLogDAO.getLoggingMode());
		}
		return false;
	}

	/**
	 * 获取操作人的LoginUser对象
	 * @param request
	 * @param context
	 * @return
	 */
	protected abstract <T> T getAccessUser(HttpServletRequest request, HttpAccessLogContext context);
	
	/**
	 * 创建HttpRequestLogger处理器(如将日志写入数据库、日志文件等等)
	 * @param context
	 * @return
	 */
	protected HttpAccessLogDAO getHttpAccessLogDAO(HttpAccessLogContext context) {
		return getTypedBean(context.getHttpAccessLogging().loggingMode());
	}
	
	/**
	 * 执行日志记录任务
	 * @param request
	 * @param response
	 * @param context
	 */
	protected void executeHttpAccessLoggingTask(HttpServletRequest request, HttpServletResponse response, HttpAccessLogContext context) {
		HttpAccessLogDAO httpAccessLogDAO = getHttpAccessLogDAO(context);
		if(httpAccessLogDAO != null){
			getHttpAccessLogHandlerExecutor().submit(new DefaultHttpAccessLoggingTask(request, response, context, httpAccessLogDAO));
		}
	}
	
	@SuppressWarnings("unused")
	public class DefaultHttpAccessLoggingTask implements Runnable {

		private final HttpServletRequest request;
		
		private final HttpServletResponse response;
		
		private final HttpAccessLogContext context;
		
		private final HttpAccessLogDAO httpAccessLogDAO;
		
		public DefaultHttpAccessLoggingTask(HttpServletRequest request, HttpServletResponse response, HttpAccessLogContext context, HttpAccessLogDAO httpAccessLogDAO) {
			super();
			this.request = request;
			this.response = response;
			this.context = context;
			this.httpAccessLogDAO = httpAccessLogDAO;
		}

		public void run() {
			try {
				HttpAccessLog<?> httpAccessLog = context.getHttpAccessLog();
				if (httpAccessLog != null) {
					logger.debug(">>> http access log : " + httpAccessLog);
					httpAccessLogDAO.saveLog((HttpAccessLog<?>) httpAccessLog);
				} 
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			
		}
		
	}
	
}
