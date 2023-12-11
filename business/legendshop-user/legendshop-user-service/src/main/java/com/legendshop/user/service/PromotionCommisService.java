/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.service;

import com.legendshop.user.dto.UserDetailDTO;

/**
 * 推广员订单佣金Service
 *
 * @author legendshop
 */
public interface PromotionCommisService {

	/**
	 * 推广员 推广注册成功
	 */
	void handlePromotionReg(UserDetailDTO userDetail);

	/** 订单佣金结算 */
//	 void settleOrderCommis(SubItem subItem);
//
//	/** 订单分配佣金 ---》 支持分销的订单项 */
//	 void handleOrderCommis(List<SubItem> subItems, Long userId);
//
//	/** 订单佣金回退 */
//	 void cancleOrderCommis(SubItem subItem);

}
