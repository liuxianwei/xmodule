package com.penglecode.xmodule.springsecurity.upms.service.impl;

import java.util.List;
import java.util.function.Consumer;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.support.BusinessAssert;
import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.support.Sort.Order;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.ModelDecodeUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.security.util.UserPasswordUtils;
import com.penglecode.xmodule.springsecurity.upms.consts.em.UpmsUserStatusEnum;
import com.penglecode.xmodule.springsecurity.upms.consts.em.UpmsUserTypeEnum;
import com.penglecode.xmodule.springsecurity.upms.mapper.UpmsUserMapper;
import com.penglecode.xmodule.springsecurity.upms.model.UpmsRole;
import com.penglecode.xmodule.springsecurity.upms.model.UpmsUser;
import com.penglecode.xmodule.springsecurity.upms.service.UpmsUserService;

@Service("upmsUserService")
public class UpmsUserServiceImpl implements UpmsUserService {

	public static final Consumer<UpmsUser> UPMS_USER_DECODER = model -> {
        if (model.getStatus() != null) {
            UpmsUserStatusEnum em = UpmsUserStatusEnum.getStatus(model.getStatus());
            if (em != null) {
                model.setStatusName(em.getStatusName());
            }
        }
        if (model.getUserType() != null) {
            UpmsUserTypeEnum em = UpmsUserTypeEnum.getUserType(model.getUserType());
            if (em != null) {
                model.setUserTypeName(em.getTypeName());
            }
        }
        if (!StringUtils.isEmpty(model.getUserIcon())) {
        	model.setUserIconUrl(GlobalConstants.MVVM_WEBAPP_CONFIG.getGlobalFileServerUrl() + model.getUserIcon());
        }
	};
	
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
        upmsUserMapper.updateModelById(param);
	}

	@Override
	public UpmsUser getUserById(Long userId) {
		return ModelDecodeUtils.decodeModel(upmsUserMapper.selectModelById(userId), UPMS_USER_DECODER);
	}

	@Override
	public UpmsUser getUserByUserName(String userName) {
		if(!StringUtils.isEmpty(userName)) {
			UpmsUser example = new UpmsUser();
			example.setUserName(userName);
			return ModelDecodeUtils.decodeModel(upmsUserMapper.selectModelByExample(example), UPMS_USER_DECODER);
		}
		return null;
	}

	@Override
	public List<UpmsUser> getUserListByPage(UpmsUser condition, Page page, Sort sort) {
		List<UpmsUser> dataList = ModelDecodeUtils.decodeModel(upmsUserMapper.selectModelPageListByExample(condition, sort, new RowBounds(page.getOffset(), page.getLimit())), UPMS_USER_DECODER);
    	page.setTotalRowCount(upmsUserMapper.countModelPageListByExample(condition));
		return dataList;
	}

	@Override
	public List<UpmsUser> getAllUserList(Integer status) {
		UpmsUser example = new UpmsUser();
		example.setStatus(status);
		return ModelDecodeUtils.decodeModel(upmsUserMapper.selectModelListByExample(example, Sort.by(Order.asc("userId"))), UPMS_USER_DECODER);
	}

	@Override
	public List<UpmsRole> getUserRoleListByUserId(Long userId) {
		return upmsUserMapper.selectUserRoleListByUserId(userId);
	}

}
