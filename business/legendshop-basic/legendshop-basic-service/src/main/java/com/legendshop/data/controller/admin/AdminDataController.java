/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.controller.admin;

import com.legendshop.common.core.constant.R;
import com.legendshop.common.excel.annotation.ExportExcel;
import com.legendshop.data.dto.*;
import com.legendshop.data.query.ActivityDataQuery;
import com.legendshop.data.query.SimpleQuery;
import com.legendshop.data.service.DataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据服务的控制层
 *
 * @author legendshop
 */
@Slf4j
@RestController
@RequestMapping(value = "/admin/count", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Tag(name = "数据服务")
public class AdminDataController {

	private final DataService dataService;

	@Operation(summary = "运营统计概况", description = "")
	@GetMapping("/business/data")
	public R<BusinessDataDTO> getBusinessData(SimpleQuery simpleQuery) {

		BusinessDataDTO businessData = dataService.getBusinessData(simpleQuery);
		return R.ok(businessData);
	}

	@Operation(summary = "运营统计概况成交统计折线图", description = "")
	@GetMapping("/deal/pic")
	public R<List<DealOrderPicDTO>> getDealOrderPic(SimpleQuery simpleQuery) {

		List<DealOrderPicDTO> list = dataService.getDealOrderPic(simpleQuery);
		return R.ok(list);
	}

	@Operation(summary = "运营统计概况售后统计折线图", description = "")
	@GetMapping("/return/pic")
	public R<List<ReturnOrderPicDTO>> getReturnOrderPic(SimpleQuery simpleQuery) {

		List<ReturnOrderPicDTO> list = dataService.getReturnOrderPic(simpleQuery);
		return R.ok(list);
	}

	@Operation(summary = "运营统计概况访问统计折线图", description = "")
	@GetMapping("/view/pic")
	public R<List<ViewPicDTO>> getViewPic(SimpleQuery simpleQuery) {

		List<ViewPicDTO> list = dataService.getViewPic(simpleQuery);
		return R.ok(list);
	}


	@Operation(summary = "营销统计新增使用次数折线图", description = "")
	@GetMapping("/activity/publish/pic")
	public R<List<ActivityPublishPicDTO>> getActivityPublishPic(ActivityDataQuery activityDataQuery) {
		List<ActivityPublishPicDTO> list = dataService.getActivityPublishPic(activityDataQuery);
		return R.ok(list);
	}

	@Operation(summary = "营销统计新增成交金额折线图", description = "")
	@GetMapping("/activity/deal/pic")
	public R<List<ActivityDealPicDTO>> getActivityDealPic(ActivityDataQuery activityDataQuery) {
		List<ActivityDealPicDTO> list = dataService.getActivityDealPic(activityDataQuery);
		return R.ok(list);
	}

	@Operation(summary = "营销统计列表", description = "")
	@GetMapping("/activity/page")
	public R<List<ActivityCollectPage>> getActivityPage(ActivityDataQuery activityDataQuery) {
		List<ActivityCollectPage> list = dataService.getActivityPage(activityDataQuery);
		return R.ok(list);
	}

	@Operation(summary = "营销统计列表Excel", description = "")
	@GetMapping("/activity/page/excel")
	@ExportExcel(name = "营销统计列表", sheet = "营销统计列表")
	public List<ActivityCollectPage> getActivityPageExcel(ActivityDataQuery dateQuery) {
		return dataService.getActivityPage(dateQuery);
	}
}
