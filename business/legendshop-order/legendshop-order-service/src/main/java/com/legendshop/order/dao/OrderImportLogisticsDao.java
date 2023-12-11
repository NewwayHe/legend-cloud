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
import com.legendshop.order.dto.OrderImportLogisticsDTO;
import com.legendshop.order.entity.OrderImportLogistics;
import com.legendshop.order.query.OrderImportLogisticsQuery;

/**
 * (OrderImportLogistics)表数据库访问层
 *
 * @author legendshop
 * @since 2022-04-25 14:09:00
 */
public interface OrderImportLogisticsDao extends GenericDao<OrderImportLogistics, Long> {

	/**
	 * 商家端的订单列表中支持批量操作分页查询
	 *
	 * @return
	 */
	PageSupport<OrderImportLogisticsDTO> page(OrderImportLogisticsQuery query);
}
