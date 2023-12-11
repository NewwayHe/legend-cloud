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
import com.legendshop.order.dao.OrderRefundReturnDetailDao;
import com.legendshop.order.entity.OrderRefundReturnDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 退款明细Dao
 *
 * @author legendshop
 */
@Repository
public class OrderRefundReturnDetailDaoImpl extends GenericDaoImpl<OrderRefundReturnDetail, Long> implements OrderRefundReturnDetailDao {
	@Override
	public List<OrderRefundReturnDetail> queryByRefundId(Long refundId) {
		return queryByProperties(new EntityCriterion().eq("refundId", refundId));
	}

}
