/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller;

import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.RequestHeaderConstant;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.UserTokenUtil;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.bo.ProductCommentAnalysisBO;
import com.legendshop.product.bo.ProductCommentInfoBO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.dto.VitLogRecordDTO;
import com.legendshop.product.enums.VitLogPageEnum;
import com.legendshop.product.query.ProductCommentQuery;
import com.legendshop.product.query.ProductDetailQuery;
import com.legendshop.product.service.FavoriteProductService;
import com.legendshop.product.service.ProductCommentService;
import com.legendshop.product.service.ProductService;
import com.legendshop.product.service.VitLogService;
import com.legendshop.product.utils.ProductStatusChecker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@Slf4j
@RestController
@Tag(name = "商品相关")
@RequiredArgsConstructor
public class ProductController {

	final HttpServletRequest request;
	final UserTokenUtil userTokenUtil;
	final VitLogService vitLogService;
	final ProductService productService;
	final ProductStatusChecker productStatusChecker;
	final ProductCommentService productCommentService;
	final FavoriteProductService favoriteProductService;


	@GetMapping("/views")
	@SystemLog("【用户】商品详情")
	@Operation(summary = "【用户】商品详情")
	public R<ProductBO> views(HttpServletRequest request, ProductDetailQuery query) {
		//不需要验权访问的地址，没有CHECK TOKEN，不能通过SecurityUtils.getUserId() 获取用户信息，需要通过userTokenUtil获取
		Long userId = userTokenUtil.getUserId(request);
		query.setUserId(userId);
		R<ProductBO> productBOR = productService.views(query);
		if (!productBOR.success()) {
			return productBOR;
		}
		ProductBO productBO = productBOR.getData();

		// 异步记录浏览历史
		vitLogService.recordVitLog(new VitLogRecordDTO()
				.setUser_key(request.getHeader(RequestHeaderConstant.USER_KEY))
				.setUserId(userId)
				.setShopId(productBO.getShopId())
				.setShopName(productBO.getSiteName())
				.setSource(request.getHeader(RequestHeaderConstant.SOURCE_KEY))
				.setPage(VitLogPageEnum.PRODUCT_PAGE.value())
				.setProductId(productBO.getId())
				.setProductName(productBO.getName())
				.setPrice(productBO.getPrice())
				.setPic(productBO.getPic())
				.setIp(JakartaServletUtil.getClientIP(request))
		);
		// MQ记录用户浏览数据
		String source = request.getHeader(RequestHeaderConstant.SOURCE_KEY);
		vitLogService.saveProdView(productBO, userId, source);
		vitLogService.saveShopView(productBO.getShopId(), userId, source);
		return R.ok(productBO);
	}


	@Operation(summary = "【用户】商品详情评论分页查询")
	@GetMapping("/comment/page")
	public R<PageSupport<ProductCommentInfoBO>> queryProductComment(ProductCommentQuery productCommentQuery) {
		return R.ok(productCommentService.queryProductComment(productCommentQuery));
	}


	@Operation(summary = "【用户】商品评论统计")
	@GetMapping("/comment/analysis")
	public R<ProductCommentAnalysisBO> getProductProductCommentAnalysis(Long productId) {
		return R.ok(productCommentService.getProductProductCommentAnalysis(productId));
	}

	/**
	 * 根据商品id计算综合评分
	 */
	@Operation(summary = "【通用】根据商品id计算综合评分")
	@Parameter(name = "productId", description = "商品id", required = true)
	@GetMapping("/comment/getComScore")
	public R<ProductDTO> getComScore(@RequestParam("productId") Long productId) {
		return R.ok(productCommentService.getComScore(productId));
	}
}
