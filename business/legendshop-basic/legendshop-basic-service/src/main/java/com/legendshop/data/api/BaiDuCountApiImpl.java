/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.api;

import com.legendshop.common.core.constant.R;
import com.legendshop.data.dto.UserDataViewsDTO;
import com.legendshop.data.service.BaiduViewService;
import com.legendshop.data.service.DataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author legendshop
 * @version 1.0.0
 * @title DataController
 * @date 2022/3/18 18:10
 * @description：
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class BaiDuCountApiImpl implements BaiDuCountApi {

	private final DataService dataService;

	private final BaiduViewService baiduViewService;

	@Override
	public R<UserDataViewsDTO> getPvUv(@RequestParam String source) {
		UserDataViewsDTO userViewData = dataService.getUserViewData(source);
		return R.ok(userViewData);
	}

	@Override
	public R baiduStatisticsArchive(@RequestParam(value = "startDate") Date startDate, @RequestParam(value = "endDate") Date endDate) {
		return baiduViewService.baiduStatisticsArchive(startDate, endDate);
	}
}
