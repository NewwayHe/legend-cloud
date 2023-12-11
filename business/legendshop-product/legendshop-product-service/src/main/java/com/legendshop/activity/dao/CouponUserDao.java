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
import cn.legendshop.jpaplus.support.PageSupport;
import com.legendshop.activity.bo.CouponUserBO;
import com.legendshop.activity.bo.MyCouponBO;
import com.legendshop.activity.dto.CouponUserDTO;
import com.legendshop.activity.entity.CouponUser;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.activity.query.CouponUserQuery;
import com.legendshop.product.bo.SkuBO;

import java.util.Date;
import java.util.List;

/**
 * 用户优惠券Dao.
 *
 * @author legendshop
 */
public interface CouponUserDao extends GenericDao<CouponUser, Long> {
	/**
	 * 根据用户 ID 查询优惠券用户信息列表的方法。
	 *
	 * @param userId 用户 ID
	 * @return 包含优惠券用户信息的列表
	 */
	List<CouponUser> queryByUserId(Long userId);

	/**
	 * 根据用户id查询用户已领取的优惠券
	 *
	 * @param couponQuery
	 * @return
	 */
	PageSupport<MyCouponBO> queryMyCouponPage(CouponQuery couponQuery);

	/**
	 * 查询所有已领取优惠券的用户并进行分页处理。
	 *
	 * @param couponQuery 优惠券查询条件对象
	 * @return 包含已领取优惠券用户信息的分页对象
	 */
	PageSupport<CouponUserBO> queryPage(CouponQuery couponQuery);

	/**
	 * 用户领券
	 *
	 * @param id
	 * @param userId
	 * @param onTime
	 * @param offTime
	 * @return
	 */
	Integer updateUserIdByPwd(Long id, Long userId, Date onTime, Date offTime);

	/**
	 * 查询优惠券关联的商品列表
	 *
	 * @param couponUserQuery
	 * @return
	 */
	PageSupport<SkuBO> querySkuPageById(CouponUserQuery couponUserQuery);

	/**
	 * 获取用户未使用的优惠券
	 *
	 * @param userId
	 * @return
	 */
	List<CouponUserDTO> queryUnusedCouponIdByUserId(Long userId);

	/**
	 * 更新用户优惠券为未使用
	 */
	Integer userCouponValid();

	/**
	 * 更新用户优惠券为已过期
	 */
	Integer userCouponInvalid();

	/**
	 * 通过卡密查找优惠券
	 *
	 * @return
	 */
	CouponUser getByPwd(String pwd);

	/**
	 * 根据用户id，优惠券id查询用户优惠券
	 *
	 * @param userId
	 * @param couponId
	 * @return
	 */
	List<CouponUser> queryByUserIdAndCouponId(Long userId, Long couponId);

	/**
	 * 更新用户用户券状态
	 *
	 * @param id
	 * @param status
	 * @return
	 */
	Integer updateStatus(Long id, Integer status);

	/**
	 * 更新用户用户券状态
	 *
	 * @param couponId
	 * @return
	 */
	Integer updateUserCouponStatus(Long couponId);

	/**
	 * 通过订单编号获取用户优惠劵
	 *
	 * @param orderNumber
	 * @return
	 */
	List<CouponUser> getByOrderNumber(String orderNumber);

	Integer userCouponCount(Long userId);

	void batchUpdateStatus(Long userId, List<Long> ids, String orderNumber, Integer status);


	/**
	 * 获取可用代金券数量
	 *
	 * @param userId
	 * @return
	 */
	Integer getAvailableCount(Long userId);

	/**
	 * 获取已使用代金券数量
	 *
	 * @param userId
	 * @return
	 */
	Integer getUsedCount(Long userId);

	/**
	 * 获取已使用代金券数量
	 *
	 * @param userId
	 * @return
	 */
	Integer getExpireCount(Long userId);

	/**
	 * 更新优惠券使用状态
	 *
	 * @param couponUserId
	 * @param value
	 */
	void batchUpdateStatusById(List<Long> couponUserId, Integer value);


}
