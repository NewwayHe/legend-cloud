/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dao;

import cn.legendshop.jpaplus.GenericDao;
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.order.entity.LogisticsCompany;
import com.legendshop.order.query.LogisticsCompanyQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * 物流公司Dao.
 *
 * @author legendshop
 */
public interface LogisticsCompanyDao extends GenericDao<LogisticsCompany, Long> {

	LogisticsCompany getByTemplateId(Long templateId);

	void deleteByShopId(Long shopId);

	PageSupport<LogisticsCompany> queryPage(LogisticsCompanyQuery logisticsCompanyQuery);

	/**
	 * 获取店铺的物流公司
	 *
	 * @param shopId
	 * @return
	 */
	List<LogisticsCompany> getList(Long shopId);

	/**
	 * 采用次数+1
	 *
	 * @param id
	 * @return
	 */
	int addUseCount(Long id);

	/**
	 * 采用次数-1
	 *
	 * @param id
	 * @return
	 */
	int subUseCount(Long id);

	/**
	 * 获取商家全部物流公司
	 *
	 * @param shopId
	 * @return
	 */
	List<LogisticsCompany> queryAllByShopId(Long shopId);

	/**
	 * 根据订单id获取物流公司
	 *
	 * @param shopId
	 * @return
	 */
	LogisticsCompany getByShopId(Long logisticsId, Long shopId);

	List<LogisticsCompany> queryByNameList(ArrayList<String> logisticsCompanyList);
}
