/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.task.job.pay;

import com.legendshop.common.core.constant.R;
import com.legendshop.pay.api.PaySettlementApi;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 支付定时任务
 *
 * @author legendshop
 * @create: 2021-12-31 15:09
 */
@Slf4j
@Component
@AllArgsConstructor
public class PaymentJob {

	final PaySettlementApi paySettlementApi;

	/**
	 * 支付成功但处于待支付的订单处理定时任务
	 *
	 * @param param
	 * @return
	 */
	@XxlJob("paySuccessCompensate")
	public ReturnT<String> paySuccessCompensate(String param) {
		log.info("paySuccessCompensate-JOB，开始处理支付成功但处于待支付的订单");
		XxlJobHelper.log("paySuccessCompensate-JOB，开始处理支付成功但处于待支付的订单");

		R<Void> result = paySettlementApi.paySuccessCompensateJobHandle();
		if (!result.success()) {
			XxlJobHelper.log("处理支付成功但处于待支付的订单失败--" + result.getMsg());
			return ReturnT.FAIL;
		}
		XxlJobHelper.log("paySuccessCompensate-JOB，处理支付成功但处于待支付的订单完成");
		return ReturnT.SUCCESS;
	}
}
