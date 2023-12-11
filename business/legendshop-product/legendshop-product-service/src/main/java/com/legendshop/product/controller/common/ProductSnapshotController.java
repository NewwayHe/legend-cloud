/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.common;

import com.legendshop.common.core.constant.R;
import com.legendshop.product.dto.ProductSnapshotDTO;
import com.legendshop.product.service.ProductSnapshotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@Tag(name = "商品快照管理")
@RestController
@RequestMapping(value = "/snapshot", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductSnapshotController {

	@Autowired
	private ProductSnapshotService productSnapshotService;


	@Operation(summary = "【公共】查看商品快照")
	@Parameter(name = "snapshotId", description = "快照id", required = true)
	@GetMapping("/get")
	public R<ProductSnapshotDTO> get(@RequestParam("snapshotId") Long snapshotId) {
		return productSnapshotService.getProductSnapshot(snapshotId);
	}

}
