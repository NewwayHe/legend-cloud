/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.legendshop.basic.api.LocationApi;
import com.legendshop.basic.dto.LocationDTO;
import com.legendshop.product.dao.TransCityDao;
import com.legendshop.product.dto.TransCityDTO;
import com.legendshop.product.entity.TransCity;
import com.legendshop.product.enums.TransCityTypeEnum;
import com.legendshop.product.service.TransCityService;
import com.legendshop.product.service.convert.TransportCityConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 每个城市的运费设置(TransCity)表服务实现类
 *
 * @author legendshop
 * @since 2020-09-04 15:13:49
 */
@Slf4j
@Service
public class TransCityServiceImpl implements TransCityService {

	@Autowired
	private TransCityDao transCityDao;

	@Autowired
	private TransportCityConverter transportCityConverter;

	@Autowired
	private LocationApi locationApi;


	@Override
	public void batchAdd(List<TransCityDTO> transCityDTOS) {
		transCityDao.batchAdd(transportCityConverter.from(transCityDTOS));
	}

	@Override
	public void delByTransId(Long transId) {
		transCityDao.delByTransId(transId);
	}

	@Override
	public List<TransCityDTO> getCityList(Long transId, Long parentId) {
		List<TransCity> cityList = transCityDao.getCityList(transId, parentId);
		return transportCityConverter.to(cityList);
	}

	@Override
	public List<TransCityDTO> getCityList(Long transId, List<Long> parentIds) {
		List<TransCity> cityList = transCityDao.getCityList(transId, parentIds);
		return transportCityConverter.to(cityList);
	}

	@Override
	public List<TransCityDTO> getCityList(List<Long> transIds, Long parentId) {
		List<TransCity> cityList = transCityDao.getCityList(transIds, parentId);
		return transportCityConverter.to(cityList);
	}

	@Override
	public String getAreaNames(List<TransCityDTO> cityDTOS, Map<Long, List<LocationDTO>> citisInProvince) {
		Map<Long, List<TransCityDTO>> transCityInProvince = cityDTOS.stream().collect(Collectors.groupingBy(TransCityDTO::getProvinceId));
		StringBuffer sb = new StringBuffer();
		transCityInProvince.forEach((provinceId, transCityDTOS) -> {
			List<LocationDTO> cities = citisInProvince.get(provinceId);
			LocationDTO province = locationApi.get(provinceId).getData();
			if (province == null) {
				log.info("省份为空, id: {}", provinceId);
				return;
			}
			sb.append(province.getName());
			if (cities.size() != transCityDTOS.size()) {
				sb.append("（部分地区）");
			}
			sb.append("、");
		});
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	@Override
	public String getDetailAreaNames(List<TransCityDTO> cityDTOS) {
		Map<Long, List<TransCityDTO>> collect = cityDTOS.stream().collect(Collectors.groupingBy(TransCityDTO::getProvinceId));
		//拼接地区名称
		StringBuffer sb = new StringBuffer();
		collect.forEach((k, transCityDTOS) -> {
			List<LocationDTO> cities = locationApi.loadCityList(k).getData();
			LocationDTO province = locationApi.get(k).getData();
			if (ObjectUtil.isNotEmpty(province)) {
				sb.append(province.getName());
			}
			if (cities.size() != transCityDTOS.size()) {
				sb.append("（");
				cities.forEach(c -> {
					transCityDTOS.forEach(city -> {
						if (c.getId().equals(city.getCityId())) {
							sb.append(c.getName());
							sb.append("、");
						}
					});
				});
				sb.setLength(sb.length() - 1);
				sb.append("）");
			} else {
				sb.append("（全省）");
			}
			sb.append("、");
		});
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}


	@Override
	public TransCityDTO getByTransIdAndType(Long transId, Long cityId, TransCityTypeEnum transCityTypeEnum) {
		return transportCityConverter.to(transCityDao.getByTransIdAndType(transId, cityId, transCityTypeEnum));
	}
}
