/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao;


import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.pay.entity.PaySettlement;

import java.util.List;

/**
 * 订单结算票据中心
 *
 * @author legendshop
 */
public interface PaySettlementDao extends GenericDao<PaySettlement, Long> {

	List<PaySettlement> queryPaidBySnList(List<String> snList);


	PaySettlement getBySn(String settlementSn);

	/**
	 * 检查订单是否重复支付
	 *
	 * @param orderNumber
	 * @param settlementType
	 * @param userId
	 * @return
	 */
	long checkRepeatPay(String orderNumber, String settlementType, Long userId);

	List<PaySettlement> queryPaySuccessfulButOrderUnPaid();

	/**
	 * 根据订单号获取支付成功的支付单
	 *
	 * @param orderNumber
	 * @return
	 */
	PaySettlement getPaidByOrderNumber(String orderNumber);

	/**
	 * 根据订单号列表，查询当前合单支付的所有订单，是否存在有订单已经支付
	 *
	 * @param snList
	 * @return
	 */
	List<PaySettlement> getPaidByOrderNumberList(List<String> orderNumbers);
}
