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
import com.legendshop.pay.entity.PayRefundSettlement;

import java.util.List;

/**
 * (PayRefundSettlement)表数据库访问层
 *
 * @author legendshop
 * @since 2021-05-12 18:09:16
 */
public interface PayRefundSettlementDao extends GenericDao<PayRefundSettlement, Long> {


	List<PayRefundSettlement> queryByRefundSn(String refundSn);

	PayRefundSettlement queryByRefundSnAndType(String refundSn, String refundType);

	PayRefundSettlement getByExternalRefundSn(String externalRefundSn);

	PayRefundSettlement getByPayRefundSn(String payRefundSn);
}
