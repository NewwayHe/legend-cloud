/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.shop;

import com.legendshop.common.core.constant.R;
import com.legendshop.shop.dto.ShopCommentStatisticsDTO;
import com.legendshop.shop.service.ShopCommentStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺评分
 *
 * @author legendshop
 */
@RestController
@RequestMapping(value = "/comment/statistics", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ShopCommentStatisticsController {

	final ShopCommentStatisticsService shopCommentStatisticsService;

	@PostMapping("/getShopCommStatByShopIdForUpdate")
	public R<ShopCommentStatisticsDTO> getShopCommStatByShopIdForUpdate(Long shopId) {
		return R.ok(shopCommentStatisticsService.getShopCommStatByShopIdForUpdate(shopId));
	}

	@PostMapping("/saveShopCommStat")
	public R<Void> saveShopCommStat(ShopCommentStatisticsDTO shopCommStat) {
		shopCommentStatisticsService.saveShopCommStat(shopCommStat);
		return R.ok();
	}

	@PostMapping("/updateShopCommStat")
	public R<Void> updateShopCommStat(@RequestParam(value = "score") Integer score, @RequestParam(value = "count") Integer count, @RequestParam(value = "shopId") Long shopId) {
		shopCommentStatisticsService.updateShopCommStat(score, count, shopId);
		return R.ok();
	}
}
