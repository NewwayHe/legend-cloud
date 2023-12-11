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
import com.legendshop.order.dto.ShopBillPeriodDTO;
import com.legendshop.order.excel.ShopBillPeriodExportDTO;
import com.legendshop.order.query.ShopBillPeriodQuery;

import java.util.List;

/**
 * 结算档期服务
 *
 * @author legendshop
 */
public interface ShopBillPeriodService {

	ShopBillPeriodDTO getShopBillPeriod(Long id);

	int deleteShopBillPeriod(Long id);

	Long saveShopBillPeriod(ShopBillPeriodDTO shopBillPeriodDto);

	/**
	 * 保存
	 *
	 * @param shopBillPeriodDto
	 * @param id
	 * @return
	 */
	long saveShopBillPeriodWithId(ShopBillPeriodDTO shopBillPeriodDto, Long id);

	int updateShopBillPeriod(ShopBillPeriodDTO shopBillPeriodDto);

	/**
	 * 生成一个id
	 *
	 * @return
	 */
	Long getShopBillPeriodId();

	/**
	 * 获取当前期结算的月份 例如：201512.
	 *
	 * @return the shop bill month
	 */
	String getShopBillMonth();

	/**
	 * 查询列表
	 *
	 * @param shopBillPeriodQuery
	 * @return
	 */
	PageSupport<ShopBillPeriodDTO> queryShopBillPeriod(ShopBillPeriodQuery shopBillPeriodQuery);

	/**
	 * 结算档期列表导出
	 *
	 * @param shopBillPeriodQuery
	 * @return
	 */
	List<ShopBillPeriodExportDTO> exportShopBillPeriod(ShopBillPeriodQuery shopBillPeriodQuery);
}
