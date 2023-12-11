/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.query.OperatorLogQuery;
import com.legendshop.basic.service.OperatorLogService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.dto.OperatorLogDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@RestController
@RequiredArgsConstructor
public class OperatorLogApiImpl implements OperatorLogApi {
	/**
	 * 操作日志表(OperatorLog)服务对象
	 */
	private final OperatorLogService operatorLogService;

	@Override
	@Operation(summary = "分页查询操作日志表")
	public R<PageSupport<OperatorLogDTO>> page(@RequestBody OperatorLogQuery operatorLogQuery) {
		return operatorLogService.page(operatorLogQuery);
	}
}
