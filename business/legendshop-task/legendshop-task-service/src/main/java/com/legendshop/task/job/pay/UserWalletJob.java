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
import com.legendshop.pay.api.UserWalletBusinessApi;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 用户钱包定时任务
 *
 * @author legendshop
 */
@Slf4j
@Component
@AllArgsConstructor
public class UserWalletJob {

	final UserWalletBusinessApi userWalletBusinessApi;

	/**
	 * 处理用户钱包中间表
	 * 0 30 * * * ?
	 */
	@XxlJob("dealWithUserWalletCentre")
	public ReturnT<String> dealWithUserWalletCentre(String param) {
		XxlJobHelper.log("dealWithUserWalletCentreJob - 开始处理用户钱包中间表状态为待处理的记录~");
		log.info("dealWithUserWalletCentreJob - 开始处理用户钱包中间表状态为待处理的记录~");

		R<Void> result = userWalletBusinessApi.dealWithUserWalletCentreJobHandle();
		if (!result.success()) {
			XxlJobHelper.log("处理用户钱包中间表状态为待处理失败--" + result.getMsg());
			return ReturnT.FAIL;
		}
		XxlJobHelper.log("dealWithUserWalletCentreJob - 定时任务完成~");
		return ReturnT.SUCCESS;
	}


}
