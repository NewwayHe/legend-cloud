/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.basic.dto.LocationDTO;
import com.legendshop.basic.dto.LocationDetailDTO;
import com.legendshop.basic.entity.Location;
import com.legendshop.basic.query.LocationQuery;

import java.util.List;

/**
 * 地址DAO
 *
 * @author legendshop
 */
public interface LocationDao extends GenericDao<Location, Long> {

	/**
	 * 根据等级获取对应地区列表
	 *
	 * @param grade 等级
	 * @return List<LocationDTO> 地区列表
	 */
	List<Location> loadLocation(Integer grade);

	/**
	 * 获取地区下级区域列表
	 *
	 * @param parentId
	 * @return
	 */
	List<Location> getChildrenList(Long parentId);

	/**
	 * 根据地区名获取地区id
	 *
	 * @param name
	 * @param grade
	 * @return
	 */
	Long getIdByName(String name, Integer grade);

	/**
	 * 根据地区名获取地区id
	 *
	 * @param name
	 * @param grade
	 * @return
	 */
	Location getByName(String name, Integer grade);

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


	/**
	 * 根据地区级别分页
	 *
	 * @param grade
	 * @return
	 */
	PageSupport<Location> queryLocationByGrade(LocationQuery grade);

	/**
	 * 查询详细地址
	 *
	 * @param locationDetailDTO
	 * @return
	 */
	LocationDetailDTO getDetailAddress(LocationDetailDTO locationDetailDTO);

}
