SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS sa_upms_resource;
DROP TABLE IF EXISTS sa_upms_role;
DROP TABLE IF EXISTS sa_upms_role_resource;
DROP TABLE IF EXISTS sa_upms_user;
DROP TABLE IF EXISTS sa_upms_user_role;




/* Create Tables */

-- 用户权限管理系统-资源菜单表
CREATE TABLE sa_upms_resource
(
	-- 资源id
	resource_id bigint NOT NULL AUTO_INCREMENT COMMENT '资源id',
	-- 资源名称
	resource_name varchar(100) NOT NULL COMMENT '资源名称',
	-- 父级资源id
	parent_resource_id bigint NOT NULL COMMENT '父级资源id',
	-- 是否是应用的根节点：1-是，0-否
	app_root_resource tinyint(1) DEFAULT 0 NOT NULL COMMENT '是否是应用的根节点：1-是，0-否',
	-- 资源URL
	resource_url varchar(512) COMMENT '资源URL',
	-- 资源类型：0-系统资源,1-普通资源
	resource_type tinyint DEFAULT 0 NOT NULL COMMENT '资源类型：0-系统资源,1-普通资源',
	-- HTTP方法(GET,POST,DELETE,PUT)
	http_method varchar(20) DEFAULT 'GET' COMMENT 'HTTP方法(GET,POST,DELETE,PUT)',
	-- 功能类型：0-菜单，1-按钮
	action_type tinyint DEFAULT 0 NOT NULL COMMENT '功能类型：0-菜单，1-按钮',
	-- 兄弟节点间的排序号,asc排序
	siblings_index int NOT NULL COMMENT '兄弟节点间的排序号,asc排序',
	-- 资源菜单ICON(font-awesome类名)
	resource_icon varchar(255) DEFAULT 'fa-circle-o' COMMENT '资源菜单ICON(font-awesome类名)',
	-- 是否是首页：1-是，0-否
	index_page tinyint(1) DEFAULT 0 NOT NULL COMMENT '是否是首页：1-是，0-否',
	-- 创建时间
	create_time datetime NOT NULL COMMENT '创建时间',
	-- 创建者,用户表id
	create_by bigint NOT NULL COMMENT '创建者,用户表id',
	-- 更新时间
	update_time datetime COMMENT '更新时间',
	-- 更新者,用户表的id
	update_by bigint COMMENT '更新者,用户表的id',
	PRIMARY KEY (resource_id),
	CONSTRAINT sa_upms_resource_uk_resource_name UNIQUE (resource_name, parent_resource_id)
) ENGINE = InnoDB COMMENT = '用户权限管理系统-资源菜单表'
AUTO_INCREMENT = 100;


-- 用户权限管理系统-角色表
CREATE TABLE sa_upms_role
(
	-- 角色id
	role_id bigint NOT NULL AUTO_INCREMENT COMMENT '角色id',
	-- 角色名称
	role_name varchar(100) NOT NULL COMMENT '角色名称',
	-- 角色代码,由字母、下划线组成
	role_code varchar(100) NOT NULL COMMENT '角色代码,由字母、下划线组成',
	-- 角色类型：0-系统角色,1-普通角色
	role_type tinyint DEFAULT 0 NOT NULL COMMENT '角色类型：0-系统角色,1-普通角色',
	-- 角色描述
	description varchar(512) NOT NULL COMMENT '角色描述',
	-- 创建时间
	create_time datetime NOT NULL COMMENT '创建时间',
	-- 创建者,用户表的id
	create_by bigint NOT NULL COMMENT '创建者,用户表的id',
	-- 最近更新时间
	update_time datetime COMMENT '最近更新时间',
	update_by bigint,
	PRIMARY KEY (role_id),
	CONSTRAINT sa_upms_role_uk_role_name UNIQUE (role_name),
	CONSTRAINT sa_upms_role_uk_role_code UNIQUE (role_code)
) ENGINE = InnoDB COMMENT = '用户权限管理系统-角色表';


-- 用户权限管理系统-角色.资源关系表
CREATE TABLE sa_upms_role_resource
(
	-- 角色id
	role_id bigint NOT NULL COMMENT '角色id',
	-- 资源id
	resource_id bigint NOT NULL COMMENT '资源id',
	-- 创建时间
	create_time datetime NOT NULL COMMENT '创建时间',
	-- 创建者,用户表id
	create_by bigint NOT NULL COMMENT '创建者,用户表id',
	PRIMARY KEY (role_id, resource_id)
) ENGINE = InnoDB COMMENT = '用户权限管理系统-角色.资源关系表';


-- 用户权限管理系统-用户表
CREATE TABLE sa_upms_user
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
	user_icon varchar(255) DEFAULT '/images/default-user-avatar.png' COMMENT '用户头像',
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


-- 用户权限管理系统-用户角色关系表
CREATE TABLE sa_upms_user_role
(
	-- 用户id
	user_id bigint NOT NULL COMMENT '用户id',
	-- 角色id
	role_id bigint NOT NULL COMMENT '角色id',
	-- 创建时间
	create_time datetime NOT NULL COMMENT '创建时间',
	-- 创建者,用户表id
	create_by bigint NOT NULL COMMENT '创建者,用户表id',
	PRIMARY KEY (user_id, role_id)
) ENGINE = InnoDB COMMENT = '用户权限管理系统-用户角色关系表';



