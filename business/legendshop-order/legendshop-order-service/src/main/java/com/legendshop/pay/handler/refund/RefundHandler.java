/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.refund;

import com.legendshop.common.core.constant.R;
import com.legendshop.pay.dto.RefundRequestDTO;
import com.legendshop.pay.dto.RefundResultItemDTO;

/**
 * @author legendshop
 */
public interface RefundHandler {

	R<RefundResultItemDTO> refund(RefundRequestDTO refundRequest);

	boolean isSupport(String payType, String paySource);

	R<Void> queryRefundResult(RefundRequestDTO refundItem);
}
