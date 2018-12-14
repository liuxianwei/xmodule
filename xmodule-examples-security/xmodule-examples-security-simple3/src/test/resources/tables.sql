SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS ss_simple_user;




/* Create Tables */

-- 用户表
CREATE TABLE ss_simple_user
(
	-- 用户ID
	user_id bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
	-- 用户名
	user_name varchar(100) NOT NULL COMMENT '用户名',
	-- 密码
	password varchar(100) NOT NULL COMMENT '密码',
	-- 昵称
	nick_name varchar(100) NOT NULL COMMENT '昵称',
	-- 角色代码列表
	role_codes varchar(512) COMMENT '角色代码列表',
	-- 创建时间
	create_time datetime NOT NULL COMMENT '创建时间',
	PRIMARY KEY (user_id),
	UNIQUE (user_name)
) COMMENT = '用户表';



