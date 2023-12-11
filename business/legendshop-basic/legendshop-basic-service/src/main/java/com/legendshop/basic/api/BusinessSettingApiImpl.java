/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import com.legendshop.basic.service.BusinessSettingService;
import com.legendshop.common.core.constant.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@RestController
@RequiredArgsConstructor
public class BusinessSettingApiImpl implements BusinessSettingApi {

	final BusinessSettingService businessSettingService;

	@Override
	public R<String> getByType(@RequestParam(value = "type") String type) {
		return R.ok(this.businessSettingService.getByType(type));
	}

	@Override
	public R<Void> updateByType(@RequestParam(value = "type") String type, @RequestParam(value = "categorySetting") String categorySetting) {
		return R.process(this.businessSettingService.updateByType(type, categorySetting), "更新设置失败");
	}
}
