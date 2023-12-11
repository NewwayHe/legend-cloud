/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.controller;

import com.legendshop.common.core.constant.R;
import com.legendshop.data.dto.UserDataViewsDTO;
import com.legendshop.data.service.DataService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 * @version 1.0.0
 * @title DataController
 * @date 2022/3/18 18:10
 * @description：
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataController {

	@Autowired
	private DataService dataService;

	@GetMapping("/baidu/view/count")
	public R<UserDataViewsDTO> getPvUv(@RequestParam String source) {
		UserDataViewsDTO userViewData = dataService.getUserViewData(source);
		return R.ok(userViewData);
	}
}
