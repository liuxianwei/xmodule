package com.penglecode.xmodule.upms.model;

import com.penglecode.xmodule.common.support.BaseModel;

/**
 * UpmsUserAccessLog (upms_user_access_log) 实体类
 * 
 * @author PengPeng
 * @date	2019-01-15 10:31:24
 */
public class UpmsUserAccessLog implements BaseModel<UpmsUserAccessLog> {
     
    private static final long serialVersionUID = 1L;

    /** 日志ID */
    private Long logId;

    /** 日志标题 */
    private String logTitle;

    /** 所属应用模块 */
    private String appModule;

    /** 请求URI */
    private String requestUri;

    /** HTTP请求方法,例如:POST,GET */
    private String httpMethod;

    /** MVC控制器方法 */
    private String mvcMethod;

    /** 请求头 */
    private String requestHeader;

    /** 请求的Content-Type */
    private String requestContentType;

    /** 请求内容(参数) */
    private String requestContent;

    /** 访问用户ID */
    private Long accessUserId;

    /** 访问时间 */
    private String accessTime;

    /** 客户端IP地址 */
    private String clientIpAddr;

    /** 访问的服务器IP:端口号 */
    private String serverIpAddr;

    /** 控制器方法的执行时长(毫秒) */
    private Long processTime;

    /** 日志记录是否结束,1-是0-否 */
    private Boolean loggingCompleted;

    /** 请求是否是异步的,1-是0-否 */
    private Boolean asyncRequest;

    /** 响应的Content-Type */
    private String responseContentType;

    /** 访问应用ID */
    private Long appId;

    /** 创建时间 */
    private String createTime;

    /** 响应内容 */
    private String responseContent;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public String getLogTitle() {
        return logTitle;
    }

    public void setLogTitle(String logTitle) {
        this.logTitle = logTitle;
    }

    public String getAppModule() {
        return appModule;
    }

    public void setAppModule(String appModule) {
        this.appModule = appModule;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getMvcMethod() {
        return mvcMethod;
    }

    public void setMvcMethod(String mvcMethod) {
        this.mvcMethod = mvcMethod;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
    }

    public Long getAccessUserId() {
        return accessUserId;
    }

    public void setAccessUserId(Long accessUserId) {
        this.accessUserId = accessUserId;
    }

    public String getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(String accessTime) {
        this.accessTime = accessTime;
    }

    public String getClientIpAddr() {
        return clientIpAddr;
    }

    public void setClientIpAddr(String clientIpAddr) {
        this.clientIpAddr = clientIpAddr;
    }

    public String getServerIpAddr() {
        return serverIpAddr;
    }

    public void setServerIpAddr(String serverIpAddr) {
        this.serverIpAddr = serverIpAddr;
    }

    public Long getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Long processTime) {
        this.processTime = processTime;
    }

    public Boolean getLoggingCompleted() {
        return loggingCompleted;
    }

    public void setLoggingCompleted(Boolean loggingCompleted) {
        this.loggingCompleted = loggingCompleted;
    }

    public Boolean getAsyncRequest() {
        return asyncRequest;
    }

    public void setAsyncRequest(Boolean asyncRequest) {
        this.asyncRequest = asyncRequest;
    }

    public String getResponseContentType() {
        return responseContentType;
    }

    public void setResponseContentType(String responseContentType) {
        this.responseContentType = responseContentType;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }
}