/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.LocationDTO;
import com.legendshop.basic.dto.LocationDetailDTO;
import com.legendshop.basic.dto.ProvinceDTO;
import com.legendshop.basic.query.LocationQuery;
import com.legendshop.common.core.constant.R;

import java.util.List;

/**
 * 地址服务
 *
 * @author legendshop
 */
public interface LocationService {

	/**
	 * 根据等级获取对应地区列表
	 *
	 * @return List<LocationDTO>
	 */
	List<LocationDTO> loadLocation(Integer grade);

	/**
	 * 获取地区下级区域列表
	 *
	 * @param parentId
	 * @return
	 */
	List<LocationDTO> getChildrenList(Long parentId);

	/**
	 * 根据id获取地区信息
	 *
	 * @param id
	 * @return
	 */
	LocationDTO getById(Long id);

	/**
	 * 根据地区名获取地区id
	 *
	 * @param name
	 * @param grade
	 * @return
	 */
	Long getIdByName(String name, Integer grade);

	/**
	 * 获取所有省市数据
	 *
	 * @return
	 */
	List<ProvinceDTO> loadProvinceCity();

	/**
	 * 根据父级id、等级地区名获取地区id
	 *
	 * @param name
	 * @param grade
	 * @param parentId
	 * @return
	 */
	Long getIdByGradeAndNameAndParentId(String name, Integer grade, Long parentId);

	/**
	 * 根据父级id、等级地区名获取地区
	 *
	 * @param name
	 * @param grade
	 * @param parentId
	 * @return
	 */
	LocationDTO getByGradeAndNameAndParentId(String name, Integer grade, Long parentId);


	PageSupport<LocationDTO> getPage(LocationQuery grade);

	/**
	 * 新增地区
	 *
	 * @param
	 * @return
	 */
	R<Integer> insertLocation(LocationDTO locationDTO);

	/**
	 * 删除地区
	 *
	 * @param id 地区ID
	 * @return
	 */
	R<Integer> deleteLocation(Long id);

	/***
	 * 修改地区
	 * @param
	 * @return
	 */
	R<Integer> updateLocation(LocationQuery locationQuery);

	/**
	 * 获取详细地址
	 *
	 * @param locationDetailDTO
	 * @return
	 */
	R<LocationDetailDTO> getDetailAddress(LocationDetailDTO locationDetailDTO);

	/**
	 * 根据id获取地区信息
	 *
	 * @param ids
	 * @return
	 */
	List<LocationDTO> queryByIds(List<Long> ids);
}
