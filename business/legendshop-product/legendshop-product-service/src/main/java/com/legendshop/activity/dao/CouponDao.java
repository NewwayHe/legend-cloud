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
import com.legendshop.activity.dto.ActivityCouponStatisticsDTO;
import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.dto.CouponItemExtDTO;
import com.legendshop.activity.entity.Coupon;
import com.legendshop.activity.query.ActivityCouponStatisticsQuery;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.activity.vo.CouponVO;

import java.util.List;

/**
 * 优惠券(Coupon)表数据库访问层
 *
 * @author legendshop
 * @since 2020-09-10 10:52:36
 */
public interface CouponDao extends GenericDao<Coupon, Long> {

	/**
	 * 更新单个商品状态的方法。
	 *
	 * @param id     商品 ID
	 * @param status 商品状态
	 * @return 更新操作影响的行数
	 */
	Integer updateStatus(Long id, Integer status);

	/**
	 * 批量更新商品状态的方法。
	 *
	 * @param ids    商品 ID 列表
	 * @param status 商品状态
	 * @return 更新操作影响的行数
	 */
	Integer batchUpdateStatus(List<Long> ids, Integer status);

	/**
	 * 根据优惠券 ID 列表获取优惠券列表。
	 *
	 * @param couponIds 优惠券 ID 列表
	 * @return 包含优惠券信息的列表
	 */
	List<Coupon> getByIds(List<Long> couponIds);

	/**
	 * 根据用户 ID 查询优惠券信息列表的方法。
	 *
	 * @param userId            用户 ID
	 * @param shopProviderFlag  商店提供标志
	 * @param status            优惠券状态
	 * @return 包含优惠券信息的列表
	 */
	List<CouponItemExtDTO> queryByUserId(Long userId, Boolean shopProviderFlag, Integer status);

	/**
	 * 分页查询优惠券信息的方法。
	 *
	 * @param couponQuery 优惠券查询对象
	 * @return 包含优惠券信息的分页支持对象
	 */
	PageSupport<CouponVO> queryCouponPage(CouponQuery couponQuery);


	/**
	 * 查询用户可领取优惠券列表
	 *
	 * @param couponQuery
	 * @return
	 */
	List<CouponDTO> listReceivable(CouponQuery couponQuery);

	/**
	 * 分页查询用户可领取优惠券
	 *
	 * @param couponQuery
	 * @return
	 */
	PageSupport<CouponDTO> queryReceivablePage(CouponQuery couponQuery);

	/**
	 * 查找要上线的优惠券
	 *
	 * @return
	 */
	List<Coupon> queryPreOnline();

	/**
	 * 更新发放数量
	 *
	 * @param couponId
	 */
	void updateReceiveNum(Long couponId);

	/**
	 * 获取到点下线的优惠券
	 *
	 * @return
	 */
	List<Coupon> getOffLineId();

	/**
	 * 获取优惠券，根据用户优惠券ID和店铺ID
	 *
	 * @param userCouponId
	 * @param shopId
	 * @return
	 */
	Coupon getCouponByUserCouponIdAndShopId(Long userCouponId, Long shopId);

	/**
	 * 分页查找符合体条件的优惠卷
	 *
	 * @param couponQuery
	 * @return
	 */
	PageSupport<CouponDTO> queryCoupon(CouponQuery couponQuery);


	/**
	 * 查找优惠卷下能够使用的商品
	 *
	 * @param couponId 优惠卷ID
	 * @return
	 */
	List<Long> queryCouponSkuIds(Long couponId);

	/**
	 * 查找优惠卷下能够使用的店铺
	 *
	 * @param couponId 优惠卷ID
	 * @return
	 */
	List<Long> queryCouponShopIds(Long couponId);


	/**
	 * 查询活动列表的商家优惠卷
	 *
	 * @param activityShopCouponQuery 领取时间
	 * @return
	 */
	PageSupport<ActivityCouponStatisticsDTO> pageActivityShopCoupon(ActivityCouponStatisticsQuery activityShopCouponQuery);

	/**
	 * 查询活动列表的平台优惠卷
	 *
	 * @param activityPlatformCouponQuery 领取时间
	 * @return
	 */
	PageSupport<ActivityCouponStatisticsDTO> pageActivityPlatformCoupon(ActivityCouponStatisticsQuery activityPlatformCouponQuery);

	/**
	 * 平台优惠卷导出
	 *
	 * @param query 领取时间
	 * @return
	 */
	List<ActivityCouponStatisticsDTO> getFlowExcelPlatform(ActivityCouponStatisticsQuery query);

	/**
	 * 商家优惠卷导出
	 *
	 * @param query 领取时间
	 * @return
	 */
	List<ActivityCouponStatisticsDTO> getFlowExcelShop(ActivityCouponStatisticsQuery query);

	/**
	 * 更新优惠券的使用数量
	 *
	 * @param id       优惠券ID
	 * @param useCount 使用数量
	 * @return
	 */
	Integer updateUseCount(Long id, Long useCount);
}
