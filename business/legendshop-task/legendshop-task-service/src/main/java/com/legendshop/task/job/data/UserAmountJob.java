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
import com.legendshop.data.api.DataApi;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户数量统计定时任务
 *
 * @author legendshop
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserAmountJob {

	private final DataApi dataApi;


	@XxlJob("setUserAmount")
	public ReturnT<String> setUserAmount(String param) {
		XxlJobHelper.log("开始结算前一天用户数量统计");
		log.info("开始结算前一天用户数量统计");

		R<Void> result = dataApi.dataUserAmountJobHandle();
		if (!result.success()) {
			XxlJobHelper.log("用户数量统计处理失败");
			return ReturnT.FAIL;
		}
		return ReturnT.SUCCESS;
	}
}
