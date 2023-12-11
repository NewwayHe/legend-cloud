/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.hutool.core.date.DateUtil;
import com.legendshop.basic.api.LocationApi;
import com.legendshop.basic.dto.LocationDTO;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.product.dao.TransFreeDao;
import com.legendshop.product.dto.TransCityDTO;
import com.legendshop.product.dto.TransFreeDTO;
import com.legendshop.product.entity.TransFree;
import com.legendshop.product.enums.TransCityTypeEnum;
import com.legendshop.product.service.TransCityService;
import com.legendshop.product.service.TransFreeService;
import com.legendshop.product.service.convert.TransportFreeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 条件包邮(TransFree)表服务实现类
 *
 * @author legendshop
 * @since 2020-09-04 16:54:51
 */
@Service
public class TransFreeServiceImpl implements TransFreeService {

	@Autowired
	private TransFreeDao transFreeDao;

	@Autowired
	private TransportFreeConverter transportFreeConverter;

	@Autowired
	private TransCityService transCityService;

	@Autowired
	private LocationApi locationApi;

	@Override
	public TransFreeDTO getById(Long id) {
		return transportFreeConverter.to(transFreeDao.getById(id));
	}

	@Override
	public Long save(TransFreeDTO transFreeDTO) {
		transFreeDTO.setRecDate(DateUtil.date());
		return transFreeDao.save(transportFreeConverter.from(transFreeDTO));
	}

	@Override
	@Caching(evict = {
			@CacheEvict(value = "transFree", key = "#transId"),
			@CacheEvict(value = "transFreeDetail", key = "#transId")
	})
	public void delByTransId(Long transId) {
		transFreeDao.delByTransId(transId);
	}


	@Override
	public void saveWithCityList(List<TransFreeDTO> transFreeDTOList, Long transportId) {
		List<TransCityDTO> transCityDTOS = new ArrayList<>();
		List<Long> cityIds = new ArrayList<>();
		transFreeDTOList.forEach(free -> {
			free.setTransId(transportId);
			Long freeId = save(free);
			free.getTransCityDTOList().forEach(c -> {
				c.setParentId(freeId);
				c.setTransId(transportId);
				c.setType(TransCityTypeEnum.CONDITION_FREE.value());
				cityIds.add(c.getCityId());
			});
			transCityDTOS.addAll(free.getTransCityDTOList());
		});
		HashSet set = new HashSet(cityIds);
		if (cityIds.size() != set.size()) {
			throw new BusinessException("存在重复的可支持销售的地区!");
		}
		transCityService.batchAdd(transCityDTOS);
	}

	@Override
	@Cacheable(value = "transFreeDetail", key = "#transId")
	public List<TransFreeDTO> getListDetailAreaByTransId(Long transId) {
		List<TransFree> transFrees = transFreeDao.getListByTransId(transId);
		List<TransFreeDTO> transFreeDTOS = transportFreeConverter.to(transFrees);
		transFreeDTOS.forEach(f -> {
			List<TransCityDTO> cityList = transCityService.getCityList(transId, f.getId());
			f.setArea(transCityService.getDetailAreaNames(cityList));
			f.setTransCityDTOList(cityList);
		});
		return transFreeDTOS;
	}

	@Override
	@Cacheable(value = "transFree", key = "#transId")
	public List<TransFreeDTO> getListAreaByTransId(Long transId) {
		List<TransFree> transFrees = transFreeDao.getListByTransId(transId);
		List<TransFreeDTO> transFreeDTOS = transportFreeConverter.to(transFrees);

		//省份里的城市
		Map<Long, List<LocationDTO>> citisInProvince = locationApi.loadCitys().getData().stream().collect(Collectors.groupingBy(LocationDTO::getParentId));


		transFreeDTOS.forEach(f -> {
			List<TransCityDTO> cityList = transCityService.getCityList(transId, f.getId());
			f.setArea(transCityService.getAreaNames(cityList, citisInProvince));
			f.setTransCityDTOList(cityList);
		});
		return transFreeDTOS;
	}
}
