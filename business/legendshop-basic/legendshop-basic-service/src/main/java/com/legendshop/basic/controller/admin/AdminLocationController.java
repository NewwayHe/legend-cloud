/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.controller.admin;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.LocationDTO;
import com.legendshop.basic.query.LocationQuery;
import com.legendshop.basic.service.LocationService;
import com.legendshop.common.core.constant.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author legendshop
 */
@Tag(name = "admin地区选择器")
@RestController
@RequestMapping(value = "/admin/location", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class AdminLocationController {

	private final LocationService locationService;


	/**
	 * 获取所有省数据
	 */
	@GetMapping("/loadProvince")
	@Operation(summary = "【平台】获取所有省数据")
	public R<List<LocationDTO>> loadProvince() {
		return R.ok(this.locationService.loadLocation(1));
	}


	/**
	 * 根据省份ID获取所有城市
	 *
	 * @param provincesId 省份ID
	 * @return List<LocationDTO>
	 */
	@GetMapping("/loadProvinceCity")
	@Operation(summary = "【平台】根据省份ID获取所有市数据")
	public R<List<LocationDTO>> loadProvinceCity(@RequestParam(value = "provincesId") Long provincesId) {
		return R.ok(this.locationService.getChildrenList(provincesId));
	}


	/**
	 * 获取地区下级区域列表
	 *
	 * @param parentId
	 * @return
	 */
	@Operation(summary = "【平台】获取地区下级区域列表")
	@Parameter(name = "parentId", description = "父级id", required = true)
	@GetMapping("/loadProvinceCityChildren/{parentId}")
	public R<List<LocationDTO>> getChildrenList(@PathVariable Long parentId) {
		return R.ok(locationService.getChildrenList(parentId));
	}

	/**
	 * 获取地区分頁区
	 *
	 * @param
	 * @return
	 */
	@Operation(summary = "【平台】获取地区分页")
	@GetMapping("/loadPage")
	public R<PageSupport<LocationDTO>> getLocationPage(LocationQuery locationQuery) {
		return R.ok(locationService.getPage(locationQuery));
	}

	/**
	 * 新增地区
	 */
	@Operation(summary = "【平台】新增地区 admin_location_insertLocation")
	@PreAuthorize("@pms.hasPermission('admin_location_insertLocation')")
	@PostMapping("/insertLocation")
	public R<Integer> insertLocation(@RequestBody LocationDTO locationDTO) {
		return locationService.insertLocation(locationDTO);
	}

	/**
	 * 删除地区
	 */
	@PreAuthorize("@pms.hasPermission('admin_location_deleteLocation')")
	@Operation(summary = "【平台】删除地区 admin_location_deleteLocation")
	@DeleteMapping("/deleteLocation/{id}")
	public R<Integer> deleteLocation(@PathVariable(value = "id") Long id) {
		return locationService.deleteLocation(id);
	}

	/**
	 * 修改地区
	 */
	@PreAuthorize("@pms.hasPermission('admin_location_updateLocation')")
	@Operation(summary = "【平台】修改地区 admin_location_updateLocation")
	@Parameters({
			@Parameter(name = "id", description = "修改地區ID", required = true),
			@Parameter(name = "locationName", description = "地区名", required = true)
	})
	@PutMapping("/updateLocation")
	public R<Integer> updateLocation(@RequestBody LocationQuery locationQuery) {
		return locationService.updateLocation(locationQuery);
	}
}
