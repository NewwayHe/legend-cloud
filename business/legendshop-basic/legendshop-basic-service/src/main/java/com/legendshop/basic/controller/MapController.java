/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller;

import com.legendshop.basic.bo.LngAndLatBO;
import com.legendshop.basic.util.MapUtil;
import com.legendshop.common.core.constant.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author legendshop
 */
@Tag(name = "高德地图工具")
@Slf4j
@RestController
@RequestMapping(value = "/a/map", produces = MediaType.APPLICATION_JSON_VALUE)
public class MapController {

	@Autowired
	private MapUtil mapUtil;

	@GetMapping(value = "/lng/lat")
	@Operation(summary = "【公共】根据地址获取经纬度")
	@Parameter(name = "address", description = "地址", required = true)
	public R<LngAndLatBO> getLngAndLatBO(@RequestParam(value = "address") String address) {
		log.info("basic test address :{} ", address);
		LngAndLatBO lngAndLatBO = mapUtil.getLngAndLatBO(address);
		return R.ok(lngAndLatBO);
	}


	@GetMapping(value = "/address")
	@Operation(summary = "【公共】根据经纬度获取地址")
	public R<LngAndLatBO> getAddress(@RequestParam(value = "longitude") String longitude, @RequestParam(value = "latitude") String latitude) {
		LngAndLatBO address = mapUtil.getAddress(longitude, latitude);
		return R.ok(address);
	}

	@GetMapping(value = "/addressAndLocationCode")
	@Operation(summary = "【公共】根据经纬度获取地址和省市区LocationID")
	public R<LngAndLatBO> getAddressAndLocationCode(@RequestParam(value = "longitude") String longitude, @RequestParam(value = "latitude") String latitude) {
		LngAndLatBO address = mapUtil.getAddressAndLocationCode(longitude, latitude);
		return R.ok(address);
	}
}
