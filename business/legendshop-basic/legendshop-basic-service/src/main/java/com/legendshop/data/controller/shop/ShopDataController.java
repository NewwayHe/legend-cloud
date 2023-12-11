/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.controller.shop;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.common.security.utils.SecurityUtils;
import com.legendshop.data.dto.*;
import com.legendshop.data.query.ActivityDataQuery;
import com.legendshop.data.query.SimpleQuery;
import com.legendshop.data.service.DataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author legendshop
 */
@Slf4j
@RestController
@RequestMapping(value = "/s/count", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = "商家端数据服务")
public class ShopDataController {

	@Autowired
	private DataService dataService;


	@Operation(summary = "运营统计概况", description = "")
	@GetMapping("/business/data")
	public R<BusinessDataDTO> getBusinessData(SimpleQuery simpleQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		simpleQuery.setShopId(shopId);
		BusinessDataDTO businessData = dataService.getBusinessData(simpleQuery);
		return R.ok(businessData);
	}

	@Operation(summary = "运营统计概况成交统计折线图", description = "")
	@GetMapping("/deal/pic")
	public R<List<DealOrderPicDTO>> getDealOrderPic(SimpleQuery simpleQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		simpleQuery.setShopId(shopId);
		List<DealOrderPicDTO> list = dataService.getDealOrderPic(simpleQuery);
		return R.ok(list);
	}

	@Operation(summary = "运营统计概况售后统计折线图", description = "")
	@GetMapping("/return/pic")
	public R<List<ReturnOrderPicDTO>> getReturnOrderPic(SimpleQuery simpleQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		simpleQuery.setShopId(shopId);
		List<ReturnOrderPicDTO> list = dataService.getReturnOrderPic(simpleQuery);
		return R.ok(list);
	}

	@Operation(summary = "运营统计概况访问统计折线图", description = "")
	@GetMapping("/view/pic")
	public R<List<ViewPicDTO>> getViewPic(SimpleQuery simpleQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		simpleQuery.setShopId(shopId);
		List<ViewPicDTO> list = dataService.getShopViewPic(simpleQuery);
		return R.ok(list);
	}

	@Operation(summary = "营销统计列表", description = "")
	@GetMapping("/activity/page")
	public R<List<ActivityCollectPage>> getActivityPage(ActivityDataQuery activityDataQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		activityDataQuery.setShopId(shopId);
		List<ActivityCollectPage> list = dataService.getActivityPage(activityDataQuery);
		return R.ok(list);
	}

	@ExportExcel(name = "营销统计列表Excel", sheet = "趋势图成交列表")
	@Operation(summary = "营销统计列表Excel", description = "")
	@GetMapping("/activity/page/excel")
	public R<List<ActivityCollectPage>> getActivityPageExcel(ActivityDataQuery activityDataQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		activityDataQuery.setShopId(shopId);
		List<ActivityCollectPage> list = dataService.getActivityPage(activityDataQuery);
		return R.ok(list);
	}

	@Operation(summary = "营销统计分析", description = "")
	@GetMapping("/activity/detail")
	public R<ActivityCollectDetail> getActivityDetail(ActivityDataQuery activityDataQuery) {
		Long shopId = SecurityUtils.getShopUser().getShopId();
		activityDataQuery.setShopId(shopId);
		ActivityCollectDetail result = dataService.getActivityDetail(activityDataQuery);
		return R.ok(result);
	}


}
