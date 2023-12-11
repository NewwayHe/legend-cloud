/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.controller;

import com.legendshop.activity.dto.MarketingActivityItemDTO;
import com.legendshop.activity.service.MarketingActivityService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.UserTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author legendshop
 */
@Tag(name = "营销活动")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/marketingActivity", produces = MediaType.APPLICATION_JSON_VALUE)
public class MarketingActivityController {

	final HttpServletRequest request;

	final UserTokenUtil userTokenUtil;

	final MarketingActivityService marketingActivityService;


	@GetMapping
	@Operation(summary = "【用户】根据商品id获取获取下面所有sku对应的营销信息", description = "")
	@Parameter(name = "productId", description = "商品ID", required = true)
	public R<Map<Long, List<MarketingActivityItemDTO>>> get(@RequestParam Long productId) {
		Assert.notNull(productId, "商品id不能为空");
		Long userId = userTokenUtil.getUserId(request);
		if (null != userId && userId == 0L) {
			userId = null;
		}
		return R.ok(this.marketingActivityService.getMarketingActivity(productId, userId));
	}

}
