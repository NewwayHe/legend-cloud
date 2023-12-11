/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.common;

import com.legendshop.common.core.constant.R;
import com.legendshop.shop.dto.PlateCapitalFlowDTO;
import com.legendshop.shop.service.PlateCapitalFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台资金流水
 *
 * @author legendshop
 */
@RestController
@RequestMapping(value = "/plate/capital/flow", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PlateCapitalFlowController {

	final PlateCapitalFlowService plateCapitalFlowService;


	@PostMapping("/save")
	public R<Long> save(PlateCapitalFlowDTO plateCapitalFlowDTO) {
		return R.ok(plateCapitalFlowService.save(plateCapitalFlowDTO));
	}
}
