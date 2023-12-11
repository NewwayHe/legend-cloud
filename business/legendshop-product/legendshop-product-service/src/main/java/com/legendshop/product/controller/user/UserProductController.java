/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.user;

import cn.hutool.core.util.ObjectUtil;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.bo.ProductBO;
import com.legendshop.product.dto.FavoriteProductDTO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.product.service.FavoriteProductService;
import com.legendshop.product.service.ProductService;
import com.legendshop.product.service.VitLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户端商品控制器
 *
 * @author legendshop
 */
@Tag(name = "用户端商品")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/p/product", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserProductController {

	final ProductService productService;
	final FavoriteProductService favoriteProductService;
	final VitLogService vitLogService;

	@GetMapping("/isExistsFavorite")
	@Operation(summary = "【用户】判断商品是否已被收藏")
	@Parameter(name = "productId", description = "商品ID", required = true)
	public R<FavoriteProductDTO> isExistsFavorite(@RequestParam Long productId) {
		//获取登录用户Id
		Long userId = SecurityUtils.getUserId();
		FavoriteProductDTO favoriteProductDTO = new FavoriteProductDTO();
		if (ObjectUtil.isNotNull(userId) && favoriteProductService.isExistsFavorite(productId, userId)) {
			favoriteProductDTO.setCollectionFlag(Boolean.TRUE);
			return R.ok(favoriteProductDTO);
		}
		favoriteProductDTO.setCollectionFlag(Boolean.FALSE);
		return R.ok(favoriteProductDTO);
	}


	@GetMapping("/queryRecFavorProdList")
	@Operation(summary = "【用户】获取收藏商品列表")
	public R<List<ProductBO>> queryRecFavorProd(ProductQuery query) {
		List<ProductBO> favorProd = productService.getRencFavorProd(query.getAmount(), query.getUserId());
		return R.ok(favorProd);
	}

	/**
	 * 根据id查询商品名称
	 *
	 * @param dto
	 * @return
	 */
	@GetMapping("/getProduct")
	@Operation(summary = "【用户】获取商品")
	public R<ProductDTO> getProduct(@Valid @RequestBody ProductDTO dto) {
		ProductDTO productDTO = productService.getDtoByProductId(dto.getId());
		return R.ok(productDTO);
	}


}
