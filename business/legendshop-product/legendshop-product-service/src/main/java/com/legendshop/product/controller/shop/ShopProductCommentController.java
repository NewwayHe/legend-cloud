/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.shop;

import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.api.WxApi;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.bo.ProductCommentInfoBO;
import com.legendshop.product.dto.ProductCommentDTO;
import com.legendshop.product.query.ProductCommentQuery;
import com.legendshop.product.service.ProductAddCommentService;
import com.legendshop.product.service.ProductCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author legendshop
 */
@Tag(name = "商品评论")
@RestController
@RequestMapping(value = "/s/product/comment", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopProductCommentController {

	@Autowired
	private ProductCommentService productCommentService;

	@Autowired
	private ProductAddCommentService productAddCommentService;

	@Autowired
	private WxApi wxApi;

	@PreAuthorize("@pms.hasPermission('s_product_comment_page')")
	@Operation(summary = "【商家】分页查询（spu为单位）")
	@GetMapping("/page")
	public R<PageSupport<ProductCommentDTO>> querySpuComment(ProductCommentQuery productCommentQuery) {
		productCommentQuery.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(productCommentService.querySpuComment(productCommentQuery));
	}

	@PreAuthorize("@pms.hasPermission('s_product_comment_skuPage')")
	@Operation(summary = "【商家】分页查询（sku为单位）")
	@GetMapping("/sku/page")
	public R<PageSupport<ProductCommentInfoBO>> querySkuComment(ProductCommentQuery productCommentQuery) {
		if (ObjectUtil.isNull(productCommentQuery.getProductId())) {
			return R.fail("商品id不能为空");
		}
		productCommentQuery.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(productCommentService.querySkuComment(productCommentQuery));
	}

	@PreAuthorize("@pms.hasPermission('s_product_comment_get')")
	@Operation(summary = "【商家】查看评论详情")
	@Parameter(name = "id", description = "评论id", required = true)
	@GetMapping("/get")
	public R<ProductCommentInfoBO> get(@RequestParam Long id) {
		return R.ok(productCommentService.getProductCommentDetail(id));
	}

	@PreAuthorize("@pms.hasPermission('s_product_comment_replyFirst')")
	@Operation(summary = "【商家】回复初评")
	@Parameters({
			@Parameter(name = "id", description = "评论id", required = true),
			@Parameter(name = "content", description = "回复内容", required = true)
	})
	@PostMapping("/reply/first")
	public R replyFirst(@RequestParam Long id, @RequestParam String content) {
		//敏感字审核
		R<Void> result = wxApi.checkContent(content);
		if (!result.success()) {
			return result;
		}
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return R.ok(productCommentService.replyFirst(id, content, shopId));
	}

	@PreAuthorize("@pms.hasPermission('s_product_comment_replyAdd')")
	@Operation(summary = "【商家】回复追加评论")
	@Parameters({
			@Parameter(name = "addId", description = "追评id", required = true),
			@Parameter(name = "content", description = "回复内容", required = true)
	})
	@PostMapping("/reply/add")
	public R replyAdd(@RequestParam Long addId, @RequestParam String content) {
		//敏感字审核
		R<Void> result = wxApi.checkContent(content);
		if (!result.success()) {
			return result;
		}
		Long shopId = SecurityUtils.getShopUser().getShopId();
		return R.ok(productAddCommentService.replyAdd(addId, content, shopId));
	}
}
