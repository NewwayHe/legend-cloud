/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller.admin;

import cn.hutool.json.JSONArray;
import com.legendshop.basic.dto.RouteConfigDTO;
import com.legendshop.basic.service.RouteConfigService;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.log.annotation.SystemLog;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台动态路由控制器
 *
 * @author legendshop
 */
@RestController
@RequestMapping(value = "/admin/route/config", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminRouteConfigController {


	private final RouteConfigService routeConfigService;


	/**
	 * 获取当前定义的路由信息
	 *
	 * @return
	 */
	@GetMapping("list")
	public R<List<RouteConfigDTO>> list() {
		return R.ok(routeConfigService.queryAll());
	}

	/**
	 * 修改路由
	 *
	 * @param routes 路由定义
	 * @return
	 */
	@SystemLog("修改路由")
	@PutMapping
	public R updateRoutes(@RequestBody JSONArray routes) {
		return R.ok(routeConfigService.updateRoutes(routes));
	}

}
