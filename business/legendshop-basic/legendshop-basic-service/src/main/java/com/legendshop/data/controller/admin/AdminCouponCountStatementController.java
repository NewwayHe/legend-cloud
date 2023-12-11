/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.controller.admin;

import com.legendshop.activity.dto.MarketingDataTrendDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.excel.annotation.ExportExcel;
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
@Tag(name = "平台营销数据统计报表")
@RestController
@RequestMapping(value = "/admin/coupon/statement", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminCouponCountStatementController {

	final CouponCountStatementService couponCountStatementService;

	@GetMapping("/platform/amountCount")
	@Operation(summary = "平台营销金额数据统计", description = "")
	public R<MarketingAmountStatisticsDTO> platformMarketingAmountStatistics(ActivityCouponCountQuery query) {
		return R.ok(couponCountStatementService.marketingAmountStatistics(query));
	}

	@GetMapping("/platform/userAmountCount")
	@Operation(summary = "平台营销用户数据统计", description = "")
	public R<MarketingUserStatisticsDTO> platformMarketingUserStatistics(ActivityCouponCountQuery query) {
		return R.ok(couponCountStatementService.marketingUserStatistics(query));
	}

	@GetMapping("/platform/userDataCount")
	@Operation(summary = "平台营销数据趋势统计", description = "")
	public R<List<MarketingDataTrendDTO>> platformMarketingDataTrendStatistics(ActivityCouponCountQuery query) {
		return R.ok(couponCountStatementService.marketingDataTrendStatistics(query));
	}

	@Operation(summary = "【后台】导出Excel平台营销金额数据统计", description = "")
	@GetMapping("/platform/page/excel/userAmountCount")
	@ExportExcel(name = "平台营销数据趋势统计概况", sheet = "平台营销数据趋势统计概况")
	public List<MarketingDataTrendDTO> platformMarketingDataTrendStatisticsExcel(ActivityCouponCountQuery query) {
		return couponCountStatementService.marketingDataTrendStatistics(query);
	}


}
