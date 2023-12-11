/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.legendshop.user.bo.UserAddressBO;
import com.legendshop.user.bo.UserSecureBO;
import com.legendshop.user.dao.UserAddressDao;
import com.legendshop.user.dao.UserCenterPersonInfoDao;
import com.legendshop.user.dto.MyPersonInfoDTO;
import com.legendshop.user.dto.PassportDTO;
import com.legendshop.user.dto.UserDistributionInfoDTO;
import com.legendshop.user.service.PassportService;
import com.legendshop.user.service.UserCenterPersonInfoService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 普通用户信息
 *
 * @author legendshop
 */
@Service
public class OrdinaryUserCenterPersonInfoServiceImpl implements UserCenterPersonInfoService {

	@Autowired
	private UserAddressDao userAddressDao;

	@Autowired
	private PassportService passportService;

	@Resource(name = "ordinaryUserCenterPersonInfoDaoImpl")
	private UserCenterPersonInfoDao userCenterPersonInfoDao;

	@Override
	public MyPersonInfoDTO getUserCenterPersonInfo(Long userId) {
		// 获取用户基本信息
		MyPersonInfoDTO dto = userCenterPersonInfoDao.getPersonInfo(userId);
		if (ObjectUtil.isEmpty(dto)) {
			return dto;
		}
		// 用户分销状态
		dto.setDistribution(new UserDistributionInfoDTO());


		// 获取用户默认地址
		UserAddressBO defaultAddress = userAddressDao.getDefaultAddress(userId);
		dto.setDefaultAddress(defaultAddress);

		// 获取微信头像
		if (StrUtil.isBlank(dto.getPortraitPic())) {
			PassportDTO passportDTO = passportService.getByUserId(userId);
			dto.setPortraitPic(Optional.ofNullable(passportDTO).map(PassportDTO::getHeadPortraitUrl).orElse(null));
		}

		return dto;
	}

	@Override
	public void updateUserInfo(MyPersonInfoDTO myPersonInfo) {

	}

	@Override
	public int updateNickNameByUserId(Long userId, String nickName) {
		return userCenterPersonInfoDao.updateNickNameByUserId(userId, nickName);
	}

	@Override
	public int updateSexByUserId(Long userId, String sex) {
		return userCenterPersonInfoDao.updateSexByUserId(userId, sex);
	}


	@Override
	public UserSecureBO getUserSecureByUserId(Long userId) {
		UserSecureBO secure = userCenterPersonInfoDao.getUserSecureByUserId(userId);
		if (StrUtil.isNotBlank(secure.getPassword())) {
			secure.setSetPasswordFlag(true);
			secure.setPassword("");
		}
		if (StrUtil.isNotBlank(secure.getPayPassword())) {
			secure.setSetPayPasswordFlag(true);
			secure.setPayPassword("");
		}
		return secure;
	}
}
