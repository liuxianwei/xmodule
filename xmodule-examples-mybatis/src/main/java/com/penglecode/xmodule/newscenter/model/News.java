package com.penglecode.xmodule.newscenter.model;

import com.penglecode.xmodule.common.codegen.Id;
import com.penglecode.xmodule.common.codegen.Model;
import com.penglecode.xmodule.common.support.BaseModel;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 新闻 (nc_news) 实体类
 * 
 * @author PengPeng
 * @date	2019年06月26日 下午 19:22:49
 */
@Model(name="新闻")
public class News implements BaseModel<News> {
     
    private static final long serialVersionUID = 1L;

    /** 新闻ID */
    @Id
    private String newsId;

    /** 新闻标题 */
    private String newsTitle;

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

    /** 审核人ID */
    private Long auditBy;

    /** 删除状态:0-正常，1-已删除 */
    private Boolean deleted;

    /** 创建时间 */
    private String createTime;

    /** 创建者ID */
    private Long createBy;

    /** 更新时间 */
    private String updateTime;

    /** 更新者ID */
    private Long updateBy;

    /** 新闻内容 */
    private String newsContent;

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

    public Long getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(Long auditBy) {
        this.auditBy = auditBy;
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

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public MapBuilder mapBuilder() {
        return new MapBuilder();
    }

    /**
     * Auto generated by Mybatis Generator
     */
    public class MapBuilder {
         
        private final Map<String, Object> modelProperties = new LinkedHashMap<String,Object>();

        MapBuilder() {
            
        }

        public MapBuilder withNewsId(String ... newsId) {
            modelProperties.put("newsId", BaseModel.first(newsId, getNewsId()));
            return this;
        }

        public MapBuilder withNewsTitle(String ... newsTitle) {
            modelProperties.put("newsTitle", BaseModel.first(newsTitle, getNewsTitle()));
            return this;
        }

        public MapBuilder withNewsTags(String ... newsTags) {
            modelProperties.put("newsTags", BaseModel.first(newsTags, getNewsTags()));
            return this;
        }

        public MapBuilder withPublishTime(String ... publishTime) {
            modelProperties.put("publishTime", BaseModel.first(publishTime, getPublishTime()));
            return this;
        }

        public MapBuilder withPublisherId(Long ... publisherId) {
            modelProperties.put("publisherId", BaseModel.first(publisherId, getPublisherId()));
            return this;
        }

        public MapBuilder withAuditStatus(Integer ... auditStatus) {
            modelProperties.put("auditStatus", BaseModel.first(auditStatus, getAuditStatus()));
            return this;
        }

        public MapBuilder withAuditRemark(String ... auditRemark) {
            modelProperties.put("auditRemark", BaseModel.first(auditRemark, getAuditRemark()));
            return this;
        }

        public MapBuilder withAuditTime(String ... auditTime) {
            modelProperties.put("auditTime", BaseModel.first(auditTime, getAuditTime()));
            return this;
        }

        public MapBuilder withAuditBy(Long ... auditBy) {
            modelProperties.put("auditBy", BaseModel.first(auditBy, getAuditBy()));
            return this;
        }

        public MapBuilder withDeleted(Boolean ... deleted) {
            modelProperties.put("deleted", BaseModel.first(deleted, getDeleted()));
            return this;
        }

        public MapBuilder withCreateTime(String ... createTime) {
            modelProperties.put("createTime", BaseModel.first(createTime, getCreateTime()));
            return this;
        }

        public MapBuilder withCreateBy(Long ... createBy) {
            modelProperties.put("createBy", BaseModel.first(createBy, getCreateBy()));
            return this;
        }

        public MapBuilder withUpdateTime(String ... updateTime) {
            modelProperties.put("updateTime", BaseModel.first(updateTime, getUpdateTime()));
            return this;
        }

        public MapBuilder withUpdateBy(Long ... updateBy) {
            modelProperties.put("updateBy", BaseModel.first(updateBy, getUpdateBy()));
            return this;
        }

        public MapBuilder withNewsContent(String ... newsContent) {
            modelProperties.put("newsContent", BaseModel.first(newsContent, getNewsContent()));
            return this;
        }

        public Map<String, Object> build() {
            return modelProperties;
        }
    }
}