/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.pay.entity.PaySettlementOrder;

import java.util.List;

/**
 * 支付单据关联dao
 *
 * @author legendshop
 */
public interface PaySettlementOrderDao extends GenericDao<PaySettlementOrder, Long> {


	List<PaySettlementOrder> queryOrderBySn(String settlementSn);

	List<PaySettlementOrder> querySnByOrderNumber(String orderNumber);

	List<PaySettlementOrder> querySnByOrderNumber(List<String> orderNumber);
}
