/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service;

import com.legendshop.activity.dto.ActivityConfigDTO;
import com.legendshop.common.core.constant.R;

/**
 * 营销活动配置
 *
 * @author legendshop
 * @create: 2021/11/2 15:18
 */
public interface ActivityConfigService {

	/**
	 * 获取活动配置
	 *
	 * @return
	 */
	R<ActivityConfigDTO> getConfig();

}
