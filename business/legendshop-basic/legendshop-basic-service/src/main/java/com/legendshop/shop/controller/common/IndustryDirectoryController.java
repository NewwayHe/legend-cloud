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
import com.legendshop.shop.dto.IndustryDirectoryDTO;
import com.legendshop.shop.service.IndustryDirectoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 行业目录(IndustryDirectory)表控制层
 *
 * @author legendshop
 * @since 2021-03-09 13:53:13
 */
@Slf4j

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/industry/directory", produces = MediaType.APPLICATION_JSON_VALUE)
public class IndustryDirectoryController {

	/**
	 * 行业目录(IndustryDirectory)服务对象
	 */
	private final IndustryDirectoryService industryDirectoryService;

	/**
	 * 获取分页列表
	 */
	@GetMapping("/getById")
	public R<IndustryDirectoryDTO> getById(@RequestParam("id") Long id) {
		return R.ok(this.industryDirectoryService.getById(id));
	}

	@PostMapping("/queryById")
	public R<List<IndustryDirectoryDTO>> queryById(@RequestBody List<Long> ids) {
		return R.ok(this.industryDirectoryService.queryById(ids));
	}

	@GetMapping("/queryAll")
	@Operation(summary = "【公共】获取行业目录列表")
	public R<List<IndustryDirectoryDTO>> queryAll() {
		return R.ok(this.industryDirectoryService.queryAll());
	}


}
