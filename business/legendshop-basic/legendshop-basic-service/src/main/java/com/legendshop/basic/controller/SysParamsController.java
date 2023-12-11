/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller;

import com.legendshop.basic.dto.PayTypeDTO;
import com.legendshop.basic.dto.SysParamItemDTO;
import com.legendshop.basic.dto.SysParamsDTO;
import com.legendshop.basic.enums.SysParamGroupEnum;
import com.legendshop.basic.service.SysParamsService;
import com.legendshop.common.core.constant.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/sysParams")
public class SysParamsController {

	final SysParamsService sysParamsService;


	@PostMapping(value = "/getConfigDtoByParamName")
	public <T> R<T> getConfigDtoByParamName(@RequestParam(value = "name") String name, @RequestBody Class<T> clazz) {
		T configDtoByParamName = this.sysParamsService.getConfigDtoByParamName(name, clazz);
		return R.ok(configDtoByParamName);
	}

	@PostMapping(value = "/getNotCacheConfigByName")
	public <T> R<T> getNotCacheConfigByName(String name, Class<T> clazz) {
		T configDtoByParamName = this.sysParamsService.getNotCacheConfigByName(name, clazz);
		return R.ok(configDtoByParamName);
	}

	@GetMapping(value = "/getSysParamItemsByParamName")
	public R<List<SysParamItemDTO>> getSysParamItemsByParamName(@RequestParam(value = "name") String name) {
		return R.ok(this.sysParamsService.getSysParamItemsByParamName(name));
	}

	@GetMapping(value = "/getByGroup")
	public R<List<SysParamsDTO>> getByGroup(@RequestParam(value = "groupEnum") SysParamGroupEnum groupEnum) {
		return R.ok(this.sysParamsService.getByGroup(groupEnum));
	}

	@GetMapping(value = "/getEnabledPayType")
	public R<List<PayTypeDTO>> getEnabledPayType() {
		return R.ok(this.sysParamsService.getEnabledPayType());
	}

	@GetMapping(value = "/getUseEnabledPayType")
	public R<List<PayTypeDTO>> getUseEnabledPayType() {
		return R.ok(this.sysParamsService.getUseEnabledPayType());
	}


}
