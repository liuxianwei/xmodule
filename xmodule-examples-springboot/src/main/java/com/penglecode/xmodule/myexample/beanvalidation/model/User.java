package com.penglecode.xmodule.myexample.beanvalidation.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.penglecode.xmodule.common.support.BaseModel;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.ConstraintOrder.Constraint1;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.ConstraintOrder.Constraint2;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.ConstraintOrder.Constraint3;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.Create;
import com.penglecode.xmodule.myexample.beanvalidation.consts.ValidationGroups.Update;

public class User implements BaseModel<User> {

	private static final long serialVersionUID = 1L;

	@Null(message="用户ID必须为空!", groups={Create.class})
	@NotNull(message="用户ID不能为空!", groups={Constraint1.class, Update.class})
	@Positive(message="用户ID不合法!", groups={Constraint2.class, Update.class})
	private Long userId;
	
	@NotBlank(message="用户名不能为空!", groups={Constraint1.class, Create.class, Update.class})
	@Pattern(regexp="[a-zA-Z]{1}[a-zA-Z0-9_]{2,19}", message="用户名必须由字母开头,3~20个字母、数字及下划线组成!", groups={Constraint3.class, Create.class, Update.class})
	@Size(min=3, max=20, message="用户名长度为3~20个字符!", groups={Constraint2.class, Create.class, Update.class})
	private String userName;
	
	@NotBlank(message="密码不能为空!", groups={Constraint1.class, Create.class, Update.class})
	@Pattern(regexp="[a-zA-Z0-9]{6,20}", message="密码由6~20个字母或数字组成!", groups={Constraint3.class, Create.class, Update.class})
	@Size(min=3, max=20, message="密码长度为6~20个字符!", groups={Constraint2.class, Create.class, Update.class})
	private String password;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", password=" + password + "]";
	}
	
}
