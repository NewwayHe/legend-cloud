/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.legendshop.basic.dao.LocationDao;
import com.legendshop.basic.entity.Location;
import com.legendshop.basic.enums.LocationGradeEnum;
import com.legendshop.basic.service.UpdateAddressService;
import com.legendshop.common.core.comparer.DataComparer;
import com.legendshop.common.core.comparer.DataComparerResult;
import com.legendshop.common.core.comparer.DataListComparer;
import com.legendshop.common.core.constant.R;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 更新地址库服务实现
 *
 * @author legendshop
 */
@Service
public class UpdateAddressServiceImpl implements UpdateAddressService {

	@Autowired
	private LocationDao locationDao;

	private DataListComparer<CityTest, Location> cityComparer;

	private DataListComparer<AreaTest, Location> areaComparer;

	private DataListComparer<StreetTest, Location> townComparer;

	@Override
	@Caching(evict = {
			@CacheEvict(value = "LocationList", allEntries = true),
			@CacheEvict(value = "loadProvinceCity", allEntries = true),
			@CacheEvict(value = "loadLocation", allEntries = true)
	})
	public R updateAddress(String path) {

		JSONArray objects = JSONUtil.parseArray(readJsonFile(path));
		List<ProvinceTest> result = JSONUtil.toList(objects, ProvinceTest.class);

		if (ObjectUtils.isEmpty(result)) {
			return R.fail("找不到该文件");
		}

		List<CityTest> cityTests = new ArrayList<>();
		List<AreaTest> areaTests = new ArrayList<>();
		List<StreetTest> streetTests = new ArrayList<>();
		// 以省会邮编为key
		Map<String, Location> provinceMap = locationDao.loadLocation(LocationGradeEnum.PROVINCE.getValue()).stream().collect(Collectors.toMap(Location::getCode, province -> province));

		// 数据库里的原市级数据
		List<Location> cities = locationDao.loadLocation(LocationGradeEnum.CITY.getValue());

		// 先处理市级
		for (ProvinceTest provinceTest : result) {
			// 因为github数据没有补0，这里要手动补
			Location province = provinceMap.get(provinceTest.getCode());
			provinceTest.setId(province.getId());

			if (ObjectUtil.isNotEmpty(provinceTest.getChildren())) {
				for (CityTest cityTest : provinceTest.getChildren()) {
					cityTest.setParentId(province.getId());
					cityTest.setGrade(LocationGradeEnum.CITY.getValue());
				}
				cityTests.addAll(provinceTest.getChildren());
			}
		}

		DataComparerResult<Location> cityCompare = getCityComparer().compare(cityTests, cities, null);

		List<Location> citiesUpdate = cityCompare.getUpdateList();
		List<Location> citiesAdd = cityCompare.getAddList();
		locationDao.update(citiesUpdate);
		locationDao.save(citiesAdd);

		// 获取更新后的数据
		cities = locationDao.loadLocation(LocationGradeEnum.CITY.getValue());

		// 以市邮编为key
		Map<String, Location> cityMap = cities.stream().collect(Collectors.toMap(Location::getCode, city -> city));

		// 给所有市级数据赋值
		for (CityTest cityTest : cityTests) {
			if (cityMap.containsKey(cityTest.getCode())) {
				Location city = cityMap.get(cityTest.getCode());
				cityTest.setId(city.getId());
			}

			// 开始处理 区级 数据
			if (ObjectUtil.isNotEmpty(cityTest.getChildren())) {
				for (AreaTest areaTest : cityTest.getChildren()) {
					areaTest.setParentId(cityTest.getId());
					areaTest.setGrade(LocationGradeEnum.AREA.getValue());
				}
				areaTests.addAll(cityTest.getChildren());
			}
		}

		List<Location> areas = locationDao.loadLocation(LocationGradeEnum.AREA.getValue());

		DataComparerResult<Location> areaComparer = getAreaComparer().compare(areaTests, areas, null);

		List<Location> areaUpdate = areaComparer.getUpdateList();
		List<Location> areaAdd = areaComparer.getAddList();

		locationDao.update(areaUpdate);
		locationDao.save(areaAdd);

		// 重新获取数据
		areas = locationDao.loadLocation(LocationGradeEnum.AREA.getValue());
		Map<String, Location> areaMap = areas.stream().collect(Collectors.toMap(Location::getCode, area -> area));

		List<Location> streetsList = locationDao.loadLocation(LocationGradeEnum.STREET.getValue());

		// 第一次插入，街道没有数据
		if (ObjectUtil.isEmpty(streetsList)) {
			Location streets = null;

			streetsList = new ArrayList<>();

			for (AreaTest areaTest : areaTests) {

				if (areaMap.containsKey(areaTest.getCode())) {
					areaTest.setId(areaMap.get(areaTest.getCode()).getId());
				}

				// 开始处理街道数据
				if (ObjectUtil.isNotEmpty(areaTest.getChildren())) {
					for (StreetTest streetTest : areaTest.getChildren()) {
						streets = new Location();
						streets.setParentId(areaTest.getId());
						streets.setCode(streetTest.getCode());
						streets.setName(streetTest.getName());
						streets.setGrade(LocationGradeEnum.STREET.getValue());
						streetsList.add(streets);
					}
				}
			}

			locationDao.save(streetsList);

		} else {
			// 为将来的扩展数据保留更新方法
			for (AreaTest areaTest : areaTests) {
				if (areaMap.containsKey(areaTest.getCode())) {
					areaTest.setId(areaMap.get(areaTest.getCode()).getId());
				}

				// 开始处理街道数据
				if (ObjectUtil.isNotEmpty(areaTest.getChildren())) {
					for (StreetTest streetTest : areaTest.getChildren()) {
						streetTest.setParentId(areaTest.getId());
						streetTest.setGrade(LocationGradeEnum.STREET.getValue());
					}

					streetTests.addAll(areaTest.getChildren());
				}
			}

			DataComparerResult<Location> townComparer = getTownComparer().compare(streetTests, streetsList, null);

			List<Location> townUpdate = townComparer.getUpdateList();
			List<Location> townAdd = townComparer.getAddList();

			locationDao.update(townUpdate);
			locationDao.save(townAdd);
		}
		return R.ok("更新完成");
	}

	@Data
	@ToString
	private static class ProvinceTest {

		private Long id;

		private String code;

		private String name;

		private List<CityTest> children;

	}

	@Data
	@ToString
	private static class CityTest {
		private Long id;

		private String code;

		private String name;

		private Long parentId;

		private Integer grade;

		private List<AreaTest> children;

	}

	@Data
	@ToString
	private static class AreaTest {
		private Long id;

		private String code;

		private String name;

		private Long parentId;

		private Integer grade;

		private List<StreetTest> children;
	}

	@Data
	@ToString
	private static class StreetTest {
		private String code;

		private String name;

		private Long parentId;

		private Integer grade;
	}

	/**
	 * 获取文件内容
	 *
	 * @param fileName
	 * @return
	 */
	private static String readJsonFile(String fileName) {
		String jsonStr = "";
		try {
			File jsonFile = new File(fileName);
			FileReader fileReader = new FileReader(jsonFile);
			Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8);
			int ch = 0;
			StringBuffer sb = new StringBuffer();
			while ((ch = reader.read()) != -1) {
				sb.append((char) ch);
			}
			fileReader.close();
			reader.close();
			jsonStr = sb.toString();
			return jsonStr;
		} catch (IOException e) {
			return null;
		}
	}

	private DataListComparer<CityTest, Location> getCityComparer() {
		if (cityComparer == null) {
			cityComparer = new DataListComparer<CityTest, Location>(new CityComparer());
		}
		return cityComparer;
	}

	private DataListComparer<AreaTest, Location> getAreaComparer() {
		if (areaComparer == null) {
			areaComparer = new DataListComparer<AreaTest, Location>(new AreaComparer());
		}
		return areaComparer;
	}

	private DataListComparer<StreetTest, Location> getTownComparer() {
		if (townComparer == null) {
			townComparer = new DataListComparer<StreetTest, Location>(new TownComparer());
		}
		return townComparer;
	}

	/**
	 * 城市比较工具类
	 */
	private static class CityComparer implements DataComparer<CityTest, Location> {
		@Override
		public boolean needUpdate(CityTest dto, Location dbObj, Object obj) {
			String code = dto.getCode();
			// 名字相同但邮编不同
			if (dto.getName().equals(dbObj.getName())) {
				if (code.compareTo(dbObj.getCode()) != 0) {
					dbObj.setCode(code);
					return true;
				}
			}

			// 邮编相同但名字不同
			if (code.compareTo(dbObj.getCode()) == 0) {
				if (!dto.getName().equals(dbObj.getName())) {
					dbObj.setName(dto.getName());
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean isExist(CityTest dto, Location dbObj) {
			String code = dto.getCode();
			dto.setCode(code);
			if (dto.getParentId().equals(dbObj.getParentId())) {
				// 名称相同或者邮编相同
				if (dto.getName().equals(dbObj.getName())) {
					return true;
				} else if (code.compareTo(dbObj.getCode()) == 0) {
					return true;
				}
			}
			return false;
		}

		@Override
		public Location copyProperties(CityTest dtoj, Object obj) {
			Location city = new Location();
			city.setCode(dtoj.getCode());
			city.setName(dtoj.getName());
			city.setParentId(dtoj.getParentId());
			city.setGrade(dtoj.getGrade());
			return city;
		}
	}

	/**
	 * 地区比较工具类
	 */
	private static class AreaComparer implements DataComparer<AreaTest, Location> {
		@Override
		public boolean needUpdate(AreaTest dto, Location dbObj, Object obj) {
			String code = dto.getCode();

			// 名字相同但邮编不同
			if (dto.getName().equals(dbObj.getName())) {
				if (!code.equals(dbObj.getCode())) {
					dbObj.setCode(code);
					return true;
				}
			}

			// 邮编相同但名字不同
			if (code.equals(dbObj.getCode())) {
				if (!dto.getName().equals(dbObj.getName())) {
					dbObj.setName(dto.getName());
					return true;
				}
			}

			return false;

		}

		@Override
		public boolean isExist(AreaTest dto, Location dbObj) {
			String code = dto.getCode();
			// 同地区下
			if (dto.getParentId().equals(dbObj.getParentId())) {
				// 名称相同或者邮编相同
				if (dto.getName().equals(dbObj.getName())) {
					return true;
				} else if (code.equals(dbObj.getCode())) {
					return true;
				}
			}
			return false;
		}

		@Override
		public Location copyProperties(AreaTest dtoj, Object obj) {
			Location area = new Location();
			area.setCode(dtoj.getCode());
			area.setName(dtoj.getName());
			area.setParentId(dtoj.getParentId());
			area.setGrade(dtoj.getGrade());
			return area;
		}
	}

	private static class TownComparer implements DataComparer<StreetTest, Location> {

		@Override
		public boolean needUpdate(StreetTest dto, Location dbObj, Object obj) {
			String code = dto.getCode();

			// 名字相同但邮编不同
			if (dto.getName().equals(dbObj.getName())) {
				if (!code.equals(dbObj.getCode())) {
					dbObj.setCode(code);
					return true;
				}
			}

			// 邮编相同但名字不同
			if (code.equals(dbObj.getCode())) {
				if (!dto.getName().equals(dbObj.getName())) {
					dbObj.setName(dto.getName());
					return true;
				}
			}

			return false;
		}

		@Override
		public boolean isExist(StreetTest dto, Location dbObj) {
			String code = dto.getCode();
			if (dto.getParentId().equals(dbObj.getParentId())) {
				// 名称相同或者邮编相同
				if (dto.getName().equals(dbObj.getName())) {
					return true;
				} else if (code.equals(dbObj.getCode())) {
					return true;
				}
			}
			return false;
		}

		@Override
		public Location copyProperties(StreetTest dtoj, Object obj) {
			Location town = new Location();
			town.setCode(dtoj.getCode());
			town.setName(dtoj.getName());
			town.setParentId(dtoj.getParentId());
			town.setGrade(dtoj.getGrade());
			return town;
		}
	}
}
