/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.legendshop.basic.api.LocationApi;
import com.legendshop.basic.dto.LocationDTO;
import com.legendshop.common.core.expetion.BusinessException;
import com.legendshop.product.dao.TransFeeDao;
import com.legendshop.product.dto.TransCityDTO;
import com.legendshop.product.dto.TransFeeDTO;
import com.legendshop.product.entity.TransFee;
import com.legendshop.product.enums.TransCityTypeEnum;
import com.legendshop.product.service.TransCityService;
import com.legendshop.product.service.TransFeeService;
import com.legendshop.product.service.convert.TransportFeeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 运输费用(TransFee)表服务实现类
 *
 * @author legendshop
 * @since 2020-09-04 15:14:03
 */
@Service
@Slf4j
public class TransFeeServiceImpl implements TransFeeService {

	@Autowired
	private TransFeeDao transFeeDao;


	@Autowired
	private TransportFeeConverter transportFeeConverter;

	@Autowired
	private TransCityService transCityService;

	@Autowired
	private LocationApi locationApi;

	@Override
	public TransFeeDTO getTransFee(Long id) {
		TransFee transFee = transFeeDao.getById(id);
		return transportFeeConverter.to(transFee);
	}

	@Override
	public Long saveTransFee(TransFeeDTO transFeeDTO) {

		transFeeDTO.setRecDate(DateUtil.date());
		return transFeeDao.save(transportFeeConverter.from(transFeeDTO));
	}


	@Override
	public void batchAdd(List<TransFeeDTO> transFees) {
		List<TransFee> feeList = transportFeeConverter.from(transFees);
		transFeeDao.batchAdd(feeList);
	}

	@Override
	@Caching(evict = {
			@CacheEvict(value = "transFee", key = "#transId"),
			@CacheEvict(value = "transFeeDetail", key = "#transId")
	})
	public void delByTransId(Long transId) {
		transFeeDao.delByTransId(transId);
	}


	@Override
	public void saveWithCityList(List<TransFeeDTO> transFeeDTOList, Long transportId) {
		List<TransCityDTO> transCityDTOS = new ArrayList<>();
		List<Long> cityIds = new ArrayList<>();
		transFeeDTOList.forEach(f -> {
			f.setTransId(transportId);
			Long feeId = saveTransFee(f);
			List<TransCityDTO> cityDTOList = f.getTransCityDTOList();
			cityDTOList.forEach(c -> {
				c.setParentId(feeId);
				c.setTransId(transportId);
				c.setType(TransCityTypeEnum.FREIGHT_CAL.value());
				cityIds.add(c.getCityId());
			});
			transCityDTOS.addAll(cityDTOList);
		});
		HashSet set = new HashSet(cityIds);
		if (cityIds.size() != set.size()) {
			throw new BusinessException("存在重复的可支持销售的地区!");
		}
		transCityService.batchAdd(transCityDTOS);
	}

	@Override
	@Cacheable(value = "transFeeDetail", key = "#transId")
	public List<TransFeeDTO> getListDetailAreaByTransId(Long transId) {
		List<TransFee> transFees = transFeeDao.getListByTransId(transId);
		List<TransFeeDTO> transFeeDTOS = transportFeeConverter.to(transFees);
		transFeeDTOS.forEach(f -> {
			List<TransCityDTO> cityList = transCityService.getCityList(transId, f.getId());
			String areaNames = transCityService.getDetailAreaNames(cityList);
			f.setArea(areaNames);
			f.setTransCityDTOList(cityList);
		});
		return transFeeDTOS;
	}

	@Override
	@Cacheable(value = "transFee", key = "#transId")
	public List<TransFeeDTO> getListAreaByTransId(Long transId) {
		List<TransFee> transFees = transFeeDao.getListByTransId(transId);
		if (CollUtil.isEmpty(transFees)) {
			log.info("getListAreaByTransId reurn null by transId = {}", transId);
			return Collections.emptyList();
		}

		List<Long> transFeeIds = transFees.stream().map(e -> e.getId()).collect(Collectors.toList());
		List<TransCityDTO> cityListInTransFees = transCityService.getCityList(transId, transFeeIds);
		Map<Long, List<TransCityDTO>> cityListInTransFeesMap = cityListInTransFees.stream().collect(Collectors.groupingBy(TransCityDTO::getParentId));

		//省份里的城市
		Map<Long, List<LocationDTO>> citisInProvince = locationApi.loadCitys().getData().stream().collect(Collectors.groupingBy(LocationDTO::getParentId));

		List<TransFeeDTO> transFeeDTOS = transportFeeConverter.to(transFees);
		transFeeDTOS.forEach(f -> {
			List<TransCityDTO> cityList = cityListInTransFeesMap.get(f.getId());
			String areaNames = transCityService.getAreaNames(cityList, citisInProvince);
			f.setArea(areaNames);
			f.setTransCityDTOList(cityList);
		});
		return transFeeDTOS;
	}
}
