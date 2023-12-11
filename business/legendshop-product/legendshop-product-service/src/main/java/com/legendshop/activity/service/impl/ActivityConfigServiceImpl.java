/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service.impl;

import com.legendshop.activity.dto.ActivityConfigDTO;
import com.legendshop.activity.dto.DistributionConfigDTO;
import com.legendshop.activity.service.ActivityConfigService;
import com.legendshop.common.core.constant.R;
import org.springframework.stereotype.Service;

/**
 * 营销活动配置
 *
 * @author legendshop
 * @create: 2021-11-02 15:19
 */
@Service
public class ActivityConfigServiceImpl implements ActivityConfigService {

	@Override
	public R<ActivityConfigDTO> getConfig() {
		ActivityConfigDTO result = new ActivityConfigDTO();

		DistributionConfigDTO distributionConfig = new DistributionConfigDTO();
		result.setDistributionConfig(distributionConfig);

		return R.ok(result);
	}
}
