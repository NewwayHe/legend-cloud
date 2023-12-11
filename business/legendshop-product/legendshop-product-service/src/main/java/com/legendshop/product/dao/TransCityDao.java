/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.product.entity.TransCity;
import com.legendshop.product.enums.TransCityTypeEnum;

import java.util.List;

/**
 * 每个城市的运费设置(TransCity)表数据库访问层
 *
 * @author legendshop
 * @since 2020-09-04 15:13:47
 */
public interface TransCityDao extends GenericDao<TransCity, Long> {


	/**
	 * 批量添加
	 *
	 * @param transCities
	 */
	void batchAdd(List<TransCity> transCities);

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
	List<TransCity> getCityList(Long transId, Long parentId);

	/**
	 * 获取城市列表
	 *
	 * @param transId
	 * @param parentIds
	 * @return
	 */
	List<TransCity> getCityList(Long transId, List<Long> parentIds);

	/**
	 * 获取城市列表
	 *
	 * @param transIds
	 * @param parentId
	 * @return
	 */
	List<TransCity> getCityList(List<Long> transIds, Long parentId);

	/**
	 * 根据模板id和类型获取
	 *
	 * @param transId
	 * @param cityId
	 * @param transCityTypeEnum
	 * @return
	 */
	TransCity getByTransIdAndType(Long transId, Long cityId, TransCityTypeEnum transCityTypeEnum);
}
