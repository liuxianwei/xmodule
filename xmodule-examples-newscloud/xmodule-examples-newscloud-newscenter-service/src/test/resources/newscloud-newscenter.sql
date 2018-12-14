SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS nc_news;




/* Create Tables */

-- 新闻表
CREATE TABLE nc_news
(
	-- 新闻ID
	news_id varchar(100) NOT NULL COMMENT '新闻ID',
	-- 新闻标题
	news_title varchar(255) NOT NULL COMMENT '新闻标题',
	-- 新闻内容
	news_content text NOT NULL COMMENT '新闻内容',
	-- 新闻标签
	news_tags varchar(255) COMMENT '新闻标签',
	-- 发布时间
	publish_time datetime COMMENT '发布时间',
	-- 发布者用户ID
	publisher_id bigint NOT NULL COMMENT '发布者用户ID',
	-- 审核状态：0-待审核,1-审核通过,2-审核不通过
	audit_status tinyint DEFAULT 0 NOT NULL COMMENT '审核状态：0-待审核,1-审核通过,2-审核不通过',
	-- 审核备注
	audit_remark varchar(512) COMMENT '审核备注',
	-- 审核时间
	audit_time datetime COMMENT '审核时间',
	-- 删除状态:0-正常，1-已删除
	deleted tinyint(1) DEFAULT 0 NOT NULL COMMENT '删除状态:0-正常，1-已删除',
	-- 创建时间
	create_time datetime NOT NULL COMMENT '创建时间',
	-- 更新时间
	update_time datetime COMMENT '更新时间',
	PRIMARY KEY (news_id),
	CONSTRAINT uk_nc_news_title UNIQUE (news_title, publisher_id)
) COMMENT = '新闻表';



