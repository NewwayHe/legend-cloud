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
import com.legendshop.order.entity.OrderImportLogisticsDetail;

import java.util.List;

/**
 * (OrderImportLogisticsDetail)表数据库访问层
 *
 * @author legendshop
 * @since 2022-04-25 14:09:01
 */
public interface OrderImportLogisticsDetailDao extends GenericDao<OrderImportLogisticsDetail, Long> {

	List<OrderImportLogisticsDetail> findByImportId(Long importId);
}
