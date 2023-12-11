/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service;


import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.activity.bo.CouponUserBO;
import com.legendshop.activity.bo.MyCouponBO;
import com.legendshop.activity.bo.MyCouponCountBO;
import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.dto.CouponUserDTO;
import com.legendshop.activity.dto.OrderRefundCouponDTO;
import com.legendshop.activity.entity.CouponUser;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.activity.query.CouponUserQuery;
import com.legendshop.common.core.constant.R;
import com.legendshop.data.dto.CouponViewDTO;
import com.legendshop.product.bo.SkuBO;

import java.util.List;

/**
 * 用户优惠券服务
 *
 * @author legendshop
 */
public interface CouponUserService {

	/**
	 * 查询所有已领取优惠券的用户
	 *
	 * @param couponQuery
	 * @return
	 */
	PageSupport<CouponUserBO> queryPage(CouponQuery couponQuery);

	/**
	 * 分页查询用户所有优惠券列表
	 *
	 * @param couponQuery
	 * @return
	 */
	PageSupport<MyCouponBO> queryMyCouponPage(CouponQuery couponQuery);

	/**
	 * 用户领取优惠券
	 *
	 * @param couponId
	 * @param userId
	 */
	R save(Long couponId, Long userId);

	/**
	 * 用户卡密领券
	 *
	 * @param pwd
	 * @param userId
	 * @return
	 */
	R updateUserIdByPwd(String pwd, Long userId);

	/**
	 * 查询优惠券关联的商品列表
	 *
	 * @param couponUserQuery
	 * @return
	 */
	PageSupport<SkuBO> querySkuPageById(CouponUserQuery couponUserQuery);

	/**
	 * 更新用户优惠券为未使用
	 */
	Integer userCouponValid();

	/**
	 * 更新用户优惠券为已过期
	 */
	Integer userCouponInvalid();

	/**
	 * 更新用户用户券状态
	 *
	 * @param id
	 * @param status
	 * @param shopId
	 * @return
	 */
	R updateStatus(Long id, Integer status, Long shopId);

	/**
	 * 获取用户优惠卷数量
	 */
	R<Integer> userCouponCount(Long userId);

	/**
	 * 获取用户优惠券
	 *
	 * @param orderNumber
	 * @return
	 */
	List<CouponUserDTO> getByOrderNumber(String orderNumber);

	void update(List<CouponUserDTO> couponUsers);

	R<CouponDTO> getById(Long id, Long userId);

	List<CouponUserDTO> queryById(List<Long> ids);

	/**
	 * 保存用户优惠券
	 *
	 * @param couponUserList
	 * @return
	 */
	List<Long> save(List<CouponUser> couponUserList);

	/**
	 * 获取用户优惠券数量
	 *
	 * @param userId
	 * @return
	 */
	R<MyCouponCountBO> getUserCouponCount(Long userId);

	void updateVisit(CouponViewDTO couponViewDTO);

	void updateCoupon(List<OrderRefundCouponDTO> couponUsers);

	void updateCouponOrder(List<OrderRefundCouponDTO> couponUsers);

	/**
	 * 用户优惠券到点上线 定时任务处理
	 *
	 * @return
	 */
	R<Void> userCouponValidJobHandle();

	/**
	 * 用户优惠券到点下线 定时任务处理
	 */
	R<Void> userCouponInvalidJobHandle();
}
