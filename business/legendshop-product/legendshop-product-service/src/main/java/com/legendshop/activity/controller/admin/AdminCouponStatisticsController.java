/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.activity.dto.ActivityCouponStatisticsDTO;
import com.legendshop.activity.query.ActivityCouponStatisticsQuery;
import com.legendshop.activity.service.ActivityCouponStatisticsService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.excel.annotation.ExportExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@Tag(name = "营销报表-活动列表优惠卷")
@RestController
@RequestMapping(value = "/admin/statistics/coupon", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminCouponStatisticsController {

	@Autowired
	private ActivityCouponStatisticsService activityCouponStatisticsService;

	@GetMapping("/page")
	@Operation(summary = "【平台】活动列表-优惠券", description = "")
	public R<PageSupport<ActivityCouponStatisticsDTO>> pageActivityPlatformCoupon(ActivityCouponStatisticsQuery query) {
		query.setShopProviderFlag(false);
		return R.ok(activityCouponStatisticsService.pageActivityPlatformCoupon(query));
	}

	@GetMapping("/flow/excel/platform")
	@ExportExcel(name = "平台优惠券统计", sheet = "平台优惠券统计", exclude = {"id", "shopName"})
	@Operation(summary = "平台优惠券导出", description = "")
	public List<ActivityCouponStatisticsDTO> getFlowExcelPlatform(ActivityCouponStatisticsQuery query) {
		query.setShopProviderFlag(false);
		return activityCouponStatisticsService.getFlowExcelPlatform(query);
	}


	@GetMapping("/shop/page")
	@Operation(summary = "【商家】活动列表-优惠券", description = "")
	public R<PageSupport<ActivityCouponStatisticsDTO>> pageActivityShopCoupon(ActivityCouponStatisticsQuery query) {
		query.setShopProviderFlag(true);
		return R.ok(activityCouponStatisticsService.pageActivityShopCoupon(query));
	}


	@GetMapping("/flow/excel/shop")
	@ExportExcel(name = "商家优惠券统计", sheet = "商家优惠券统计")
	@Operation(summary = "商家优惠券导出", description = "")
	public List<ActivityCouponStatisticsDTO> getFlowExcelShop(ActivityCouponStatisticsQuery query) {
		query.setShopProviderFlag(true);
		return activityCouponStatisticsService.getFlowExcelShop(query);
	}

}
