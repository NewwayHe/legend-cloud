/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.order.bo.ConfirmOrderBO;
import com.legendshop.order.dto.ConfirmOrderDTO;
import com.legendshop.order.dto.InvoiceChangeDTO;
import com.legendshop.order.dto.UseWalletDTO;
import com.legendshop.user.bo.UserInvoiceBO;

/**
 * 订单核心业务服务
 *
 * @author legendshop
 */
public interface OrderBusinessService {


	/**
	 * 获取确认订单信息
	 *
	 * @param confirmOrderDTO
	 * @return
	 */
	R<ConfirmOrderBO> getOrderInfo(ConfirmOrderBO confirmOrderDTO);


	/**
	 * 提交订单
	 *
	 * @param confirmOrderBO
	 * @return
	 */
	R submitOrder(ConfirmOrderBO confirmOrderBO);

	/**
	 * 下单前检查以及组装确认订单商品信息
	 *
	 * @param confirmOrderDTO
	 * @return
	 */
	R<ConfirmOrderBO> checkAndAssemblyConfirmOrder(ConfirmOrderDTO confirmOrderDTO);


	/**
	 * 确认订单，更新发票信息
	 *
	 * @param invoiceChangeDTO 入参DTO
	 * @param confirmOrderBo
	 * @return
	 */
	R<UserInvoiceBO> invoiceChange(InvoiceChangeDTO invoiceChangeDTO, ConfirmOrderBO confirmOrderBo);

	/**
	 * 使用用户钱包余额抵扣处理
	 */
	R<ConfirmOrderBO> useWallet(Long userId, UseWalletDTO dto);

	/**
	 * 使用积分开关切换
	 *
	 * @param integralFlag
	 * @param confirmOrderBo
	 */
	void switchIntegralFlag(Boolean integralFlag, ConfirmOrderBO confirmOrderBo);

	/**
	 * 计算预售
	 *
	 * @param confirmOrderBo
	 */
	void executeSpecificBusinessStrategy(ConfirmOrderBO confirmOrderBo);
}
