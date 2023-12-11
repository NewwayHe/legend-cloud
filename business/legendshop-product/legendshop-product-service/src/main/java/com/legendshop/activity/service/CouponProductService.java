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
import com.legendshop.activity.dto.CouponProductDTO;
import com.legendshop.common.core.constant.R;
import com.legendshop.product.dto.ProductItemDTO;

import java.util.List;

/**
 * 优惠券商品.
 *
 * @author legendshop
 */
public interface CouponProductService {

	/**
	 * 根据指定的 ID 和优惠券 ID 删除数据的方法。
	 *
	 * @param id       指定的 ID
	 * @param couponId 优惠券 ID
	 * @return 删除操作影响的行数
	 */
	int delete(Long id, Long couponId);

	/**
	 * 更新优惠券商品信息的方法。
	 *
	 * @param couponProductDTO 包含要更新的优惠券商品信息的数据传输对象
	 * @return 更新操作影响的行数
	 */
	int updateCouponProduct(CouponProductDTO couponProductDTO);


	/**
	 * 根据id查询
	 *
	 * @param couponId
	 * @return
	 */
	List<CouponProductDTO> queryByCouponId(Long couponId);

	/**
	 * 根据id查询商品项
	 *
	 * @param couponId
	 * @return
	 */
	List<ProductItemDTO> queryInfoByCouponId(Long couponId);


	/**
	 * 更新用户优惠券状态的方法。
	 *
	 * @param ids    优惠券 ID 列表
	 * @param status 优惠券状态
	 * @return 更新操作影响的行数
	 */
	Integer updateStatus(List<Long> ids, Integer status);

	/**
	 * 更新用户优惠券为失效的方法。
	 *
	 * @param ids 优惠券 ID 列表
	 * @return 更新操作影响的行数
	 */
	Integer updateInvalidStatus(List<Long> ids);

	/**
	 * 更新用户优惠券状态的方法，需要指定旧的优惠券状态。
	 *
	 * @param ids       优惠券 ID 列表
	 * @param status    优惠券新状态
	 * @param oldStatus 优惠券旧状态
	 * @return 更新操作影响的行数
	 */
	Integer updateStatus(List<Long> ids, Integer status, Integer oldStatus);



	/**
	 * 更新优惠券商品
	 *
	 * @param couponDTO
	 * @return
	 */
	Long update(CouponDTO couponDTO);

	/**
	 * 拷贝优惠券
	 *
	 * @param couponId
	 * @return
	 */
	R copyCouponProduct(Long couponId);
}
