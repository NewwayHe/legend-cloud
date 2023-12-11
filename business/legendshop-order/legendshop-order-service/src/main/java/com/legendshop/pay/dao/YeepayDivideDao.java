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
import com.legendshop.pay.entity.YeepayDivide;
import com.legendshop.pay.enums.YeepayDivideTypeEnum;

/**
 * 易宝支付分账信息(YeepayDivide)表数据库访问层
 *
 * @author legendshop
 * @since 2021-03-31 20:51:36
 */
public interface YeepayDivideDao extends GenericDao<YeepayDivide, Long> {

	/**
	 * 根据订单ID获取分账信息
	 *
	 * @param orderId
	 * @param typeEnum
	 * @return
	 */
	YeepayDivide getByOrderId(Long orderId, Long yeepayOrderId, YeepayDivideTypeEnum typeEnum);

	/**
	 * 保存分账信息
	 *
	 * @param yeepayDivide
	 */
	void saveDivide(YeepayDivide yeepayDivide);
}
