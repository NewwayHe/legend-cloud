/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.dto.ProductConsultDTO;
import com.legendshop.product.query.ProductConsultQuery;
import com.legendshop.product.service.ProductConsultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 平台端商品咨询
 *
 * @author legendshop
 */
@Tag(name = "平台端商品咨询")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/admin/product/consult", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminProductConsultController {

	final ProductConsultService productConsultService;


	@GetMapping("/page")
	@Operation(summary = "【后台】商品咨询列表查询")
	public R<PageSupport<ProductConsultDTO>> queryAdminProductConsultList(ProductConsultQuery query) {
		query.setDelSts(0);
		PageSupport<ProductConsultDTO> favorProd = productConsultService.getProductConsultCenterPage(query);
		return R.ok(favorProd);
	}

	@PreAuthorize("@pms.hasPermission('shop_product_consult_offline')")
	@Operation(summary = "【后台】商品咨询列表上下线 shop_product_consult_offline")
	@GetMapping("/offline")
	public R<Integer> offline(@RequestParam Long id, @RequestParam Integer status) {
		return productConsultService.offlineById(id, status);
	}

	@PreAuthorize("@pms.hasPermission('shop_product_consult_delete')")
	@Operation(summary = "【后台】商品咨询列表删除 shop_product_consult_delete")
	@DeleteMapping("/delete/{id}")
	public R<Integer> delete(@PathVariable Long id) {
		return productConsultService.deleteById(id);
	}


}
