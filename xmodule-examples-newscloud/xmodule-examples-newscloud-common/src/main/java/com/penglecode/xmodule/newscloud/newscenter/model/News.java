package com.penglecode.xmodule.newscloud.newscenter.model;

import com.penglecode.xmodule.common.support.BaseModel;

/**
 * News (nc_news) 实体类
 * 
 * @author PengPeng
 * @date	2018-10-08 11:27:11
 */
public class News implements BaseModel {
     
    private static final long serialVersionUID = 1L;

    /** 新闻ID */
    private String newsId;

    /** 新闻标题 */
    private String newsTitle;

    /** 新闻内容 */
    private String newsContent;
    
    /** 新闻标签 */
    private String newsTags;

    /** 发布时间 */
    private String publishTime;

    /** 发布者用户ID */
    private Long publisherId;

    /** 审核状态：0-待审核,1-审核通过,2-审核不通过 */
    private Integer auditStatus;

    /** 审核备注 */
    private String auditRemark;

    /** 审核时间 */
    private String auditTime;

    /** 删除状态:0-正常，1-已删除 */
    private Boolean deleted;

    /** 创建时间 */
    private String createTime;

    /** 更新时间 */
    private String updateTime;
    
    //以下属于辅助字段
    
    private String auditStatusName;

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }
    
    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getNewsTags() {
        return newsTags;
    }

    public void setNewsTags(String newsTags) {
        this.newsTags = newsTags;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

	public String getAuditStatusName() {
		return auditStatusName;
	}

	public void setAuditStatusName(String auditStatusName) {
		this.auditStatusName = auditStatusName;
	}

}