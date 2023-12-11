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
import com.legendshop.pay.entity.YeepayOrder;

/**
 * 易宝支付订单信息(YeepayOrder)表数据库访问层
 *
 * @author legendshop
 * @since 2021-03-26 15:59:27
 */
public interface YeepayOrderDao extends GenericDao<YeepayOrder, Long> {

	YeepayOrder getByOrderNumber(String orderNumber);

	YeepayOrder getByPaySettlementSn(String paySettlementSn);
}
