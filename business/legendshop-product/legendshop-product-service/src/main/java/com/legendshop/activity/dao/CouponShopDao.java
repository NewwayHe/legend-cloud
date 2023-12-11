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
import com.legendshop.activity.bo.CouponShopBO;
import com.legendshop.activity.entity.CouponShop;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.activity.query.CouponShopQuery;

import java.util.List;

/**
 * @author legendshop
 */
public interface CouponShopDao extends GenericDao<CouponShop, Long> {

	/**
	 * 根据优惠券 ID 列表获取优惠券店铺关联信息列表。
	 *
	 * @param couponIds 优惠券 ID 列表
	 * @return 包含优惠券店铺关联信息的列表
	 */
	List<CouponShop> getCouponShopByCouponIds(List<Long> couponIds);

	/**
	 * 根据优惠券 ID 获取优惠券店铺关联信息列表。
	 *
	 * @param couponId 优惠券 ID
	 * @return 包含优惠券店铺关联信息的列表
	 */
	List<CouponShop> getCouponShopByCouponId(Long couponId);

	/**
	 * 根据优惠券 ID 删除优惠券店铺关联信息。
	 *
	 * @param couponId 优惠券 ID
	 */
	void deleteByCouponId(Long couponId);

	/**
	 * 根据店铺 ID 和优惠券 ID 删除优惠券店铺关联信息。
	 *
	 * @param couponShopId 优惠券店铺关联 ID
	 * @param couponId     优惠券 ID
	 * @return 删除操作影响的行数
	 */
	Integer deleteByShopIdAndCouponId(Long couponShopId, Long couponId);

	/**
	 * 根据优惠券 ID 查询关联的店铺 ID 列表。
	 *
	 * @param couponId 优惠券 ID
	 * @return 关联的店铺 ID 列表
	 */
	List<Long> queryShopByCouponId(Long couponId);

	/**
	 * 分页查询优惠券店铺关联信息的方法。
	 *
	 * @param couponQuery 优惠券查询对象
	 * @return 包含优惠券店铺关联信息的分页对象
	 */
	PageSupport<CouponShopBO> queryCouponShopPage(CouponQuery couponQuery);

	/**
	 * 分页查询优惠券店铺关联信息的方法。
	 *
	 * @param couponQuery 优惠券店铺查询对象
	 * @return 包含优惠券店铺关联信息的分页对象
	 */
	PageSupport<CouponShopBO> queryCouponShopPage(CouponShopQuery couponQuery);

}
