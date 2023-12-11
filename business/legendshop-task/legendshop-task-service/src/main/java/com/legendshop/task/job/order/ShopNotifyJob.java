/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.task.job.order;

import com.legendshop.common.core.constant.R;
import com.legendshop.order.api.OrderApi;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 店铺通知定时任务
 *
 * @author legendshop
 * @create: 2021-09-03 18:21
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ShopNotifyJob {

	private final OrderApi orderApi;

	/**
	 * 通知商家未处理订单售后数量定时任务
	 * 每天下午五点执行一次
	 * 短信加站内信进行通知
	 *
	 * @param param
	 * @return
	 */
	@XxlJob("shopMessageSend")
	public ReturnT<String> toShopMessageSend(String param) {
		XxlJobHelper.log("发送 未发货订单通知 未处理售后通知 通知商家");
		log.info("发送 未发货订单通知 未处理售后通知 通知商家");

		// 处理 TODO
		R<Void> result = orderApi.shopMessageSendJobHandle();
		if (!result.success()) {
			XxlJobHelper.log("通知商家未处理订单售后数量定时任务处理失败");
			return ReturnT.FAIL;
		}
		return ReturnT.SUCCESS;
	}
}
