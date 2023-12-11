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
import com.legendshop.activity.entity.CouponProduct;
import com.legendshop.activity.query.CouponQuery;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.ProductItemDTO;

import java.util.List;

/**
 * 优惠券商品
 *
 * @author legendshop
 */
public interface CouponProductDao extends GenericDao<CouponProduct, Long> {

	/**
	 * 根据优惠券 ID 查询关联的商品列表。
	 *
	 * @param couponId 优惠券 ID
	 * @return 包含优惠券关联商品信息的列表
	 */
	List<CouponProduct> queryByCouponId(Long couponId);

	/**
	 * 分页查询优惠券关联商品信息的方法。
	 *
	 * @param couponQuery 优惠券查询对象
	 * @return 包含优惠券关联商品信息的分页支持对象
	 */
	PageSupport<SkuBO> queryCouponProductPage(CouponQuery couponQuery);

	/**
	 * 更新用户优惠券状态的方法。
	 *
	 * @param ids    优惠券 ID 列表
	 * @param status 优惠券状态
	 * @return 更新操作影响的行数
	 */
	Integer updateStatus(List<Long> ids, Integer status);

	/**
	 * 更新用户优惠券为失效状态
	 *
	 * @param ids
	 * @return
	 */
	Integer updateInvalidStatus(List<Long> ids);

	/**
	 * 更新用户优惠券状态的方法。
	 * @param ids       优惠券 ID 列表
	 * @param status    优惠券新状态
	 * @param oldStatus 优惠券旧状态
	 * @return 更新操作影响的行数
	 */
	Integer updateStatus(List<Long> ids, Integer status, Integer oldStatus);

	/**
	 * 根据优惠券 ID 查询相关商品信息的方法。
	 *
	 * @param couponId 优惠券 ID
	 * @return 包含相关商品信息的列表
	 */
	List<ProductItemDTO> queryInfoByCouponId(Long couponId);

	/**
	 * 根据优惠券 ID 和 SKU ID 列表查询所有的优惠券商品关联信息。
	 *
	 * @param couponId 优惠券 ID
	 * @param skuId    SKU ID 列表
	 * @return 包含优惠券商品关联信息的列表
	 */
	List<CouponProduct> queryAllBySkuIds(Long couponId, List<Long> skuId);

	/**
	 * 根据优惠券 ID 和未选中的 SKU ID 列表删除相关优惠券商品关联信息。
	 *
	 * @param couponId      优惠券 ID
	 * @param unSelectSkuId 未选中的 SKU ID 列表
	 * @return 删除操作影响的行数
	 */
	Integer deleteBySkuIds(Long couponId, List<Long> unSelectSkuId);

	/**
	 * 根据优惠券 ID 和 SKU ID 列表查询相关的优惠券 ID 列表。
	 *
	 * @param couponId 优惠券 ID
	 * @param skuIdList SKU ID 列表
	 * @return 相关的优惠券 ID 列表
	 */
	List<Long> queryByCouponIdAndSkuList(Long couponId, List<Long> skuIdList);

	/**
	 * 根据优惠券 ID 删除相关数据的方法。
	 *
	 * @param couponId 优惠券 ID
	 */
	void deleteByCouponId(Long couponId);

}
