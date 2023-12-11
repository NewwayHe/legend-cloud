/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.PayParamsDTO;
import com.legendshop.pay.dto.PaySettlementDTO;
import com.legendshop.pay.dto.PaySettlementSuccessDTO;
import com.legendshop.pay.entity.PaySettlement;

import java.util.List;

/**
 * @author legendshop
 */
public interface PaySettlementService {

	List<PaySettlementDTO> queryPaidBySnList(List<String> snList);

	/**
	 * 根据订单号获取已支付的支付单
	 *
	 * @param orderNumber
	 * @return
	 */
	PaySettlementDTO getPaidByOrderNumber(String orderNumber);


	/**
	 * 根据订单号List获取已支付的支付单
	 *
	 * @param orderNumbers
	 * @return
	 */
	List<PaySettlementDTO> getPaidByOrderNumberList(List<String> orderNumbers);

	PaySettlementDTO getBySn(String settlementSn);

	/**
	 * 更新支付结算信息
	 *
	 * @param paySettlement 支付结算信息
	 * @return 更新结果
	 */
	int updateSettlement(PaySettlementDTO paySettlement);

	/**
	 * 检查是否重复支付
	 *
	 * @param payParamsDTO 支付参数信息
	 * @return 重复支付检查结果
	 */
	R<Void> checkRepeatPay(PayParamsDTO payParamsDTO);

	/**
	 * 保存支付结算信息
	 *
	 * @param settlementDTO 结算信息
	 * @return 是否成功保存
	 */
	boolean saveSettlement(PaySettlementDTO settlementDTO);

	R<PaySettlementSuccessDTO> success(String settlementSn);

	R<PaySettlementSuccessDTO> orderSuccess(List<String> orderNumberList);

	/**
	 * 查询支付成功但订单待付款的订单
	 *
	 * @return
	 */
	List<PaySettlement> queryPaySuccessfulButOrderUnPaid();
}
