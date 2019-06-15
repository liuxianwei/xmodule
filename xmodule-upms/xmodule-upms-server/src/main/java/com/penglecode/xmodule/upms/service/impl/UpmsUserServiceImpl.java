package com.penglecode.xmodule.upms.service.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.security.util.UserPasswordUtils;
import com.penglecode.xmodule.common.support.BusinessAssert;
import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.ModelDecodeUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.upms.consts.em.UpmsUserStatusEnum;
import com.penglecode.xmodule.upms.consts.em.UpmsUserTypeEnum;
import com.penglecode.xmodule.upms.mapper.UpmsUserMapper;
import com.penglecode.xmodule.upms.model.UpmsResource;
import com.penglecode.xmodule.upms.model.UpmsRole;
import com.penglecode.xmodule.upms.model.UpmsUser;
import com.penglecode.xmodule.upms.service.UpmsUserService;

@Service("upmsUserService")
public class UpmsUserServiceImpl implements UpmsUserService {

	@Autowired
	private UpmsUserMapper upmsUserMapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void createUser(UpmsUser user) {
		ValidationAssert.notNull(user, "参数不能为空!");
		ValidationAssert.notEmpty(user.getUserName(), "用户名不能为空!");
		ValidationAssert.notEmpty(user.getPassword(), "用户密码不能为空!");
		ValidationAssert.notEmpty(user.getRealName(), "真实姓名不能为空!");
		ValidationAssert.notEmpty(user.getNickName(), "用户昵称不能为空!");
		ValidationAssert.notEmpty(user.getMobilePhone(), "手机号码不能为空!");
		ValidationAssert.notEmpty(user.getEmail(), "Email不能为空!");
		user.setUserId(null);
		user.setPassword(UserPasswordUtils.encode(user.getPassword()));
		user.setUserType(UpmsUserTypeEnum.USER_TYPE_NORMAL.getTypeCode());
		user.setStatus(UpmsUserStatusEnum.USER_STATUS_ENABLED.getStatusCode());
		user.setUserIcon(StringUtils.defaultIfEmpty(user.getUserIcon(), GlobalConstants.DEFAULT_USER_AVATAR));
		user.setCreateTime(DateTimeUtils.formatNow());
		user.setCreateBy(ObjectUtils.defaultIfNull(user.getCreateBy(), GlobalConstants.DEFAULT_SUPER_ADMIN_USER_ID));
		try {
			upmsUserMapper.insertModel(user);
		} catch (DuplicateKeyException e) {
            BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("USER_NAME"), "对不起,该用户名已存在!");
            throw e;
        }
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updateUser(UpmsUser user) {
		ValidationAssert.notNull(user, "参数不能为空!");
		ValidationAssert.notNull(user.getUserId(), "用户ID不能为空!");
		ValidationAssert.notEmpty(user.getUserName(), "用户名不能为空!");
		ValidationAssert.notEmpty(user.getRealName(), "真实姓名不能为空!");
		ValidationAssert.notEmpty(user.getNickName(), "用户昵称不能为空!");
		ValidationAssert.notEmpty(user.getMobilePhone(), "手机号码不能为空!");
		ValidationAssert.notEmpty(user.getEmail(), "Email不能为空!");
		user.setUserIcon(StringUtils.defaultIfEmpty(user.getUserIcon(), GlobalConstants.DEFAULT_USER_AVATAR));
		user.setUpdateTime(DateTimeUtils.formatNow());
		user.setUpdateBy(ObjectUtils.defaultIfNull(user.getUpdateBy(), GlobalConstants.DEFAULT_SUPER_ADMIN_USER_ID));
		UpmsUser puser = upmsUserMapper.selectModelById(user.getUserId());
		ValidationAssert.notNull(puser, "该用户已经不存在了!");
		try {
			upmsUserMapper.updateModelById(user);
		} catch (DuplicateKeyException e) {
            BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("USER_NAME"), "对不起,该用户名已存在!");
            throw e;
        }
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void deleteUserById(Long userId) {
        ValidationAssert.notNull(userId, "用户ID不能为空!");
        UpmsUser puser = upmsUserMapper.selectModelById(userId);
        ValidationAssert.notNull(puser, "该用户已经不存在了!");
        BusinessAssert.isTrue(!UpmsUserTypeEnum.USER_TYPE_SYSTEM.getTypeCode().equals(puser.getUserType()), "系统级用户不能被删除!");
        upmsUserMapper.deleteModelById(userId); //删除用户信息
        upmsUserMapper.deleteUserAllRoles(userId); //删除用户的所有角色关系
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void updateUserStatus(Long userId, Integer status) {
		ValidationAssert.notNull(userId, "用户ID不能为空!");
        ValidationAssert.notNull(UpmsUserStatusEnum.getStatus(status), String.format("无法识别的用户状态(status=%s)!", status));
        UpmsUser puser = upmsUserMapper.selectModelById(userId);
        ValidationAssert.notNull(puser, "该用户已经不存在了!");
        UpmsUser param = new UpmsUser();
        param.setUserId(userId);
        param.setStatus(status);
        upmsUserMapper.dynamicUpdateModelById(param);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateLoginTime(Long userId, String lastLoginTime) {
        ValidationAssert.notNull(userId, "用户id不能为空!");
        ValidationAssert.notEmpty(lastLoginTime, "登录时间不能为空!");
        UpmsUser param = new UpmsUser();
        param.setUserId(userId);
        param.setLastLoginTime(lastLoginTime);
        upmsUserMapper.dynamicUpdateModelById(param);
    }

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updatePassword(UpmsUser user, boolean forceUpdate) {
        ValidationAssert.notNull(user, "参数不能为空!");
        ValidationAssert.notNull(user.getUserId(), "用户ID不能为空!");
        ValidationAssert.notEmpty(user.getPassword(), "新密码不能为空!");
        ValidationAssert.notEmpty(user.getRepassword(), "重复新密码不能为空!");
        ValidationAssert.isTrue(user.getPassword().equals(user.getRepassword()), "两次输入新密码不一致!");
        UpmsUser puser = upmsUserMapper.selectModelById(user.getUserId());
        ValidationAssert.notNull(puser, "该用户已经不存在了!");
        if (!forceUpdate) {
            ValidationAssert.notNull(user.getOldpassword(), "旧密码不能为空!");
            ValidationAssert.isTrue(UserPasswordUtils.matches(user.getOldpassword(), puser.getPassword()), "旧密码不正确!");
        }
        UpmsUser parameter = new UpmsUser();
        parameter.setUserId(user.getUserId());
        parameter.setPassword(UserPasswordUtils.encode(user.getPassword()));
        parameter.setUpdateTime(DateTimeUtils.formatNow());
        upmsUserMapper.dynamicUpdateModelById(parameter);
    }

	@Override
	public UpmsUser getUserById(Long userId) {
		return ModelDecodeUtils.decodeModel(upmsUserMapper.selectModelById(userId));
	}

	@Override
	public UpmsUser getUserByUserName(String userName) {
		if(!StringUtils.isEmpty(userName)) {
			UpmsUser example = new UpmsUser();
			example.setUserName(userName);
			return ModelDecodeUtils.decodeModel(upmsUserMapper.selectModelByExample(example));
		}
		return null;
	}

	@Override
	public List<UpmsUser> getUserListByPage(UpmsUser condition, Page page, Sort sort) {
		List<UpmsUser> dataList = ModelDecodeUtils.decodeModel(upmsUserMapper.selectModelPageListByExample(condition, sort, new RowBounds(page.getOffset(), page.getLimit())));
    	page.setTotalRowCount(upmsUserMapper.countModelPageListByExample(condition));
		return dataList;
	}

	@Override
	public List<UpmsRole> getUserRoleList(Long userId, Long appId, UpmsRole condition) {
		return ModelDecodeUtils.decodeModel(upmsUserMapper.selectUserRoleList(userId, appId, condition));
	}

	@Override
	public List<UpmsResource> getUserResourceList(Long userId, Long appId, Integer actionType) {
		return ModelDecodeUtils.decodeModel(upmsUserMapper.selectUserResourceList(userId, appId, actionType));
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void addUserRoles(UpmsUser user, List<Long> roleIdList, Long createBy, String createTime) {
		ValidationAssert.notNull(user, "参数不能为空!");
        ValidationAssert.notNull(user.getUserId(), "用户ID不能为空!");
        ValidationAssert.notEmpty(roleIdList, "角色ID列表不能为空!");
        UpmsUser puser = upmsUserMapper.selectModelById(user.getUserId());
        ValidationAssert.notNull(puser, "该用户已经不存在了!");
        user.setUserName(puser.getUserName());
        upmsUserMapper.insertUserRoles(user.getUserId(), roleIdList, createBy, createTime);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void delUserRoles(UpmsUser user, List<Long> roleIdList) {
		ValidationAssert.notNull(user, "参数不能为空!");
		ValidationAssert.notNull(user.getUserId(), "用户ID不能为空!");
		ValidationAssert.notEmpty(roleIdList, "角色ID列表不能为空!");
        UpmsUser puser = upmsUserMapper.selectModelById(user.getUserId());
        ValidationAssert.notNull(puser, "该用户已经不存在了!");
        user.setUserName(puser.getUserName());
        upmsUserMapper.deleteUserRoles(user.getUserId(), roleIdList);
    }
	
	@Override
	public List<UpmsResource> getUserIndexResourceList(Long userId) {
		return ModelDecodeUtils.decodeModel(upmsUserMapper.selectUserIndexResourceList(userId));
	}
	
}
