/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.service;

import com.legendshop.shop.enums.DealTypeEnum;

import java.math.BigDecimal;

/**
 * 平台资金流水策略
 *
 * @author legendshop
 */
public interface PlateCapitalFlowStrategy {

	/**
	 * 拼接备注
	 *
	 * @param
	 * @param amount
	 * @param userId
	 * @param orderNumber
	 * @return
	 */
	String getRemark(BigDecimal amount, Long userId, String orderNumber, String payType, String flag);


	/**
	 * 共有退款信息
	 *
	 * @param sb
	 * @param typeEnum
	 * @param amount
	 * @param userId
	 * @param orderNumber
	 */
	default void setRefundRemark(StringBuffer sb, DealTypeEnum typeEnum, BigDecimal amount, Long userId, String orderNumber, String payType) {
		sb.append(typeEnum.getDes());
		sb.append("，用户ID：");
		sb.append(userId);
		sb.append("，退款金额：");
		sb.append(amount);
		sb.append("，退款方式：");
		sb.append(payType);
		sb.append("，订单编号：");
		sb.append(orderNumber);
	}

	/**
	 * 公有支付信息
	 *
	 * @param sb
	 * @param typeEnum
	 * @param amount
	 * @param userId
	 * @param orderNumber
	 */
	default void setPayRemark(StringBuffer sb, DealTypeEnum typeEnum, BigDecimal amount, Long userId, String orderNumber, String payType) {
		sb.append(typeEnum.getDes());
		sb.append("，用户ID：");
		sb.append(userId);
		sb.append("，支付金额：");
		sb.append(amount);
		sb.append("，支付方式");
		sb.append(payType);
		sb.append("，订单编号");
		sb.append(orderNumber);
	}
}
