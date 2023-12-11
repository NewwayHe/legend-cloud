/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.mq.producer;

import com.legendshop.activity.dto.CouponDTO;

import java.util.Date;
import java.util.List;

/**
 * 优惠券相关生产者
 *
 * @author legendshop
 */
public interface CouponProducerService {

	/**
	 * 【优惠券】未保存优惠券的优惠券商品超时自动删除
	 *
	 * @param couponId
	 */
	void deleteCouponProduct(Long couponId);

	/**
	 * 【优惠券】未保存优惠券的优惠券店铺超时自动删除
	 *
	 * @param couponId
	 */
	void deleteCouponShop(Long couponId);

	/**
	 * 【优惠券】发送延迟上线消息
	 *
	 * @param couponDTO
	 */
	void couponOnLine(CouponDTO couponDTO);

	/**
	 * 【优惠券】发送延迟下线消息
	 *
	 * @param id
	 * @param offTime
	 */
	void couponOffLine(Long id, Date offTime);

	/**
	 * 【用户优惠券】发送延迟上线消息
	 *
	 * @param ids
	 * @param onTime
	 */
	void userCouponOnLine(List<Long> ids, Date onTime);

	/**
	 * 【用户优惠券】发送延迟下线消息
	 *
	 * @param ids
	 * @param offTime
	 */
	void userCouponOffLine(List<Long> ids, Date offTime);
}
