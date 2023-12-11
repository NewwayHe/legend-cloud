/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dao.impl;

import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.EntityCriterion;
import com.legendshop.order.dao.OrderImportLogisticsDetailDao;
import com.legendshop.order.entity.OrderImportLogisticsDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (OrderImportLogisticsDetail)表数据库访问层实现类
 *
 * @author legendshop
 * @since 2022-04-25 14:10:37
 */
@Repository
public class OrderImportLogisticsDetailDaoImpl extends GenericDaoImpl<OrderImportLogisticsDetail, Long> implements OrderImportLogisticsDetailDao {

	@Override
	public List<OrderImportLogisticsDetail> findByImportId(Long importId) {
		return super.queryByProperties(new EntityCriterion().eq("importId", importId));
	}
}
