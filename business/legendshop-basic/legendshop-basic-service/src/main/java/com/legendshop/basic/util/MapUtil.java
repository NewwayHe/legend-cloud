/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.legendshop.basic.bo.LngAndLatBO;
import com.legendshop.basic.dto.LocationDTO;
import com.legendshop.basic.dto.MapDTO;
import com.legendshop.basic.enums.LocationGradeEnum;
import com.legendshop.basic.enums.SysParamNameEnum;
import com.legendshop.basic.service.LocationService;
import com.legendshop.basic.service.SysParamsService;
import com.legendshop.common.core.expetion.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 高德地图工具类
 *
 * @author legendshop
 */
@Component
@Slf4j
public class MapUtil {

	@Autowired
	private SysParamsService sysParamsService;

	@Autowired
	private LocationService locationService;

	/**
	 * 根据地址获得经纬度
	 *
	 * @param address
	 * @return
	 */
	public LngAndLatBO getLngAndLatBO(String address) {
		Map<String, Object> paramMap = new HashMap(16);
		String url = "https://restapi.amap.com/v3/geocode/geo";
		paramMap.put("output", "JSON");
		paramMap.put("key", sysParamsService.getConfigDtoByParamName(SysParamNameEnum.A_MAP.name(), MapDTO.class).getKey());
		paramMap.put("address", address);
		log.info("请求高德接口经纬度，参数,{}", paramMap);
		String s = HttpUtil.get(url, paramMap);
		JSONObject jsonObject = JSONUtil.parseObj(s);
		log.info("请求结果，{}", jsonObject);
		String status = (String) jsonObject.get("status");
		if (null != status && !"1".equals(status)) {
			throw new BusinessException((String) jsonObject.get("info"));
		}
		JSONArray jsonArray = (JSONArray) jsonObject.get("geocodes");
		if (jsonArray.size() > 0) {
			JSONObject o = (JSONObject) jsonArray.get(0);
			String location = (String) o.get("location");
			List<String> locationList = Arrays.asList(location.split(",")).stream().map(l -> l.trim()).collect(Collectors.toList());
			if (locationList.size() > 1) {
				LngAndLatBO bo = new LngAndLatBO();
				bo.setLongitude(locationList.get(0));
				bo.setLatitude(locationList.get(1));
				return bo;
			}
		}
		return null;
	}


	/**
	 * 根据经纬度获取地址
	 *
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	public LngAndLatBO getAddress(String longitude, String latitude) {
		Map<String, Object> paramMap = new HashMap(16);
		String url = "https://restapi.amap.com/v3/geocode/regeo";
		paramMap.put("output", "JSON");
		paramMap.put("key", sysParamsService.getConfigDtoByParamName(SysParamNameEnum.A_MAP.name(), MapDTO.class).getKey());
		paramMap.put("radius", "1000");
		paramMap.put("extensions", "base");
		if (StrUtil.hasBlank(longitude, latitude)) {
			log.error("经纬度参数错误");
			throw new BusinessException("经纬度参数错误！！！");
		}
		paramMap.put("location", longitude + "," + latitude);
		log.info("请求高德接口获取详细地址，参数,{}", paramMap);
		String s = HttpUtil.get(url, paramMap);
		JSONObject jsonObject = JSONUtil.parseObj(s, JSONConfig.create().setIgnoreNullValue(true));
		log.info("请求结果，{}", jsonObject);
		String status = (String) jsonObject.get("status");
		if (null != status && !"1".equals(status)) {
			throw new BusinessException((String) jsonObject.get("info"));
		}
		JSONObject areaObject = (JSONObject) jsonObject.get("regeocode");
		LngAndLatBO lngAndLatBO = new LngAndLatBO();
		String formattedAddress = areaObject.getStr("formatted_address");
		JSONObject addressComponent = areaObject.getJSONObject("addressComponent");
		String province = addressComponent.getStr("province");
		Object city = addressComponent.get("city");
		// 解决省会城市时，city为空数组的问题
		if (city instanceof JSONArray) {
			lngAndLatBO.setCity(province);
			//新疆地区特殊处理
			if ("659003".equals(addressComponent.get("adcode"))) {
				lngAndLatBO.setCity(addressComponent.get("district").toString());
				//香港地区特殊处理
			} else if ("1852".equals(addressComponent.get("citycode"))) {
				lngAndLatBO.setCity(addressComponent.get("district").toString());
			}
		} else {
			lngAndLatBO.setCity((String) city);
		}
		String district = addressComponent.getStr("district");
		String township = addressComponent.getStr("township");

		lngAndLatBO.setProvince(province);
		//香港地区特殊处理
		if (!"1852".equals(addressComponent.get("citycode"))) {
			lngAndLatBO.setDistrict(district);
		}
		lngAndLatBO.setTownship(township);
		lngAndLatBO.setFormattedAddress(formattedAddress);
		return lngAndLatBO;
	}

	/**
	 * 根据经纬度获取地址和省市区对应的locationID
	 *
	 * @param longitude
	 * @param latitude
	 * @return (113.30316165099491, 22.996978860288196);
	 */
	public LngAndLatBO getAddressAndLocationCode(String longitude, String latitude) {
		LngAndLatBO lngAndLatBO = getAddress(longitude, latitude);
		lngAndLatBO.setLongitude(longitude);
		lngAndLatBO.setLatitude(latitude);
		lngAndLatBO.setProvinceCode(locationService.getIdByGradeAndNameAndParentId(lngAndLatBO.getProvince(), LocationGradeEnum.PROVINCE.getValue(), 0L));
		lngAndLatBO.setCityCode(locationService.getIdByGradeAndNameAndParentId(lngAndLatBO.getCity(), LocationGradeEnum.CITY.getValue(), lngAndLatBO.getProvinceCode()));

		if (ObjectUtil.isEmpty(lngAndLatBO.getCityCode())) {
			lngAndLatBO.setCityCode(lngAndLatBO.getProvinceCode());
		}

		LocationDTO districtDTO = locationService.getByGradeAndNameAndParentId(lngAndLatBO.getDistrict(), LocationGradeEnum.AREA.getValue(), lngAndLatBO.getCityCode());
		if (null == districtDTO) {
			return lngAndLatBO;
		}
		lngAndLatBO.setDistrictCode(districtDTO.getId());
		lngAndLatBO.setDistrict(districtDTO.getName());

		LocationDTO townDTO = locationService.getByGradeAndNameAndParentId(lngAndLatBO.getTownship(), LocationGradeEnum.STREET.getValue(), lngAndLatBO.getDistrictCode());
		if (null == townDTO) {
			return lngAndLatBO;
		}
		lngAndLatBO.setTownshipCode(townDTO.getId());
		lngAndLatBO.setTownship(townDTO.getName());
		return lngAndLatBO;
	}
}
