/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.activity.entity.CouponOrder;

import java.util.List;

/**
 * (CouponOrder)表数据库访问层
 *
 * @author legendshop
 * @since 2022-03-25 10:45:35
 */
public interface CouponOrderDao extends GenericDao<CouponOrder, Long> {

	/**
	 * 根据用户优惠券ID查询
	 *
	 * @param userCouponIds
	 * @return
	 */
	List<CouponOrder> queryByUserCouponId(List<Long> userCouponIds);

}
