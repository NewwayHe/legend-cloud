/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.controller.shop;

import com.legendshop.activity.dto.MarketingDataTrendDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.data.dto.ActivityCouponCountQuery;
import com.legendshop.data.dto.MarketingAmountStatisticsDTO;
import com.legendshop.data.dto.MarketingUserStatisticsDTO;
import com.legendshop.data.service.CouponCountStatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@Tag(name = "平台和商家营销数据统计报表")
@RestController
@RequestMapping(value = "/shop/coupon/statement", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ShopCouponStatementController {

	final CouponCountStatementService couponCountStatementService;

	@GetMapping("/shop/amountCount")
	@Operation(summary = "商家营销金额数据统计", description = "")
	public R<MarketingAmountStatisticsDTO> merchantMarketingAmountStatistics(ActivityCouponCountQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(couponCountStatementService.marketingAmountStatistics(query));
	}

	@GetMapping("/shop/userAmountCount")
	@Operation(summary = "商家营销用户数据统计", description = "")
	public R<MarketingUserStatisticsDTO> merchantMarketingUserStatistics(ActivityCouponCountQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(couponCountStatementService.marketingUserStatistics(query));
	}

	/**
	 * 数据趋势
	 */
	@GetMapping("/shop/userDataCount")
	@Operation(summary = "商家营销数据趋势统计", description = "")
	public R<List<MarketingDataTrendDTO>> merchantMarketingDataTrendStatistics(ActivityCouponCountQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(couponCountStatementService.marketingDataTrendStatistics(query));
	}

	/**
	 * 数据趋势
	 */
	@Operation(summary = "【后台】导出Excel商家营销金额数据统计", description = "")
	@GetMapping("/shop/page/excel/userDataCount")
	@ExportExcel(name = "营销金额趋势数据概况", sheet = "营销金额数据趋势概况")
	public List<MarketingDataTrendDTO> merchantMarketingDataTrendStatisticsExcel(ActivityCouponCountQuery query) {
		query.setShopId(SecurityUtils.getShopUser().getShopId());
		return couponCountStatementService.marketingDataTrendStatistics(query);
	}


}
