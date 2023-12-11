/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import com.legendshop.basic.dto.ExceptionLogDTO;
import com.legendshop.basic.service.ExceptionLogService;
import com.legendshop.common.core.constant.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@RestController
@RequiredArgsConstructor
public class ExceptionLogApiImpl implements ExceptionLogApi {
	final ExceptionLogService exceptionLogService;

	@Override
	public R<Long> save(@RequestBody ExceptionLogDTO dto) {
		return R.ok(this.exceptionLogService.save(dto));
	}
}
