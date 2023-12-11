/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.user;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.shop.dto.BatchFavoriteShopDTO;
import com.legendshop.shop.dto.FavoriteShopDTO;
import com.legendshop.shop.query.ShopFavoriteQuery;
import com.legendshop.shop.service.FavoriteShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏店铺控制器
 *
 * @author legendshop
 */
@RestController
@RequestMapping(value = "/p/favorite/shop", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "用户端收藏店铺")
public class UserFavoriteShopController {

	@Autowired
	private FavoriteShopService favoriteShopService;

	@PostMapping("/userFavoriteCount")
	public R<Integer> userFavoriteCount(Long userId) {
		return favoriteShopService.userFavoriteCount(userId);
	}

	/**
	 * 店铺收藏
	 *
	 * @param favoriteShopDTO
	 * @return
	 */
	@Operation(summary = "【用户】店铺收藏")
	@PostMapping("/favoriteShopFlag")
	public R<Boolean> favoriteShopFlag(@Valid @RequestBody BatchFavoriteShopDTO favoriteShopDTO) {
		Long userId = SecurityUtils.getUser().getUserId();
		favoriteShopDTO.setUserId(userId);
		if (favoriteShopService.favoriteShopFlag(favoriteShopDTO)) {
			return R.ok();
		} else {
			return R.fail();
		}
	}


	@Operation(summary = "【用户】查询我的店铺收藏")
	@GetMapping("/page")
	public R<PageSupport<FavoriteShopDTO>> queryFavoriteShopList(ShopFavoriteQuery query) {
		query.setUserId(SecurityUtils.getUser().getUserId());
		PageSupport<FavoriteShopDTO> list = favoriteShopService.queryFavoriteShopPageList(query);
		return R.ok(list);
	}

	/**
	 * 批量取消收藏
	 *
	 * @param favoriteShopDTO
	 * @return
	 */
	@Operation(summary = "【用户】批量取消店铺收藏")
	@DeleteMapping("/favorites")
	public R<Boolean> delFavorites(@RequestBody FavoriteShopDTO favoriteShopDTO) {
		Assert.notEmpty(favoriteShopDTO.getSelectedFavs(), "请选择取消收藏的店铺id!");
		favoriteShopService.deleteFavs(SecurityUtils.getUserId(), favoriteShopDTO.getSelectedFavs());
		return R.ok();
	}

}
