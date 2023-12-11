/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.api;

import com.legendshop.basic.dto.LocationDTO;
import com.legendshop.basic.dto.LocationDetailDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.common.core.constant.ServiceNameConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 地区服务
 *
 * @author legendshop
 */
@FeignClient(contextId = "locationApi", value = ServiceNameConstants.BASIC_SERVICE)
public interface LocationApi {

	String PREFIX = ServiceNameConstants.BASIC_SERVICE_RPC_PREFIX;

	/**
	 * 根据省份ID获取所有城市
	 *
	 * @param provincesId 省份ID
	 * @return List<LocationDTO>
	 */
	@GetMapping(value = PREFIX + "/location/loadCities")
	R<List<LocationDTO>> loadCityList(@RequestParam(value = "provincesId") Long provincesId);

	/**
	 * 获取所有省份
	 *
	 * @return List<LocationDTO>
	 */
	@GetMapping(value = PREFIX + "/location/loadProvinces")
	R<List<LocationDTO>> loadProvinces();

	/**
	 * 加载所有的城市
	 *
	 * @return
	 */
	@GetMapping(value = PREFIX + "/location/loadCitys")
	R<List<LocationDTO>> loadCitys();

	/**
	 * 根据地区名获取地区id
	 *
	 * @param name
	 * @param grade
	 * @return
	 */
	@GetMapping(PREFIX + "/location/getIdByName")
	R<Long> getIdByName(@RequestParam("name") String name, @RequestParam("grade") Integer grade);

	/**
	 * 根据id获取地区信息
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(PREFIX + "/location/{id}")
	R<LocationDTO> get(@PathVariable("id") Long id);

	/**
	 * 查询详细地址
	 *
	 * @param locationDetailDTO
	 * @return
	 */
	@GetMapping(PREFIX + "/location/getDetailAddress")
	R<LocationDetailDTO> getDetailAddress(@SpringQueryMap LocationDetailDTO locationDetailDTO);


	/**
	 * 根据id获取地区信息
	 *
	 * @param ids
	 * @return
	 */
	@GetMapping(PREFIX + "/location/queryByIds")
	R<List<LocationDTO>> queryByIds(@RequestParam("ids") List<Long> ids);


}
