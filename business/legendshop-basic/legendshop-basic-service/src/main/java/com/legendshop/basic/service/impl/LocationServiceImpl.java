/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dao.LocationDao;
import com.legendshop.basic.dto.CityDTO;
import com.legendshop.basic.dto.LocationDTO;
import com.legendshop.basic.dto.LocationDetailDTO;
import com.legendshop.basic.dto.ProvinceDTO;
import com.legendshop.basic.entity.Location;
import com.legendshop.basic.enums.LocationGradeEnum;
import com.legendshop.basic.query.LocationQuery;
import com.legendshop.basic.service.LocationService;
import com.legendshop.basic.service.convert.LocationConverter;
import com.legendshop.common.core.constant.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 地址服务实现
 *
 * @author legendshop
 */
@Service
public class LocationServiceImpl implements LocationService {

	@Autowired
	private LocationDao locationDao;

	@Autowired
	private LocationConverter locationConverter;


	@Override
	@Cacheable(value = "loadLocation", key = "'grade-' + #grade")
	public List<LocationDTO> loadLocation(Integer grade) {
		return locationConverter.to(locationDao.loadLocation(grade));
	}

	@Override
	public List<LocationDTO> getChildrenList(Long parentId) {
		return locationConverter.to(locationDao.getChildrenList(parentId));
	}

	@Override
	@Cacheable(value = "loadLocation", key = "#id")
	public LocationDTO getById(Long id) {
		return locationConverter.to(locationDao.getById(id));
	}

	@Override
	public Long getIdByName(String name, Integer grade) {
		Location location = locationDao.getByName(name, grade);
		return Optional.ofNullable(location).map(Location::getId).orElse(null);
	}

	@Override
	@Cacheable(value = "loadProvinceCity")
	public List<ProvinceDTO> loadProvinceCity() {
		List<Location> provinceList = locationDao.loadLocation(LocationGradeEnum.PROVINCE.getValue());
		List<Location> cityList = locationDao.loadLocation(LocationGradeEnum.CITY.getValue());
		Map<Long, List<Location>> cityMap = cityList.stream().collect(Collectors.groupingBy(Location::getParentId));

		List<ProvinceDTO> result = new ArrayList<>(provinceList.size());
		for (Location province : provinceList) {
			ProvinceDTO provinceDTO = locationConverter.convert2ProvinceDTO(province);
			if (cityMap.containsKey(provinceDTO.getId())) {
				List<CityDTO> cityDTOList = locationConverter.convert2CityDTO(cityMap.get(provinceDTO.getId()));
				provinceDTO.setChildren(cityDTOList);
			}
			result.add(provinceDTO);
		}
		return result;
	}

	@Override
	public Long getIdByGradeAndNameAndParentId(String name, Integer grade, Long parentId) {
		if (StrUtil.isBlank(name)) {
			return null;
		}
		return locationDao.getIdByGradeAndNameAndParentId(name, grade, parentId);
	}

	@Override
	public LocationDTO getByGradeAndNameAndParentId(String name, Integer grade, Long parentId) {
		if (StrUtil.isBlank(name)) {
			return null;
		}
		return locationDao.getByGradeAndNameAndParentId(name, grade, parentId);
	}

	@Override
	public PageSupport<LocationDTO> getPage(LocationQuery grade) {
		return locationConverter.page(locationDao.queryLocationByGrade(grade));
	}

	@Override
	@CacheEvict(value = "loadProvinceCity", allEntries = true)
	public R<Integer> insertLocation(LocationDTO locationDTO) {
		Location location = new Location();
		location.setName(locationDTO.getName());
		location.setGrade(locationDTO.getGrade() != null ? locationDTO.getGrade() : 1);
		location.setParentId(locationDTO.getParentId() != null ? locationDTO.getParentId() : 0);
		location.setCode(locationDTO.getCode());
		Long save = locationDao.save(location);
		if (save < 1) {
			return R.fail("新增地区失败");
		}
		return R.ok();
	}

	@Override
	@CacheEvict(value = "loadProvinceCity", allEntries = true)
	public R<Integer> deleteLocation(Long id) {
		int i = locationDao.deleteById(id);
		if (i < 1) {
			return R.fail("删除失败");
		}
		return R.ok();
	}

	@Override
	public R<Integer> updateLocation(LocationQuery locationQuery) {
		Location location = locationDao.getById(locationQuery.getId());
		location.setName(locationQuery.getName());
		location.setCode(locationQuery.getCode());
		int update = locationDao.update(location);
		if (update < 1) {
			return R.fail("修改失败");
		}
		return R.ok();
	}

	@Override
	public R<LocationDetailDTO> getDetailAddress(LocationDetailDTO locationDetailDTO) {
		return R.ok(locationDao.getDetailAddress(locationDetailDTO));
	}

	@Override
	public List<LocationDTO> queryByIds(List<Long> ids) {
		return locationConverter.to(locationDao.queryAllByIds(ids));
	}
}
