/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import com.legendshop.basic.bo.LngAndLatBO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 高德地图工具
 *
 * @author legendshop
 */
@FeignClient(contextId = "mapApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface MapApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	/**
	 * 根据地址获取经纬度
	 *
	 * @param address
	 * @return
	 */
	@GetMapping(value = PREFIX + "/a/map/lng/lat")
	R<LngAndLatBO> getLngAndLatBO(@RequestParam(value = "address") String address);


	/**
	 * 根据经纬度获取地址
	 *
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	@GetMapping(value = PREFIX + "/a/map/address")
	R<LngAndLatBO> getAddress(@RequestParam(value = "longitude") String longitude, @RequestParam(value = "latitude") String latitude);

	/**
	 * 根据经纬度获取地址和省市区LocationID
	 *
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	@GetMapping(value = PREFIX + "/a/map/addressAndLocationCode")
	R<LngAndLatBO> getAddressAndLocationCode(@RequestParam(value = "longitude") String longitude, @RequestParam(value = "latitude") String latitude);
}
