SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS nc_user;
DROP TABLE IF EXISTS nc_user_follower;




/* Create Tables */

-- 用户表
CREATE TABLE nc_user
(
	-- 用户ID
	user_id bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
	-- 用户名
	user_name varchar(100) NOT NULL COMMENT '用户名',
	-- 昵称
	nick_name varchar(100) NOT NULL COMMENT '昵称',
	-- 手机号码
	mobile_phone varchar(25) NOT NULL COMMENT '手机号码',
	-- 用户头像URL
	user_icon_url varchar(512) COMMENT '用户头像URL',
	-- 电子邮箱
	email varchar(255) NOT NULL COMMENT '电子邮箱',
	-- 性别:男，女
	sex char(1) COMMENT '性别:男，女',
	-- 出生日期
	birthday date COMMENT '出生日期',
	-- 账户状态：0-禁用一切,1-正常,2-禁言
	status tinyint DEFAULT 1 NOT NULL COMMENT '账户状态：0-禁用一切,1-正常,2-禁言',
	-- 是否已认证：1-是，0-否
	authorized tinyint(1) DEFAULT 0 NOT NULL COMMENT '是否已认证：1-是，0-否',
	-- 已认证身份证号码
	auth_idcard varchar(18) COMMENT '已认证身份证号码',
	-- 已认证真实姓名
	auth_real_name varchar(50) COMMENT '已认证真实姓名',
	-- 登录次数
	login_times int DEFAULT 0 NOT NULL COMMENT '登录次数',
	-- 最后登录时间
	last_login_time datetime COMMENT '最后登录时间',
	-- 关注数(关注他人)
	follows int DEFAULT 0 NOT NULL COMMENT '关注数(关注他人)',
	-- 粉丝数(被被人关注)
	followers int DEFAULT 0 NOT NULL COMMENT '粉丝数(被被人关注)',
	-- 创建时间
	create_time datetime NOT NULL COMMENT '创建时间',
	-- 最近修改时间
	update_time datetime COMMENT '最近修改时间',
	PRIMARY KEY (user_id),
	CONSTRAINT uk_nc_user_name UNIQUE (user_name)
) COMMENT = '用户表';


-- 用户粉丝表
CREATE TABLE nc_user_follower
(
	-- ID
	id bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
	-- 被关注用户ID
	user_id bigint NOT NULL COMMENT '被关注用户ID',
	-- 粉丝ID
	follower_user_id bigint NOT NULL COMMENT '粉丝ID',
	-- 关注时间
	follow_time datetime NOT NULL COMMENT '关注时间',
	PRIMARY KEY (id),
	CONSTRAINT uk_nc_user_follow UNIQUE (follower_user_id, user_id)
) COMMENT = '用户粉丝表';



