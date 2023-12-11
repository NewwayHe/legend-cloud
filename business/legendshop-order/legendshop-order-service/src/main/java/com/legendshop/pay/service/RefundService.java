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
import com.legendshop.pay.dto.RefundDTO;
import com.legendshop.pay.dto.RefundResultDTO;
import com.legendshop.pay.entity.PayRefundSettlement;

import java.util.List;

/**
 * @author legendshop
 */
public interface RefundService {

	R<RefundResultDTO> refund(RefundDTO refundDTO);

	/**
	 * 根据已有的退款记录重新发起退款
	 */
	R<List<PayRefundSettlement>> refund(List<PayRefundSettlement> refundSettlementList);
}
