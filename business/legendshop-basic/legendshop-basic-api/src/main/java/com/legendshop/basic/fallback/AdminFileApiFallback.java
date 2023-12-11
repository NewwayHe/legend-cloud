/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.fallback;

import com.legendshop.basic.api.AdminFileApi;
import com.legendshop.common.core.constant.R;
import org.springframework.stereotype.Component;

/**
 * @author legendshop
 */
@Component
public class AdminFileApiFallback implements AdminFileApi {

	@Override
	public R<Long> checkFile() {
		return R.serviceFail();
	}
}
