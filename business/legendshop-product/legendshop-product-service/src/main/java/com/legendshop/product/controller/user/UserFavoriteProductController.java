/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.controller.user;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.RequestHeaderConstant;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.product.dto.BatchFavoriteProductDTO;
import com.legendshop.product.dto.FavoriteProductDTO;
import com.legendshop.product.query.ProductQuery;
import com.legendshop.product.service.FavoriteProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * 用户端收藏商品控制器
 *
 * @author legendshop
 */
@RestController
@RequestMapping(value = "/p/favoriteProduct", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "用户端收藏商品")
public class UserFavoriteProductController {

	@Autowired
	private FavoriteProductService favoriteProductService;

	/**
	 * 收藏和取消收藏
	 *
	 * @param favoriteProductDTO
	 * @return
	 */
	@Operation(summary = "【用户】收藏和取消收藏")
	@PostMapping("/saveFavorite")
	public R<Boolean> saveFavorite(@RequestBody BatchFavoriteProductDTO favoriteProductDTO, HttpServletRequest request) {
		Long userId = SecurityUtils.getUser().getUserId();
		favoriteProductDTO.setUserId(userId);
		favoriteProductDTO.setSource(request.getHeader(RequestHeaderConstant.SOURCE_KEY));
		if (favoriteProductService.favoriteFlag(favoriteProductDTO)) {
			return R.ok();
		}
		return R.fail();
	}

	/**
	 * 是否已收藏
	 *
	 * @param favoriteProductDTO
	 * @return
	 */
	@Operation(summary = "【用户】是否已收藏")
	@GetMapping("/isExistsFavorite")
	public R<Boolean> isExistsFavorite(@RequestBody FavoriteProductDTO favoriteProductDTO) {
		Long userId = SecurityUtils.getUser().getUserId();
		if (favoriteProductService.isExistsFavorite(favoriteProductDTO.getProductId(), userId)) {
			return R.ok();
		}
		return R.fail();
	}

	/**
	 * 查看我的商品收藏
	 *
	 * @param query
	 * @return
	 */
	@Operation(summary = "【用户】查看我的商品收藏")
	@GetMapping("/page")
	public R<PageSupport<FavoriteProductDTO>> queryFavoriteProduct(ProductQuery query) {
		query.setUserId(SecurityUtils.getUser().getUserId());
		PageSupport<FavoriteProductDTO> collect = favoriteProductService.collect(query);
		return R.ok(collect);
	}


	/**
	 * 批量取消收藏
	 *
	 * @param favoriteProductDTO
	 * @return
	 */
	@Operation(summary = "【用户】批量取消收藏")
	@DeleteMapping("/favorites")
	public R<Boolean> delFavorites(@RequestBody FavoriteProductDTO favoriteProductDTO) {
		Assert.notEmpty(favoriteProductDTO.getSelectedFavs(), "请选择取消收藏的商品id!");
		favoriteProductService.deleteFavs(SecurityUtils.getUserId(), favoriteProductDTO.getSelectedFavs());
		return R.ok();
	}

}
