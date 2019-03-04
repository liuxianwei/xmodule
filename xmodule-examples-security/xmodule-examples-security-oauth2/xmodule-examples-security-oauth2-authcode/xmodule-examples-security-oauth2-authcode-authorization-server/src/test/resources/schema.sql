SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS upms_user;




/* Create Tables */

-- 用户权限管理系统-用户表
CREATE TABLE upms_user
(
	-- 用户id
	user_id bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
	-- 用户名
	user_name varchar(32) NOT NULL COMMENT '用户名',
	-- 真实姓名
	real_name varchar(32) COMMENT '真实姓名',
	-- 密码
	password varchar(100) NOT NULL COMMENT '密码',
	-- 用户类型：0-系统用户,1-普通用户
	user_type tinyint DEFAULT 0 NOT NULL COMMENT '用户类型：0-系统用户,1-普通用户',
	-- 昵称
	nick_name varchar(32) NOT NULL COMMENT '昵称',
	-- 手机号码
	mobile_phone varchar(11) NOT NULL COMMENT '手机号码',
	-- 电子邮箱
	email varchar(100) NOT NULL COMMENT '电子邮箱',
	-- 用户头像
	user_icon varchar(255) DEFAULT '/static/images/default-user-avatar.png' COMMENT '用户头像',
	-- 用户状态:0-冻结,1-正常
	status tinyint DEFAULT 1 NOT NULL COMMENT '用户状态:0-冻结,1-正常',
	-- 最后登录时间
	last_login_time datetime COMMENT '最后登录时间',
	-- 登录次数
	login_times int DEFAULT 0 NOT NULL COMMENT '登录次数',
	-- 创建时间
	create_time datetime NOT NULL COMMENT '创建时间',
	-- 创建者,用户表的id
	create_by bigint NOT NULL COMMENT '创建者,用户表的id',
	-- 更新时间
	update_time datetime COMMENT '更新时间',
	-- 更新者,用户表的id
	update_by bigint COMMENT '更新者,用户表的id',
	PRIMARY KEY (user_id),
	CONSTRAINT uk_upms_user_name UNIQUE (user_name)
) ENGINE = InnoDB COMMENT = '用户权限管理系统-用户表';



