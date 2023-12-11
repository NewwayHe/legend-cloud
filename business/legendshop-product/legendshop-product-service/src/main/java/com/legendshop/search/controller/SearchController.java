/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.controller;

import com.legendshop.basic.enums.OpStatusEnum;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import com.legendshop.common.security.utils.UserTokenUtil;
import com.legendshop.product.enums.ProductDelStatusEnum;
import com.legendshop.product.enums.ProductStatusEnum;
import com.legendshop.search.dto.ProductDocumentDTO;
import com.legendshop.search.service.SearchProductService;
import com.legendshop.search.vo.SearchRequest;
import com.legendshop.search.vo.SearchResult;
import com.legendshop.shop.enums.ShopDetailStatusEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 搜索商品的控制层
 *
 * @author legendshop
 */
@Slf4j
@RestController
@RequestMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = "搜索商品")
public class SearchController {

	private final SearchProductService searchProductService;

	private final UserTokenUtil userTokenUtil;

	private final HttpServletRequest servletRequest;


	/**
	 * 搜索商品
	 *
	 * @param request 搜索请求参数
	 * @return R SearchResult 返回搜索结果集
	 */
	@PostMapping
	@SystemLog("【用户】搜索商品")
	@Operation(summary = "【用户】搜索商品", description = "")
	public R<SearchResult<ProductDocumentDTO>> search(@RequestBody SearchRequest request) throws IOException {
		Long userId = userTokenUtil.getUserId(servletRequest);
		if (userId != null && userId == 0L) {
			userId = null;
		}
		// 前端搜索接口写死
		request.setStatus(ProductStatusEnum.PROD_ONLINE.getValue());
		request.setOpStatus(OpStatusEnum.PASS.getValue());
		request.setDelStatus(ProductDelStatusEnum.PROD_NORMAL.value());
		request.setShopStatus(ShopDetailStatusEnum.ONLINE.getStatus());
		SearchResult<ProductDocumentDTO> result = searchProductService.searchProduct(request, userId);

		return R.ok(result);
	}

	@GetMapping("/searchProductById")
	@Operation(summary = "【用户】商品详情", description = "")
	public R<ProductDocumentDTO> searchProductById(@RequestParam("productId") Long productId) {
		ProductDocumentDTO productDocumentDTO = searchProductService.searchProductById(productId);
		return R.ok(productDocumentDTO);
	}
}
