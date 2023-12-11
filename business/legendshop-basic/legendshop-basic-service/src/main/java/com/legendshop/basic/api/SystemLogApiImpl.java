/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import com.legendshop.basic.service.SystemLogService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.dto.SystemLogDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@RestController
@RequiredArgsConstructor
public class SystemLogApiImpl implements SystemLogApi {
	final SystemLogService systemLogService;

	@Override
	public R<Void> save(@RequestBody SystemLogDTO systemLogDTO) {
		this.systemLogService.save(systemLogDTO);
		return R.ok();
	}
}
