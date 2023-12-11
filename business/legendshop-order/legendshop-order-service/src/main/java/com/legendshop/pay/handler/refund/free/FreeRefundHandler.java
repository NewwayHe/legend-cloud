/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.refund.free;

import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.order.enums.PayTypeEnum;
import com.legendshop.pay.config.AliPayCommonSDKFactory;
import com.legendshop.pay.config.AliPaySDKFactory;
import com.legendshop.pay.dto.RefundRequestDTO;
import com.legendshop.pay.dto.RefundResultItemDTO;
import com.legendshop.pay.handler.refund.RefundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author legendshop
 */
@Slf4j
@RequiredArgsConstructor
@Component("FREE_PAY_REFUND")
public class FreeRefundHandler implements RefundHandler {

	final AliPaySDKFactory aliPaySDKFactory;

	final AliPayCommonSDKFactory aliPayCommonSDKFactory;

	/**
	 * 成功
	 */
	public static final String SUCCESS = "10000";


	@Override
	public R<RefundResultItemDTO> refund(RefundRequestDTO refundItem) {
		RefundResultItemDTO refundResultItem = new RefundResultItemDTO();
		refundResultItem.setPayTypeId(SysParamNameEnum.FREE_PAY.name());
		refundResultItem.setAmount(refundItem.getRefundAmount());
		return R.ok(refundResultItem);
	}

	@Override
	public boolean isSupport(String payType, String paySource) {
		return PayTypeEnum.FREE_PAY.name().equals(payType);
	}

	@Override
	public R<Void> queryRefundResult(RefundRequestDTO refundItem) {
		return R.ok();
	}
}
