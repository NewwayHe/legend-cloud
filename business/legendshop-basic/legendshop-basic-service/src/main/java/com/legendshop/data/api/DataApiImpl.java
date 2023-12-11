/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.data.dto.DataActivityCollectDTO;
import com.legendshop.data.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Feign调用实现
 *
 * @author legendshop
 */
@RestController
@Validated
@RequiredArgsConstructor
public class DataApiImpl implements DataApi {

	private final DataService dataService;

	@Override
	public R<DataActivityCollectDTO> getLastedCollectData() {
		return R.ok(dataService.getLastedCollectData());
	}

	@Override
	public R<Boolean> activityCollect(@RequestParam(value = "flag") Boolean flag) {
		return R.ok(dataService.activityCollect(flag));
	}

	@Override
	public R<Void> dataUserAmountJobHandle() {

		return dataService.dataUserAmountJobHandle();
	}
}
