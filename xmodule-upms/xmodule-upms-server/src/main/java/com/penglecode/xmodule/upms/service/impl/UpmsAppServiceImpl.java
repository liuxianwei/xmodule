package com.penglecode.xmodule.upms.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.penglecode.xmodule.common.consts.GlobalConstants;
import com.penglecode.xmodule.common.support.BusinessAssert;
import com.penglecode.xmodule.common.support.Sort;
import com.penglecode.xmodule.common.support.Sort.Order;
import com.penglecode.xmodule.common.support.ValidationAssert;
import com.penglecode.xmodule.common.util.DateTimeUtils;
import com.penglecode.xmodule.common.util.ModelDecodeUtils;
import com.penglecode.xmodule.common.util.ObjectUtils;
import com.penglecode.xmodule.common.util.StringUtils;
import com.penglecode.xmodule.common.util.UUIDUtils;
import com.penglecode.xmodule.upms.consts.em.UpmsAppTypeEnum;
import com.penglecode.xmodule.upms.consts.em.UpmsResourceActionTypeEnum;
import com.penglecode.xmodule.upms.consts.em.UpmsResourceTypeEnum;
import com.penglecode.xmodule.upms.mapper.UpmsAppMapper;
import com.penglecode.xmodule.upms.mapper.UpmsResourceMapper;
import com.penglecode.xmodule.upms.mapper.UpmsRoleMapper;
import com.penglecode.xmodule.upms.model.UpmsApp;
import com.penglecode.xmodule.upms.model.UpmsResource;
import com.penglecode.xmodule.upms.service.UpmsAppService;

@Service("upmsAppService")
public class UpmsAppServiceImpl implements UpmsAppService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpmsAppServiceImpl.class);
	
	@Autowired
	private UpmsAppMapper upmsAppMapper;
	
	@Autowired
	private UpmsRoleMapper upmsRoleMapper;
	
	@Autowired
	private UpmsResourceMapper upmsResourceMapper;
	
	@Override
	public List<UpmsApp> getAppList(UpmsApp condition, Sort sort) {
		return ModelDecodeUtils.decodeModel(upmsAppMapper.selectModelListByExample(condition, sort));
	}

	@Override
	public List<UpmsApp> getAllAppList(Boolean enabled) {
		UpmsApp example = new UpmsApp();
		example.setEnabled(enabled);
		return ModelDecodeUtils.decodeModel(upmsAppMapper.selectModelListByExample(example, Sort.by(Order.desc("appId"))));
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void createApp(UpmsApp app) {
		ValidationAssert.notNull(app, "参数不能为空!");
        try {
			app.setAppType(UpmsAppTypeEnum.USER_TYPE_NORMAL.getTypeCode());
			app.setCreateBy(ObjectUtils.defaultIfNull(app.getCreateBy(), GlobalConstants.DEFAULT_SUPER_ADMIN_USER_ID));
			app.setCreateTime(DateTimeUtils.formatNow());
			app.setEnabled(Boolean.TRUE);
			app.setAppKey(StringUtils.defaultIfEmpty(app.getAppKey(), UUIDUtils.uuid()));
			app.setAppSecret(StringUtils.defaultIfEmpty(app.getAppSecret(), UUIDUtils.uuid()));
			upmsAppMapper.insertModel(app);
			
			try { //自动添加新增应用的根节点资源
				UpmsResource appRootResource = new UpmsResource();
				appRootResource.setResourceId(null);
				appRootResource.setResourceName(app.getAppName());
				appRootResource.setParentResourceId(app.getAppId());
				appRootResource.setAppRootResource(Boolean.TRUE);
				appRootResource.setResourceType(UpmsResourceTypeEnum.RESOURCE_TYPE_NORMAL.getTypeCode());
				appRootResource.setActionType(UpmsResourceActionTypeEnum.RESOURCE_ACTION_TYPE_MENU.getTypeCode());
				appRootResource.setResourceUrl(null);
				appRootResource.setPermissionExpression(null);
				appRootResource.setSiblingsIndex(app.getAppId().intValue());
				appRootResource.setResourceIcon("fa-cloud");
				appRootResource.setIndexPage(Boolean.FALSE);
				appRootResource.setAppId(app.getAppId());
				appRootResource.setCreateBy(app.getCreateBy());
				appRootResource.setCreateTime(DateTimeUtils.formatNow());
				upmsResourceMapper.insertModel(appRootResource);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
        } catch (DuplicateKeyException e) {
            BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("APP_NAME"), String.format("对不起,应用名称(%s)已存在!", app.getAppName()));
            throw e;
        }
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void updateApp(UpmsApp app) {
		ValidationAssert.notNull(app, "参数不能为空!");
		ValidationAssert.notNull(app.getAppId(), "应用ID不能为空!");
        try {
			upmsAppMapper.updateModelById(app);
        } catch (DuplicateKeyException e) {
            BusinessAssert.isTrue(!e.getCause().getMessage().toUpperCase().contains("APP_NAME"), String.format("对不起,应用名称(%s)已存在!", app.getAppName()));
            throw e;
        }
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void updateAppStatus(Long appId, boolean targetStatus) {
		 ValidationAssert.notNull(appId, "应用ID不能为空!");
		 UpmsApp app = new UpmsApp();
		 app.setAppId(appId);
		 app.setEnabled(targetStatus);
		 upmsAppMapper.dynamicUpdateModelById(app);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deleteAppById(Long appId) {
		 ValidationAssert.notNull(appId, "应用ID不能为空!");
		 UpmsApp papp = upmsAppMapper.selectModelById(appId);
		 ValidationAssert.notNull(papp, "该应用已经不存在了!");
		 BusinessAssert.isTrue(!UpmsAppTypeEnum.APP_TYPE_CORE.getTypeCode().equals(papp.getAppType()), "核心应用不能被删除!");
		 int count = 0;
		 count = upmsRoleMapper.selectAllRoleCountByAppId(appId);
		 ValidationAssert.isTrue(count == 0, "对不起,该应用被某角色关联使用不能被删除!");
		 count = upmsResourceMapper.selectAllResourceCountByAppId(appId);
		 ValidationAssert.isTrue(count <= 1, "对不起,该应用被某资源关联使用不能被删除!");
		 upmsAppMapper.deleteModelById(appId);
	}

}
