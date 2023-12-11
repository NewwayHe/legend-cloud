/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.EntityCriterion;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.enums.DecorateActionSubTypeEnum;
import com.legendshop.basic.enums.DecorateActionTypeEnum;
import com.legendshop.common.core.constant.CommonConstants;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.shop.dao.AppStartAdvDao;
import com.legendshop.shop.dto.AppDecorateActionDTO;
import com.legendshop.shop.dto.AppStartAdvDTO;
import com.legendshop.shop.dto.AppStartAdvOnlineDTO;
import com.legendshop.shop.entity.AppStartAdv;
import com.legendshop.shop.query.AppStartAdvQuery;
import com.legendshop.shop.service.AppStartAdvService;
import com.legendshop.shop.service.convert.AppStartAdvConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * APP启动广告服务.
 *
 * @author legendshop
 */
@Service
public class AppStartAdvServiceImpl extends BaseServiceImpl<AppStartAdvDTO, AppStartAdvDao, AppStartAdvConverter> implements AppStartAdvService {

	@Autowired
	private AppStartAdvDao appStartAdvDao;

	@Autowired
	private AppStartAdvConverter appStartAdvConverter;

	@Override
	public AppStartAdvDTO getAppStartAdv(Long id) {
		return appStartAdvConverter.to(appStartAdvDao.getById(id));
	}

	@Override
	public int deleteAppStartAdv(Long id) {
		return appStartAdvDao.deleteById(id);
	}

	@Override
	public Long saveAppStartAdv(AppStartAdvDTO appStartAdv) {
		if (ObjectUtil.isNotEmpty(appStartAdv.getId())) {
			updateAppStartAdv(appStartAdv);
			return appStartAdv.getId();
		}
		return appStartAdvDao.save(appStartAdvConverter.from(appStartAdv));
	}

	@Override
	public R updateAppStartAdv(AppStartAdvDTO appStartAdv) {
		appStartAdvDao.update(appStartAdvConverter.from(appStartAdv));
		return R.ok();
	}


	@Override
	public int getAllCount() {
		return (int) appStartAdvDao.getCount();
	}

	@Override
	public boolean checkNameIsExits(String name) {
		int result = appStartAdvDao.getCount(name);
		return result > 0;
	}

	@Override
	public String getName(Long id) {

		return appStartAdvDao.getName(id);
	}

	@Override
	public List<AppStartAdvOnlineDTO> getOnlines() {

		List<AppStartAdvOnlineDTO> appStartAdvDtoList = new ArrayList<AppStartAdvOnlineDTO>();

		List<AppStartAdv> appStartAdvs = appStartAdvDao.queryByProperties(new EntityCriterion().eq("status", CommonConstants.STATUS_NORMAL).addDescOrder("createTime"));
		for (AppStartAdv appStartAdv : appStartAdvs) {

			AppStartAdvOnlineDTO appStartAdvDto = new AppStartAdvOnlineDTO();
			appStartAdvDto.setId(appStartAdv.getId());
			appStartAdvDto.setPhoto(appStartAdv.getImgUrl());

			AppDecorateActionDTO action = new AppDecorateActionDTO();
			action.setType(DecorateActionTypeEnum.PROD_DETAIL.value());
			action.setSubType(DecorateActionSubTypeEnum.OUT_URL.value());
			action.setTarget(appStartAdv.getUrl());

			appStartAdvDto.setAction(action);

			appStartAdvDtoList.add(appStartAdvDto);
		}

		return appStartAdvDtoList;
	}


	/**
	 * 启动广告分页查询
	 *
	 * @param appStartAdvQuery
	 * @return
	 */
	@Override
	public PageSupport<AppStartAdvDTO> page(AppStartAdvQuery appStartAdvQuery) {
		return appStartAdvConverter.page(appStartAdvDao.page(appStartAdvQuery));
	}

	/**
	 * 修改启动广告状态
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	@Override
	public boolean updateStatus(Long id, Integer status) {
		int result = appStartAdvDao.updateStatus(id, status);
		return result > 0;
	}

	@Override
	public AppStartAdvDTO getRandomAppStartAdv() {
		//获取所有已上线的app启动广告
		EntityCriterion criterion = new EntityCriterion();
		criterion.eq("status", CommonConstants.STATUS_NORMAL);
		List<AppStartAdvDTO> appStartAdvList = appStartAdvConverter.to(appStartAdvDao.queryByProperties(criterion));
		//随机获取一天app启动广告
		AppStartAdvDTO appStartAdvDTO = null;
		if (ObjectUtil.isNotEmpty(appStartAdvList)) {
			Random random = new Random();
			int index = random.nextInt(appStartAdvList.size());
			appStartAdvDTO = appStartAdvList.get(index);
		}
		return appStartAdvDTO;
	}

	@Override
	public R deleteAppAdv(Long id) {
		AppStartAdv appStartAdv = appStartAdvDao.getById(id);
		if (ObjectUtil.isNull(appStartAdv)) {
			return R.fail("app启动广告不存在!");
		}
		if (Objects.equals(appStartAdv.getStatus(), 1)) {
			return R.fail("当前app启动广告上线状态,无法删除!");
		}
		appStartAdvDao.deleteById(id);
		return R.ok(true);
	}
}
