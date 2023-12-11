/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.shop;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.bo.ProductArrivalNoticeBO;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.query.ProductArrivalNoticeQuery;
import com.legendshop.product.service.ProductArrivalNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品到货通知表(ProductArrivalNotice)表控制层
 *
 * @author legendshop
 * @since 2020-08-20 09:27:05
 */
@Tag(name = "商品到货通知")
@RestController
@RequestMapping(value = "/s/productArrivalNotice", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopProductArrivalNoticeController {

	/**
	 * 商品到货通知表(ProductArrivalNotice)服务对象
	 */
	@Autowired
	private ProductArrivalNoticeService productArrivalNoticeService;

	/**
	 * 到货通知商品分页查询
	 */
	@GetMapping("/productPage")
	@Operation(summary = "【商家】到货通知商品分页查询")
	@PreAuthorize("@pms.hasPermission('s_product_productArrivalNotice_productPage')")
	public R<PageSupport<SkuBO>> productPage(ProductArrivalNoticeQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(productArrivalNoticeService.productPage(query));
	}

	/**
	 * 到货通知用户列表分页查询
	 */
	@GetMapping("/page")
	@Operation(summary = "【商家】到货通知用户列表分页查询")
	@PreAuthorize("@pms.hasPermission('s_product_productArrivalNotice_page')")
	public R<PageSupport<ProductArrivalNoticeBO>> page(@Valid ProductArrivalNoticeQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(productArrivalNoticeService.getSelectArrival(query));
	}
}
