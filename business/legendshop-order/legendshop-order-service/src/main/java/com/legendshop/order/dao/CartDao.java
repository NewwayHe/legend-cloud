/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dao;

import cn.legendshop.jpaplus.GenericDao;
import com.legendshop.order.dto.CartItemDTO;
import com.legendshop.order.entity.Cart;

import java.util.List;

/**
 * 购物车dao
 *
 * @author legendshop
 */
public interface CartDao extends GenericDao<Cart, Long> {

	/**
	 * 根据用户ID获取购物车列表
	 * @param userId
	 * @return
	 */

	/**
	 * 根据用户id查询BO
	 *
	 * @param userId
	 * @return
	 */
	List<CartItemDTO> queryByUserId(Long userId);

	/**
	 * 根据用户ID获取购物车列表
	 * @param userId
	 * @return
	 */

	/**
	 * 根据用户id查询BO
	 *
	 * @param userId
	 * @return
	 */
	List<CartItemDTO> queryByUserId(Long userId, List<Long> cartIds);

	/**
	 * 根据ids删除
	 *
	 * @param userId
	 * @param cartIds
	 */
	void delete(Long userId, List<Long> cartIds);

	/**
	 * 删除所有
	 *
	 * @param userId
	 */
	void deleteAll(Long userId);

	/**
	 * 获取失效商品集合
	 *
	 * @param userId
	 * @return
	 */
	List<CartItemDTO> queryInvalidProductList(Long userId);

	/**
	 * 清空失效商品
	 *
	 * @param userId
	 */
	void cleanInvalidProduct(Long userId);


	/**
	 * 批量清除购物车
	 *
	 * @param userId
	 * @param skuIds
	 */
	void batchClean(Long userId, List<Long> skuIds);
}
