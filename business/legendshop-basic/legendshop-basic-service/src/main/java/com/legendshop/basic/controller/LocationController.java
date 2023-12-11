/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller;

import cn.hutool.core.util.ObjectUtil;
import com.legendshop.basic.dto.LocationDTO;
import com.legendshop.basic.dto.LocationDetailDTO;
import com.legendshop.basic.dto.ProvinceDTO;
import com.legendshop.basic.enums.LocationGradeEnum;
import com.legendshop.basic.service.LocationService;
import com.legendshop.basic.service.UpdateAddressService;
import com.legendshop.common.core.constant.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地区控制器
 * {@see https://github.com/modood/Administrative-divisions-of-China}
 *
 * @author legendshop
 */
@Tag(name = "地区选择器")
@RestController
@RequestMapping(value = "/location", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class LocationController {

	private final LocationService locationService;

	private final UpdateAddressService updateAddressService;

	/**
	 * 根据省份ID获取所有城市
	 *
	 * @param provincesId 省份ID
	 * @return List<LocationDTO>
	 */
	@Operation(summary = "【公共】加载所有城市")
	@Parameter(name = "provincesId", description = "省份id", required = true)
	@GetMapping(value = "/loadCities")
	public R<List<LocationDTO>> loadCityList(@RequestParam(value = "provincesId") Long provincesId) {
		return R.ok(locationService.getChildrenList(provincesId));
	}

	/**
	 * 根据id获取地区信息
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
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
	@GetMapping("/getIdByName")
	@Operation(summary = "【公共】根据地区名获取地区id")
	@Parameters({
			@Parameter(name = "name", description = "地区名", required = true),
			@Parameter(name = "grade", description = "地区级别", required = true)
	})
	public R<Long> getIdByName(@RequestParam("name") String name, @RequestParam("grade") Integer grade) {
		return R.ok(locationService.getIdByName(name, grade));
	}


	/**
	 * 根据等级获取对应地区列表
	 *
	 * @param
	 * @return
	 */
	@Operation(summary = "【公共】根据等级获取对应地区列表")
	@Parameter(name = "grade", description = "等级  1：省级 2：市级 3：区级 4：街道", required = true)
	@GetMapping("/provinces")
	public R<List<LocationDTO>> loadProvincesEntity(@RequestParam Integer grade) {
		return R.ok(locationService.loadLocation(grade));
	}

	@GetMapping(value = "/loadProvinces")
	public R<List<LocationDTO>> loadProvinces() {
		return R.ok(this.locationService.loadLocation(LocationGradeEnum.PROVINCE.getValue()));
	}

	@GetMapping(value = "/loadCitys")
	public R<List<LocationDTO>> loadCitys() {
		return R.ok(this.locationService.loadLocation(LocationGradeEnum.CITY.getValue()));
	}


	/**
	 * 获取地区下级区域列表
	 *
	 * @param parentId
	 * @return
	 */
	@Operation(summary = "【公共】获取地区下级区域列表")
	@Parameter(name = "parentId", description = "父级id", required = true)
	@GetMapping("/children/{parentId}")
	public R<List<LocationDTO>> getChildrenList(@PathVariable Long parentId) {
		if (ObjectUtil.isEmpty(parentId)) {
			return R.fail("获取地区下级区域列表失败！");
		}
		return R.ok(locationService.getChildrenList(parentId));
	}

	/**
	 * 更新地址库
	 *
	 * @param path 地址库json 文件路径
	 * @return
	 */
	@Operation(summary = "【公共】更新地址库")
	@Parameter(name = "path", description = "地址库json 文件路径", required = true)
	@PostMapping("/update")
	public R updateStreets(@RequestParam String path) {
		return updateAddressService.updateAddress(path);
	}

	/**
	 * 获取所有省市数据
	 */
	@GetMapping("/loadProvinceCity")
	@Operation(summary = "【公共】获取所有省市数据")
	public R<List<ProvinceDTO>> loadProvinceCity() {
		return R.ok(this.locationService.loadProvinceCity());
	}

	/**
	 * 根据地区id查询详细地址
	 *
	 * @param locationDetailDTO
	 * @return
	 */
	@GetMapping("/getDetailAddress")
	public R<LocationDetailDTO> getDetailAddress(@SpringQueryMap LocationDetailDTO locationDetailDTO) {
		return locationService.getDetailAddress(locationDetailDTO);
	}

	@GetMapping("/queryByIds")
	public R<List<LocationDTO>> queryByIds(@RequestParam("ids") List<Long> ids) {
		return R.ok(locationService.queryByIds(ids));
	}

}
