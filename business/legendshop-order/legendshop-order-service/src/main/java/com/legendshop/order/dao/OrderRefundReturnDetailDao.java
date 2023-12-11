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
import com.legendshop.order.entity.OrderRefundReturnDetail;

import java.util.List;

/**
 * 退款明细Dao
 *
 * @author legendshop
 */
public interface OrderRefundReturnDetailDao extends GenericDao<OrderRefundReturnDetail, Long> {

	/**
	 * 根据refundId查询列表
	 *
	 * @param refundId
	 * @return
	 */
	List<OrderRefundReturnDetail> queryByRefundId(Long refundId);

}
