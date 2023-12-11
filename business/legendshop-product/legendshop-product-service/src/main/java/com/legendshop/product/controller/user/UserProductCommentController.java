/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.user;

import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.api.WxApi;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.bo.ProductCommentBO;
import com.legendshop.product.bo.ProductCommentInfoBO;
import com.legendshop.product.dto.ProductAddCommentDTO;
import com.legendshop.product.dto.ProductCommentDTO;
import com.legendshop.product.query.MyProductCommentQuery;
import com.legendshop.product.service.ProductAddCommentService;
import com.legendshop.product.service.ProductCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 订单中心-我的评价控制器
 *
 * @author legendshop
 */
@Tag(name = "商品评论")
@RestController
@RequestMapping(value = "/p/product/comment", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserProductCommentController {

	@Autowired
	private ProductCommentService productCommentService;

	@Autowired
	private ProductAddCommentService productAddCommentService;

	@Autowired
	private WxApi wxApi;

	@Operation(summary = "【用户】 商品评论列表分页查询 个人中心/订单共用")
	@GetMapping("/page")
	public R<PageSupport<ProductCommentInfoBO>> queryProductComment(MyProductCommentQuery productCommentQuery) {
		Long userId = SecurityUtils.getUserId();
		productCommentQuery.setUserId(userId);
		return R.ok(productCommentService.queryMyProductComment(productCommentQuery));
	}


	@Operation(summary = "【用户】获取去评论/追评商品信息")
	@GetMapping("/product/detail")
	public R<ProductCommentInfoBO> getProductDetailByComment(@RequestParam Long orderItemId) {
		return productCommentService.getProductDetailByComment(orderItemId);
	}


	@Operation(summary = "【用户】保存商品初评")
	@SystemLog("【用户】保存商品评价")
	@PostMapping
	public R<Void> save(@Valid @RequestBody ProductCommentDTO productCommentDTO, HttpServletRequest request) {
		//敏感字审核
		R<Void> result = wxApi.checkContent(productCommentDTO.getContent());
		if (!result.success()) {
			return result;
		}
		Long userId = SecurityUtils.getUserId();
		productCommentDTO.setUserId(userId);
		productCommentDTO.setPostIp(JakartaServletUtil.getClientIP(request));
		return productCommentService.saveProductComment(productCommentDTO);
	}


	@Operation(summary = "【用户】查看评论")
	@Parameter(name = "id", description = "评论id", required = true)
	@GetMapping
	public R<ProductCommentBO> getByCommentId(Long id) {
		return R.ok(productCommentService.getProductCommentById(id));
	}


	@Operation(summary = "【用户】查看追评")
	@Parameter(name = "addId", description = "追评id", required = true)
	@GetMapping("/getAdd")
	public R<ProductAddCommentDTO> getByAddCommentId(@RequestParam Long addId) {
		return R.ok(productAddCommentService.getById(addId));
	}

	@Operation(summary = "【用户】保存用户追评信息")
	@PostMapping("/save/add")
	public R<Void> saveAdd(@Valid @RequestBody ProductAddCommentDTO productAddCommentDTO, HttpServletRequest request) {
		//敏感字审核
		R<Void> result = wxApi.checkContent(productAddCommentDTO.getContent());
		if (!result.success()) {
			return result;
		}
		Long userId = SecurityUtils.getUserId();
		productAddCommentDTO.setUserId(userId);
		productAddCommentDTO.setPostIp(JakartaServletUtil.getClientIP(request));
		if (productAddCommentService.addProdComm(productAddCommentDTO)) {
			return R.ok();
		}
		return R.fail();
	}

}
