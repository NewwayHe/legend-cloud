/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import com.legendshop.shop.dto.ShopCommentStatisticsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 店铺评分服务
 *
 * @author legendshop
 */
@FeignClient(contextId = "shopCommentStatisticsApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface ShopCommentStatisticsApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	/**
	 * 获取店铺评分统计
	 *
	 * @param shopId
	 * @return
	 */
	@PostMapping(PREFIX + "/comment/statistics/getShopCommStatByShopIdForUpdate")
	R<ShopCommentStatisticsDTO> getShopCommStatByShopIdForUpdate(@RequestParam(value = "shopId") Long shopId);

	/**
	 * 保存店铺评分统计
	 *
	 * @param shopCommStat
	 * @return
	 */
	@PostMapping(PREFIX + "/comment/statistics/saveShopCommStat")
	R<Void> saveShopCommStat(@RequestBody ShopCommentStatisticsDTO shopCommStat);

	/**
	 * 更新店铺评分统计
	 *
	 * @param score
	 * @param count
	 * @param shopId
	 * @return
	 */
	@PostMapping(PREFIX + "/comment/statistics/updateShopCommStat")
	R<Void> updateShopCommStat(@RequestParam(value = "score") Integer score, @RequestParam(value = "count") Integer count, @RequestParam(value = "shopId") Long shopId);
}
