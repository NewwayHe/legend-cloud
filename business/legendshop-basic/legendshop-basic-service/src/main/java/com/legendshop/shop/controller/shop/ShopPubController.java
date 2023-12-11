/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.controller.shop;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.shop.dto.PubDTO;
import com.legendshop.shop.enums.PubTypeEnum;
import com.legendshop.shop.enums.ReceiverEnum;
import com.legendshop.shop.query.PubQuery;
import com.legendshop.shop.service.PubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前台公告控制器
 *
 * @author legendshop
 */
@Tag(name = "商城公告")
@RestController
public class ShopPubController {


	@Autowired
	private PubService pubService;


	@Operation(summary = "[商家]获取商城公告详情")
	@Parameter(name = "pubId", description = "公告ID", required = true)
	@GetMapping("/s/pub/info")
	public R<PubDTO> pubInfo(@RequestParam Long pubId) {
		return pubService.getPubById(pubId, SecurityUtils.getShopUser().getUserId(), ReceiverEnum.SHOP);
	}


	@Operation(summary = "[商家]获取最新一条卖家公告")
	@GetMapping("/s/get/newest/shop/pub")
	public R<PubDTO> getNewestShopPub() {
		return R.ok(pubService.getNewestPubByType(PubTypeEnum.PUB_SELLER.value()));
	}


	@Operation(summary = "[商家]获取卖家公告列表")
	@GetMapping("/s/query/shop/pub/list")
	public R<PageSupport<PubDTO>> queryShopPubList(PubQuery pubQuery) {
		pubQuery.setType(PubTypeEnum.PUB_SELLER.value());
		pubQuery.setUserId(SecurityUtils.getShopUser().getShopId());
		pubQuery.setReceiverType(ReceiverEnum.SHOP.getValue());
		PageSupport<PubDTO> result = pubService.queryPubPageListByType(pubQuery);
		return R.ok(result);
	}


}
