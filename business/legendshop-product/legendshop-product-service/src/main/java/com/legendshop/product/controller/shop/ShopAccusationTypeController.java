/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.shop;

import com.legendshop.common.core.constant.R;
import com.legendshop.product.dto.AccusationTypeDTO;
import com.legendshop.product.service.AccusationTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@RestController
@RequestMapping(value = "/s/accusationType", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@Tag(name = "商品举报类型管理")
public class ShopAccusationTypeController {

	@Autowired
	private AccusationTypeService accusationTypeService;

	/**
	 * 查询所有在线的举报类型
	 *
	 * @return
	 */
	@GetMapping("/all")
	@PreAuthorize("@pms.hasPermission('s_product_accusationType_queryAllOnLine')")
	@Operation(summary = "【商家】查询所有在线的举报类型")
	public R<List<AccusationTypeDTO>> queryAllOnLine() {
		return R.ok(accusationTypeService.queryAllOnLine());
	}

}
