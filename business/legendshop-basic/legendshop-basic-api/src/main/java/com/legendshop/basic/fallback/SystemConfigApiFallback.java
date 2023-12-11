/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.fallback;

import com.legendshop.basic.api.SystemConfigApi;
import com.legendshop.basic.dto.SystemConfigDTO;
import com.legendshop.common.core.constant.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author legendshop
 */
@Slf4j
@Component
public class SystemConfigApiFallback implements SystemConfigApi {

	@Override
	public R<SystemConfigDTO> getSystemConfig() {
		return R.serviceFail();
	}
}
