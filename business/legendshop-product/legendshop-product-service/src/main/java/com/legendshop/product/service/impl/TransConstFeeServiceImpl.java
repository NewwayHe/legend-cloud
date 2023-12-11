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
import com.legendshop.product.dao.TransConstFeeDao;
import com.legendshop.product.dto.TransCityDTO;
import com.legendshop.product.dto.TransConstFeeDTO;
import com.legendshop.product.entity.TransConstFee;
import com.legendshop.product.enums.TransCityTypeEnum;
import com.legendshop.product.service.TransCityService;
import com.legendshop.product.service.TransConstFeeService;
import com.legendshop.product.service.convert.TransConstFeeConverter;
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
 * 固定运费(TransConstFee)表服务实现类
 *
 * @author legendshop
 * @since 2020-09-07 14:43:46
 */
@Service
public class TransConstFeeServiceImpl implements TransConstFeeService {

	@Autowired
	private TransConstFeeDao transConstFeeDao;

	@Autowired
	private TransConstFeeConverter transConstFeeConverter;

	@Autowired
	private TransCityService transCityService;

	@Autowired
	private LocationApi locationApi;

	@Override
	public TransConstFeeDTO getById(Long id) {
		return transConstFeeConverter.to(transConstFeeDao.getById(id));
	}

	@Override
	public Long save(TransConstFeeDTO transConstFeeDTO) {
		Long id = transConstFeeDao.save(transConstFeeConverter.from(transConstFeeDTO));
		return id;
	}

	@Override
	public void saveWithCityList(List<TransConstFeeDTO> transConstFeeDTOList, Long transportId) {
		List<TransCityDTO> transCityDTOS = new ArrayList<>();
		List<Long> cityIds = new ArrayList<>();
		transConstFeeDTOList.forEach(transConstFeeDTO -> {
			transConstFeeDTO.setTransId(transportId);
			transConstFeeDTO.setRecDate(DateUtil.date());
			Long id = save(transConstFeeDTO);
			transConstFeeDTO.getTransCityDTOList().forEach(c -> {
				c.setParentId(id);
				c.setTransId(transportId);
				c.setType(TransCityTypeEnum.CONSTANT_FREIGHT.value());
				cityIds.add(c.getCityId());
			});
			transCityDTOS.addAll(transConstFeeDTO.getTransCityDTOList());
		});
		HashSet set = new HashSet(cityIds);
		if (cityIds.size() != set.size()) {
			throw new BusinessException("存在重复的可支持销售的地区!");
		}
		transCityService.batchAdd(transCityDTOS);
	}


	@Override
	@Caching(evict = {
			@CacheEvict(value = "transConstFee", key = "#transId"),
			@CacheEvict(value = "transConstFeeDetail", key = "#transId")
	})
	public void delByTransId(Long transId) {
		transConstFeeDao.delByTransId(transId);
	}

	@Override
	@Cacheable(value = "transConstFee", key = "#transId")
	public List<TransConstFeeDTO> getListAreaByTransId(Long transId) {
		List<TransConstFee> list = transConstFeeDao.getListByTransId(transId);
		List<TransConstFeeDTO> transConstFeeDTOS = transConstFeeConverter.to(list);

		//省份里的城市
		Map<Long, List<LocationDTO>> citisInProvince = locationApi.loadCitys().getData().stream().collect(Collectors.groupingBy(LocationDTO::getParentId));

		transConstFeeDTOS.forEach(transConstFeeDTO -> {
			List<TransCityDTO> cityList = transCityService.getCityList(transId, transConstFeeDTO.getId());
			transConstFeeDTO.setArea(transCityService.getAreaNames(cityList, citisInProvince));
			transConstFeeDTO.setTransCityDTOList(cityList);
		});
		return transConstFeeDTOS;
	}

	@Override
	@Cacheable(value = "transConstFeeDetail", key = "#transId")
	public List<TransConstFeeDTO> getListDetailAreaByTransId(Long transId) {
		List<TransConstFee> list = transConstFeeDao.getListByTransId(transId);
		List<TransConstFeeDTO> transConstFeeDTOS = transConstFeeConverter.to(list);
		transConstFeeDTOS.forEach(transConstFeeDTO -> {
			List<TransCityDTO> cityList = transCityService.getCityList(transId, transConstFeeDTO.getId());
			transConstFeeDTO.setArea(transCityService.getDetailAreaNames(cityList));
			transConstFeeDTO.setTransCityDTOList(cityList);
		});
		return transConstFeeDTOS;
	}
}
