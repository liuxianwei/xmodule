package com.penglecode.xmodule.newscloud.usercenter.service.api;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.penglecode.xmodule.common.consts.ApplicationConstants;
import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.support.BusinessAssert;
import com.penglecode.xmodule.common.support.Page;
import com.penglecode.xmodule.common.support.PageResult;
import com.penglecode.xmodule.common.support.Result;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.ModelDecodeUtils;
import com.penglecode.xmodule.common.util.SpringBeanUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.web.support.HttpAPIResourceSupport;
import com.penglecode.xmodule.newscloud.usercenter.consts.em.UserStatusEnum;
import com.penglecode.xmodule.newscloud.usercenter.mapper.UserFollowerMapper;
import com.penglecode.xmodule.newscloud.usercenter.mapper.UserMapper;
import com.penglecode.xmodule.newscloud.usercenter.model.User;
import com.penglecode.xmodule.newscloud.usercenter.model.UserFollower;

@RestController("defaultUserApiService")
public class UserApiServiceImpl extends HttpAPIResourceSupport implements UserApiService {

	public static final Consumer<User> USER_DECODER = model -> {
        if (model.getStatus() != null) {
            UserStatusEnum em = UserStatusEnum.getStatus(model.getStatus());
            if (em != null) {
                model.setStatusName(em.getStatusName());
            }
        }
        if (!StringUtils.isEmpty(model.getUserIconUrl())) {
        	model.setUserIconUrl(ApplicationConstants.GLOBAL_APP_CONFIG.getGlobalFileServerUrl() + model.getUserIconUrl());
        }
	};
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private UserFollowerMapper userFollowerMapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Result<Long> registerUser(@RequestBody User user) {
		ValidationAssert.notNull(user, "参数不能为空!");
		ValidationAssert.notEmpty(user.getUserName(), "用户名不能为空!");
		ValidationAssert.notEmpty(user.getNickName(), "用户昵称不能为空!");
		ValidationAssert.notEmpty(user.getMobilePhone(), "手机号码不能为空!");
		ValidationAssert.notEmpty(user.getEmail(), "Email不能为空!");
		user.setStatus(UserStatusEnum.ENABLED.getStatusCode());
		user.setUserIconUrl(StringUtils.defaultIfEmpty(user.getUserIconUrl(), GlobalConstants.DEFAULT_USER_AVATAR));
		user.setCreateTime(DateTimeUtils.formatNow());
		try {
			userMapper.insertModel(user);
		} catch (DuplicateKeyException e) {
            BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("USER_NAME"), "对不起,该用户名已存在!");
            throw e;
        }
		return Result.success().message("OK").data(user.getUserId()).build();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Result<Object> updateUser(@RequestBody User user) {
		ValidationAssert.notNull(user, "参数不能为空!");
		ValidationAssert.notEmpty(user.getUserId(), "用户ID不能为空!");
		user.setUpdateTime(DateTimeUtils.formatNow());
		try {
			userMapper.updateModelById(user);
		} catch (DuplicateKeyException e) {
            BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("USER_NAME"), "对不起,该用户名已存在!");
            throw e;
        }
		return Result.success().message("OK").build();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Result<Object> deleteUserById(@PathVariable("userId") Long userId) {
		ValidationAssert.notEmpty(userId, "用户ID不能为空!");
		userMapper.deleteModelById(userId);
		return Result.success().message("OK").build();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Result<Object> updateUserStatus(@PathVariable("userId") Long userId, @PathVariable("status") Integer status) {
		ValidationAssert.notEmpty(userId, "用户ID不能为空!");
		ValidationAssert.notEmpty(UserStatusEnum.getStatus(status), "用户状态不能为空!");
		User param = new User();
		param.setUserId(userId);
		param.setStatus(status);
		userMapper.dynamicUpdateModelById(param);
		return Result.success().message("OK").build();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Result<Object> authorizeUser(@RequestBody User user) {
		ValidationAssert.notNull(user, "参数不能为空!");
		ValidationAssert.notEmpty(user.getUserId(), "用户ID不能为空!");
		ValidationAssert.notEmpty(user.getAuthIdcard(), "身份证号码不能为空!");
		ValidationAssert.notEmpty(user.getAuthRealName(), "真实姓名不能为空!");
		user.setAuthorized(true);
		User param = new User();
		param.setUserId(user.getUserId());
		param.setAuthorized(true);
		param.setAuthIdcard(user.getAuthIdcard());
		param.setAuthRealName(user.getAuthRealName());
		userMapper.dynamicUpdateModelById(param);
		return Result.success().message("OK").build();
	}

	@Override
	public Result<User> getUserById(@PathVariable("userId") Long userId) {
		User data = ModelDecodeUtils.decodeModel(userMapper.selectModelById(userId), USER_DECODER);
		data.setServiceProvider(getEnvironment().getProperty("eureka.instance.instanceId"));
		return Result.success().message("OK").data(data).build();
	}

	@Override
	public Result<User> getUserByUserName(@PathVariable("userName") String userName) {
		User example = new User();
		example.setUserName(userName);
		User data = ModelDecodeUtils.decodeModel(userMapper.selectModelByExample(example), USER_DECODER);
		return Result.success().message("OK").data(data).build();
	}

	@Override
	public PageResult<List<User>> getUserListByPage1(User condition, Page page, Sort sort) {
		List<User> dataList = ModelDecodeUtils.decodeModel(userMapper.selectModelPageListByExample(condition, sort, new RowBounds(page.getOffset(), page.getLimit())), USER_DECODER);
		page.setTotalRowCount(userMapper.countModelPageListByExample(condition));
		return PageResult.success().message("OK").data(dataList).totalRowCount(page.getTotalRowCount()).build();
	}

	@Override
	public PageResult<List<User>> getUserListByPage2(@RequestParam Map<String, Object> parameter) {
		User condition = new User();
		Page page = Page.of(1, 10);
		Sort sort = Sort.by();
		SpringBeanUtils.setBeanProperty(Arrays.asList(condition, page, sort), parameter);
		
		List<User> dataList = ModelDecodeUtils.decodeModel(userMapper.selectModelPageListByExample(condition, sort, new RowBounds(page.getOffset(), page.getLimit())), USER_DECODER);
		page.setTotalRowCount(userMapper.countModelPageListByExample(condition));
		return PageResult.success().message("OK").data(dataList).totalRowCount(page.getTotalRowCount()).build();
	}

	@Override
	public Result<List<User>> getUserListByStatus(Boolean authorized, Integer... statuses) {
		List<User> dataList = ModelDecodeUtils.decodeModel(userMapper.selectUserListByStatus(authorized, Arrays.asList(statuses)), USER_DECODER);
		return Result.success().message("OK").data(dataList).build();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Result<Object> followUser(@RequestBody UserFollower userFollower) {
		ValidationAssert.notNull(userFollower, "参数不能为空!");
		ValidationAssert.notEmpty(userFollower.getUserId(), "用户ID不能为空!");
		ValidationAssert.notEmpty(userFollower.getFollowerUserId(), "被关注用户ID不能为空!");
		userFollower.setFollowTime(DateTimeUtils.formatNow());
		try {
			userFollowerMapper.insertModel(userFollower);
		} catch (DuplicateKeyException e) {
	        BusinessAssert.isTrue(false, "对不起,您已关注过该用户了!");
	        throw e;
	    }
		return Result.success().message("OK").build();
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public Result<Object> unfollowUser(@RequestBody UserFollower userFollower) {
		ValidationAssert.notNull(userFollower, "参数不能为空!");
		ValidationAssert.notEmpty(userFollower.getUserId(), "用户ID不能为空!");
		ValidationAssert.notEmpty(userFollower.getFollowerUserId(), "被关注用户ID不能为空!");
		userFollowerMapper.deleteUserFollower(userFollower.getUserId(), userFollower.getFollowerUserId());
		return Result.success().message("OK").build();
	}

}
