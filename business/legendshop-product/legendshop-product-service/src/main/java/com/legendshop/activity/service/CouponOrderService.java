/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.activity.dto.CouponOrderDTO;
import com.legendshop.activity.entity.CouponOrder;
import com.legendshop.common.core.constant.R;

import java.util.List;

/**
 * (CouponOrder)表服务接口
 *
 * @author legendshop
 * @since 2022-03-25 10:45:35
 */
public interface CouponOrderService extends GenericDao<CouponOrder, Long> {

	/**
	 * 保存优惠券订单信息的方法。
	 *
	 * @param couponOrderList 优惠券订单信息列表
	 * @return 保存优惠券订单操作的结果
	 */
	R<Void> saveCoupon(List<CouponOrderDTO> couponOrderList);


}
