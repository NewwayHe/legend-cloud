/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.service;

import com.legendshop.basic.dto.LocationDTO;
import com.legendshop.product.dto.TransCityDTO;
import com.legendshop.product.enums.TransCityTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * 每个城市的运费设置(TransCity)表服务接口
 *
 * @author legendshop
 * @since 2020-09-04 15:13:50
 */
public interface TransCityService {


	/**
	 * 批量添加
	 *
	 * @param transCityDTOS
	 */
	void batchAdd(List<TransCityDTO> transCityDTOS);


	/**
	 * 删除模板下设置的区域
	 *
	 * @param transId
	 */
	void delByTransId(Long transId);

	/**
	 * 获取城市列表
	 *
	 * @param transId
	 * @param parentId
	 * @return
	 */
	List<TransCityDTO> getCityList(Long transId, Long parentId);

	/**
	 * 获取城市列表
	 *
	 * @param transId
	 * @param parentIds
	 * @return
	 */
	List<TransCityDTO> getCityList(Long transId, List<Long> parentIds);

	/**
	 * 获取城市列表
	 *
	 * @param transIds 运费模板ID列表
	 * @param parentId
	 * @return
	 */
	List<TransCityDTO> getCityList(List<Long> transIds, Long parentId);

	/**
	 * 获取区域名
	 *
	 * @param cityDTOS
	 * @return
	 */
	String getAreaNames(List<TransCityDTO> cityDTOS, Map<Long, List<LocationDTO>> citisInProvince);

	/**
	 * 获取详细的区域名
	 *
	 * @param cityDTOS
	 * @return
	 */
	String getDetailAreaNames(List<TransCityDTO> cityDTOS);

	/**
	 * 根据模板id和类型获取
	 *
	 * @param transId
	 * @param cityId
	 * @param transCityTypeEnum
	 * @return
	 */
	TransCityDTO getByTransIdAndType(Long transId, Long cityId, TransCityTypeEnum transCityTypeEnum);

}
