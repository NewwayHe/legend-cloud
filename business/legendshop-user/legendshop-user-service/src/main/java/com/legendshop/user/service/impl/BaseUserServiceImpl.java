/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import com.legendshop.common.core.enums.UserTypeEnum;
import com.legendshop.user.dao.OrdinaryUserDao;
import com.legendshop.user.dao.ShopSubUserDao;
import com.legendshop.user.dao.ShopUserDao;
import com.legendshop.user.dto.BaseUserQuery;
import com.legendshop.user.dto.SysUserDTO;
import com.legendshop.user.entity.OrdinaryUser;
import com.legendshop.user.entity.ShopUser;
import com.legendshop.user.service.BaseUserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author legendshop
 */
@Service
@RequiredArgsConstructor
public class BaseUserServiceImpl implements BaseUserService {

	final ShopUserDao shopUserDao;
	final ShopSubUserDao shopSubUserDao;
	final OrdinaryUserDao ordinaryUserDao;

	@Override
	public String getMobile(BaseUserQuery query) {
		if (StringUtils.isBlank(query.getUserType()) || null == query.getUserId()) {
			return null;
		}
		if (!UserTypeEnum.contains(query.getUserType())) {
			return null;
		}

		String mobile = null;
		switch (UserTypeEnum.valueOf(query.getUserType())) {
			case USER:
				mobile = this.ordinaryUserDao.getMobileByUserId(query.getUserId());
				break;
			case SHOP:
				mobile = this.shopUserDao.getMobileByUserId(query.getUserId());
				break;
		}

		return mobile;
	}

	@Override
	public SysUserDTO getByMobile(BaseUserQuery query) {
		if (StringUtils.isBlank(query.getUserType()) || null != query.getMobile()) {
			return null;
		}
		if (!UserTypeEnum.contains(query.getUserType())) {
			return null;
		}
		String mobile = query.getMobile();
		SysUserDTO resultUser = new SysUserDTO();
		resultUser.setMobile(mobile);
		switch (UserTypeEnum.valueOf(query.getUserType())) {
			case USER:
				OrdinaryUser ordinaryUser = this.ordinaryUserDao.getByMobile(mobile);
				if (null == ordinaryUser) {
					return null;
				}
				resultUser.setId(ordinaryUser.getId());
				resultUser.setUsername(ordinaryUser.getUsername());
				resultUser.setDelFlag(ordinaryUser.getDelFlag());
				resultUser.setLockFlag(ordinaryUser.getLockFlag());
				break;
			case SHOP:
				ShopUser shopUser = this.shopUserDao.getByMobile(mobile);
				if (null == shopUser) {
					return null;
				}
				resultUser.setId(shopUser.getId());
				resultUser.setUsername(shopUser.getUsername());
				resultUser.setDelFlag(shopUser.getDelFlag());
				resultUser.setLockFlag(shopUser.getLockFlag());
				break;
		}
		return resultUser;
	}

	@Override
	public boolean isMobileExist(BaseUserQuery query) {

		boolean result = false;

		if (StringUtils.isBlank(query.getUserType()) || null == query.getMobile()) {
			return false;
		}
		if (!UserTypeEnum.contains(query.getUserType())) {
			return false;
		}
		String mobile = query.getMobile();
		switch (UserTypeEnum.valueOf(query.getUserType())) {
			case USER:
				result = this.ordinaryUserDao.isMobileExist(mobile);
				break;
			case SHOP:
				result = this.shopUserDao.isMobileExist(mobile);
				break;
		}
		return result;
	}

}
