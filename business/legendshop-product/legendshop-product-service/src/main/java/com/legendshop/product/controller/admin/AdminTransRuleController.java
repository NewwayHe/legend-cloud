/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.admin;

import cn.hutool.core.date.DateUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.dto.TransRuleDTO;
import com.legendshop.product.service.TransRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺运费规则(TransRule)表控制层
 *
 * @author legendshop
 * @since 2020-09-08 17:00:50
 */
@RestController
@Tag(name = "店铺运费规则")
@RequestMapping(value = "/admin/transRule", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminTransRuleController {

	/**
	 * 店铺运费规则(TransRule)服务对象
	 */
	@Autowired
	private TransRuleService transRuleService;

	@GetMapping
	@Operation(summary = "【后台】获取店铺运费规则")
	public R<TransRuleDTO> getById() {
		return R.ok(transRuleService.getByShopId(-1L));
	}

	@PutMapping
	@Operation(summary = "【后台】更新店铺运费规则")
	public R update(@Valid @RequestBody TransRuleDTO transRuleDTO) {
		transRuleDTO.setShopId(-1L);
		transRuleDTO.setRecDate(DateUtil.date());
		return R.ok(transRuleService.update(transRuleDTO));
	}
}
