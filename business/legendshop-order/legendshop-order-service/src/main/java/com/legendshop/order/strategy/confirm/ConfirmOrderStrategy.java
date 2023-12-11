/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.strategy.confirm;


import com.legendshop.common.core.constant.R;
import com.legendshop.order.bo.ConfirmOrderBO;
import com.legendshop.order.dto.ConfirmOrderDTO;

/**
 * 确认订单策略
 *
 * @author legendshop
 */
public interface ConfirmOrderStrategy {

	/**
	 * 执行确认订单策略
	 *
	 * @param confirmOrderDTO 确认订单DTO
	 * @return R
	 */
	R<ConfirmOrderBO> confirm(ConfirmOrderBO confirmOrderDTO);

	/**
	 * 执行下单检查组装商品信息策略
	 *
	 * @param confirmOrderDTO 确认订单DTO
	 * @return R
	 */
	R<ConfirmOrderBO> check(ConfirmOrderDTO confirmOrderDTO);


	/**
	 * 处理积分抵扣
	 *
	 * @param confirmOrderBO
	 * @return
	 */
	R<ConfirmOrderBO> handleSpecificBusiness(ConfirmOrderBO confirmOrderBO);
}
