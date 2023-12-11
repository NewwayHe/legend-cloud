/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service;

import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.order.dto.LogisticsCompanyDTO;
import com.legendshop.order.query.LogisticsCompanyQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * 物流公司.
 *
 * @author legendshop
 */
public interface LogisticsCompanyService {

	LogisticsCompanyDTO getById(Long id);

	void deleteById(Long id);

	Long save(LogisticsCompanyDTO logisticsCompanyDTO);

	void update(LogisticsCompanyDTO logisticsCompanyDTO);


	PageSupport<LogisticsCompanyDTO> queryPage(LogisticsCompanyQuery logisticsCompanyQuery);

	/**
	 * 获取物流公司列表
	 */
	List<LogisticsCompanyDTO> getLogisticsCompany();


	/**
	 * 商家端批量添加物流公司
	 *
	 * @param list
	 * @param shopId
	 */
	void batchAddLogisticsCompanyDTO(List<LogisticsCompanyDTO> list, Long shopId);

	/**
	 * 获取店铺的物流公司
	 *
	 * @param shopId
	 * @return
	 */
	List<LogisticsCompanyDTO> getList(Long shopId);

	/**
	 * 获取商家全部物流公司列表
	 *
	 * @param shopId
	 * @return
	 */
	List<LogisticsCompanyDTO> queryAll(Long shopId);

	/**
	 * 获取物流公司
	 *
	 * @param logisticsCompanyList
	 * @return
	 */
	List<LogisticsCompanyDTO> queryByNameList(ArrayList<String> logisticsCompanyList);
}
