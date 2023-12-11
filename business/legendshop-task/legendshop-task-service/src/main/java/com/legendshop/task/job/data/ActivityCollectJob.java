/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.task.job.data;

import cn.hutool.core.util.ObjectUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.data.api.DataApi;
import com.legendshop.data.dto.DataActivityCollectDTO;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 营销活动统计定时任务
 *
 * @author legendshop
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ActivityCollectJob {

	private final DataApi dataApi;


	@XxlJob("setActivityCollect")
	public ReturnT<String> setActivityCollect(String param) {
		XxlJobHelper.log("开始结算前一天营销活动汇总统计");
		log.info("开始结算前一天营销活动汇总统计");
		R<DataActivityCollectDTO> lastedCollectDataResult = dataApi.getLastedCollectData();

		if (!lastedCollectDataResult.success()) {
			XxlJobHelper.log("获取统计失败，请重试");
			return ReturnT.SUCCESS;
		}
		DataActivityCollectDTO lastedCollectData = lastedCollectDataResult.getData();
		Boolean aBoolean;
		if (ObjectUtil.isNull(lastedCollectData)) {
			aBoolean = dataApi.activityCollect(true).getData();
		} else {
			aBoolean = dataApi.activityCollect(false).getData();
		}

		if (aBoolean) {
			return ReturnT.SUCCESS;
		} else {
			return ReturnT.FAIL;
		}
	}
}
