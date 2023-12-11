/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.task.job.data;

import com.legendshop.common.core.constant.R;
import com.legendshop.data.api.ShopOutStockRateApi;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 缺货率定时任务
 *
 * @author legendshop
 * @create: 2021-06-25 17:42
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ShopOutStockRateJob {


	private final ShopOutStockRateApi shopOutStockRateApi;

	/**
	 * 缺货率定时任务
	 *
	 * @param param
	 * @return
	 */
	@XxlJob("shopOutStockRate")
	public ReturnT<String> shopOutStockRate(String param) {

		R<Void> result = shopOutStockRateApi.shopOutStockRateJobHandle();
		if (!result.success()) {
			XxlJobHelper.log("缺货率定时任务执行失败");
			return ReturnT.FAIL;
		}
		return ReturnT.SUCCESS;
	}
}
