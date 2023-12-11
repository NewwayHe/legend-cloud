/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.service;

import com.legendshop.activity.dto.CouponDTO;
import com.legendshop.activity.dto.CouponShopDTO;
import com.legendshop.common.core.constant.R;

import java.util.List;

/**
 * 平台优惠劵(指定商家可用类型)  店铺关联表管理
 *
 * @author legendshop
 */
public interface CouponShopService {

	/**
	 * 更新平台优惠劵的店铺关联
	 *
	 * @param couponDTO
	 * @return
	 */
	R update(CouponDTO couponDTO);

	/**
	 * 删除平台优惠劵的商家关联
	 *
	 * @param couponShopId
	 * @param couponId
	 * @return
	 */
	Integer delete(Long couponShopId, Long couponId);

	/**
	 * 拷贝平台优惠劵的商家关联
	 *
	 * @param oldCouponId
	 * @return
	 */
	R copyCouponShop(Long oldCouponId);

	/**
	 * 获取平台优惠劵的商家列表
	 *
	 * @param couponId
	 * @return
	 */
	List<CouponShopDTO> getCouponShopByCouponId(Long couponId);

	/**
	 * 根据优惠券 ID 查询关联的店铺 ID 列表。
	 *
	 * @param couponId 优惠券 ID
	 * @return 关联的店铺 ID 列表
	 */
	List<Long> queryShopByCouponId(Long couponId);

	/**
	 * 批量保存优惠券店铺关联信息。
	 *
	 * @param couponShops 优惠券店铺关联信息列表
	 * @return 保存操作影响的行数
	 */
	List<Long> saveCouponShops(List<CouponShopDTO> couponShops);

	/**
	 * 根据优惠券 ID 获取关联店铺的名称列表。
	 *
	 * @param id 优惠券 ID
	 * @return 关联店铺的名称列表
	 */
	List<String> getShopNamesByCouponId(Long id);

	/**
	 * 根据优惠券 ID 列表获取优惠券店铺关联信息列表。
	 *
	 * @param couponIds 优惠券 ID 列表
	 * @return 包含优惠券店铺关联信息的列表
	 */
	List<CouponShopDTO> getCouponShopByCouponIds(List<Long> couponIds);

}
