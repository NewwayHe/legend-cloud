/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dao.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.PageSupport;
import cn.legendshop.jpaplus.support.lambda.LambdaCriteriaQuery;
import com.legendshop.order.dao.OrderImportLogisticsDao;
import com.legendshop.order.dto.OrderImportLogisticsDTO;
import com.legendshop.order.entity.OrderImportLogistics;
import com.legendshop.order.query.OrderImportLogisticsQuery;
import org.springframework.stereotype.Repository;

/**
 * (OrderImportLogistics)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2022-04-25 14:10:35
 */
@Repository
public class OrderImportLogisticsDaoImpl extends GenericDaoImpl<OrderImportLogistics, Long> implements OrderImportLogisticsDao {

	@Override
	public PageSupport<OrderImportLogisticsDTO> page(OrderImportLogisticsQuery query) {
		LambdaCriteriaQuery<OrderImportLogisticsDTO> lq = new LambdaCriteriaQuery<>(OrderImportLogisticsDTO.class, query.getPageSize(), query.getCurPage());
		lq.eq(OrderImportLogisticsDTO::getShopId, query.getShopId());
		if (ObjectUtil.isNotEmpty(query.getStartTime())) {
			lq.ge(OrderImportLogisticsDTO::getCreateTime, DateUtil.beginOfDay(query.getStartTime()));
		}
		if (ObjectUtil.isNotEmpty(query.getEndTime())) {
			lq.le(OrderImportLogisticsDTO::getCreateTime, DateUtil.endOfDay(query.getEndTime()));
		}
		lq.addDescOrder(OrderImportLogisticsDTO::getCreateTime);
		return queryDTOPage(lq);
	}
}
