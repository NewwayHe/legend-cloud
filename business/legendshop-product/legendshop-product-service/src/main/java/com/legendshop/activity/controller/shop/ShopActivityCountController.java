/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.controller.shop;

import com.legendshop.activity.dto.ActivityShopUsageDTO;
import com.legendshop.activity.dto.ActivityUsageCountDTO;
import com.legendshop.activity.query.MarketingDataViewQuery;
import com.legendshop.activity.service.ActivityCountService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.security.utils.SecurityUtils;
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
@Tag(name = "商家营销概况")
@RestController
@AllArgsConstructor
@RequestMapping(value = "/s/count", produces = MediaType.APPLICATION_JSON_VALUE)
public class ShopActivityCountController {

	@Autowired
	private final ActivityCountService activityCountService;


	@Operation(summary = "【商家】获取营销概况数据", description = "")
	@GetMapping("/masketing/page")
	public R<List<ActivityUsageCountDTO>> getMasketingDataPage(MarketingDataViewQuery viewQuery) {
		viewQuery.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(activityCountService.getMasketingDataPage(viewQuery));
	}

	@Operation(summary = "【商家】新增使用总次数饼图", description = "")
	@GetMapping("/masketing/total/count")
	public R<List<ActivityShopUsageDTO>> getMasketingTotalCount(MarketingDataViewQuery viewQuery) {
		viewQuery.setShopId(SecurityUtils.getShopUser().getShopId());
		return R.ok(activityCountService.getMasketingTotalCount(viewQuery));
	}
}

