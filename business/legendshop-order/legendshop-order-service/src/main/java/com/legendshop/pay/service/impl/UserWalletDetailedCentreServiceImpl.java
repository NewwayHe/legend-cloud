/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service.impl;

import com.legendshop.common.core.service.impl.BaseServiceImpl;
import com.legendshop.pay.dao.UserWalletDetailedCentreDao;
import com.legendshop.pay.dto.UserWalletDetailedCentreDTO;
import com.legendshop.pay.enums.WalletCentreStatusEnum;
import com.legendshop.pay.service.UserWalletDetailedCentreService;
import com.legendshop.pay.service.convert.UserWalletDetailedCentreConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户钱包详情中间表(UserWalletDetailedCentre)表服务实现类
 *
 * @author legendshop
 * @since 2022-04-27 13:56:52
 */
@Service
public class UserWalletDetailedCentreServiceImpl extends BaseServiceImpl<UserWalletDetailedCentreDTO, UserWalletDetailedCentreDao, UserWalletDetailedCentreConverter> implements UserWalletDetailedCentreService {

	@Autowired
	private UserWalletDetailedCentreDao userWalletDetailedCentreDao;

	@Override
	public List<UserWalletDetailedCentreDTO> queryByStatus(WalletCentreStatusEnum status) {
		return userWalletDetailedCentreDao.queryByStatus(status);
	}
}
