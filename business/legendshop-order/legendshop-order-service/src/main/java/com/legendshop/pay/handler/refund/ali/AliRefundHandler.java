/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.handler.refund.ali;

import cn.hutool.json.JSONUtil;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeFastpayRefundQueryResponse;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author legendshop
 */
@Slf4j
@RequiredArgsConstructor
@Component("ALI_PAY_REFUND")
public class AliRefundHandler implements RefundHandler {

	final AliPaySDKFactory aliPaySDKFactory;

	final AliPayCommonSDKFactory aliPayCommonSDKFactory;

	/**
	 * 成功
	 */
	public static final String SUCCESS = "10000";


	@Override
	public R<RefundResultItemDTO> refund(RefundRequestDTO refundItem) {
		log.info(" [ Refund Ali ] 进入支付宝退款, param = {}", JSONUtil.toJsonStr(refundItem));
		AlipayTradeRefundResponse refund;
		try {
			refund = aliPayCommonSDKFactory.refund(refundItem.getNumber(), refundItem.getRefundAmount().toString(), refundItem.getRefundSn());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(" [ Refund Ali ] 请求支付宝退款失败");
			return R.fail("请求支付宝退款失败 : " + e.getMessage());
		}

		if (!refund.isSuccess()) {
			// 退款失败
			log.error(" [ Refund Ali ] 请求支付宝退款失败 :" + refund.getSubMsg());
			return R.fail("请求支付宝退款失败 : " + refund.getSubMsg());
		}

		RefundResultItemDTO refundResultItem = new RefundResultItemDTO();
		refundResultItem.setExternalRefundSn(refund.getTradeNo());
		refundResultItem.setPayTypeId(SysParamNameEnum.ALI_PAY.name());
		refundResultItem.setAmount(refundItem.getRefundAmount());
		return R.ok(refundResultItem);
	}

	@Override
	public boolean isSupport(String payType, String paySource) {
		return PayTypeEnum.ALI_PAY.name().equals(payType);
	}

	@Override
	public R<Void> queryRefundResult(RefundRequestDTO refundItem) {
		AlipayTradeFastpayRefundQueryResponse refund;
		try {
			refund = this.aliPaySDKFactory.paymentHandlerCommon().queryRefund(refundItem.getNumber(), refundItem.getNumber());
		} catch (Exception e) {
			e.printStackTrace();
			log.error(" [ Refund Ali ] 请求支付宝退款查询失败");
			return R.fail("请求支付宝退款查询失败 : " + e.getMessage());
		}

		if (!refund.code.equals(SUCCESS)) {
			// 退款查询失败
			log.error(" [ Refund Ali ] 请求支付宝退款查询失败 :" + refund.subMsg);
			return R.fail("请求支付宝退款查询失败 : " + refund.subMsg);
		}
		String refundStatus = refund.getRefundStatus();
		if (StringUtils.isBlank(refundStatus) || "REFUND_SUCCESS".equals(refundStatus)) {
			return R.ok();
		}

		return R.fail("退款中，请稍后重试！");
	}
}
