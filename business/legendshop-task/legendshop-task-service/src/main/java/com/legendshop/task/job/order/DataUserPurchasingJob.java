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
 * 用户购买力数据表数据清洗定时任务
 *
 * @author legendshop
 * @create: 2021-06-29 16:09
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataUserPurchasingJob {

	private final OrderApi orderApi;

	/**
	 * 1、先将ls_data_user_purchasing表的数据清空
	 * 2、通过xxl-job执行这个方法，从订单表中获取数据进行填充
	 * 3、来源默认是h5
	 *
	 * @param param
	 * @return
	 */
	@XxlJob("userPurchasingDataCleaning")
	public ReturnT<String> userPurchasingDataCleaning(String param) {

		R<Void> result = orderApi.userPurchasingDataCleaningJobHandle();
		if (!result.success()) {
			XxlJobHelper.log("户购买力数据表数据清洗定时任务处理失败");
			return ReturnT.FAIL;
		}
		return ReturnT.SUCCESS;
	}
}
