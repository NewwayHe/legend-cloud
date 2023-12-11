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
import com.legendshop.basic.enums.LocationGradeEnum;
import com.legendshop.basic.service.LocationService;
import com.legendshop.common.core.constant.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.AllArgsConstructor;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 地区控制器
 * {@see https://github.com/modood/Administrative-divisions-of-China}
 *
 * @author legendshop
 */
@RestController
@AllArgsConstructor
public class locationApiImpl implements LocationApi {

	private final LocationService locationService;

	/**
	 * 根据省份ID获取所有城市
	 *
	 * @param provincesId 省份ID
	 * @return List<LocationDTO>
	 */
	@Override
	@Operation(summary = "【公共】加载所有城市")
	@Parameter(name = "provincesId", description = "省份id", required = true)
	public R<List<LocationDTO>> loadCityList(@RequestParam(value = "provincesId") Long provincesId) {
		return R.ok(locationService.getChildrenList(provincesId));
	}


	/**
	 * 根据id获取地区信息
	 *
	 * @param id
	 * @return
	 */
	@Override
	@Operation(summary = "【公共】根据id获取地区信息")
	@Parameter(name = "id", description = "地区id", required = true)
	public R<LocationDTO> get(@PathVariable("id") Long id) {
		return R.ok(locationService.getById(id));
	}

	/**
	 * 根据地区名获取地区id
	 *
	 * @param name
	 * @return
	 */
	@Override
	@Operation(summary = "【公共】根据地区名获取地区id")
	@Parameters({
			@Parameter(name = "name", description = "地区名", required = true),
			@Parameter(name = "grade", description = "地区级别", required = true)
	})
	public R<Long> getIdByName(@RequestParam("name") String name, @RequestParam("grade") Integer grade) {
		return R.ok(locationService.getIdByName(name, grade));
	}

	@Override
	public R<List<LocationDTO>> loadProvinces() {
		return R.ok(this.locationService.loadLocation(LocationGradeEnum.PROVINCE.getValue()));
	}

	@Override
	public R<List<LocationDTO>> loadCitys() {
		return R.ok(this.locationService.loadLocation(LocationGradeEnum.CITY.getValue()));
	}

	/**
	 * 根据地区id查询详细地址
	 *
	 * @param locationDetailDTO
	 * @return
	 */
	@Override
	public R<LocationDetailDTO> getDetailAddress(@SpringQueryMap LocationDetailDTO locationDetailDTO) {
		return locationService.getDetailAddress(locationDetailDTO);
	}

	@Override
	public R<List<LocationDTO>> queryByIds(@RequestParam("ids") List<Long> ids) {
		return R.ok(locationService.queryByIds(ids));
	}

}
