/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service;

import com.legendshop.common.core.service.BaseService;
import com.legendshop.pay.dto.UserWalletDetailedCentreDTO;
import com.legendshop.pay.enums.WalletCentreStatusEnum;

import java.util.List;

/**
 * 用户钱包详情中间表(UserWalletDetailedCentre)表服务接口
 *
 * @author legendshop
 * @since 2022-04-27 13:56:51
 */
public interface UserWalletDetailedCentreService extends BaseService<UserWalletDetailedCentreDTO> {


	/**
	 * 根据状态查询钱包中间表
	 *
	 * @param status
	 * @return
	 */
	List<UserWalletDetailedCentreDTO> queryByStatus(WalletCentreStatusEnum status);

}
