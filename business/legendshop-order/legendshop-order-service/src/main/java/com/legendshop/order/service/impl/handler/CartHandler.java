/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.impl.handler;

import com.legendshop.order.dto.CartItemDTO;

import java.util.List;

/**
 * 购物车处理类
 *
 * @author legendshop
 */
public interface CartHandler {

	/**
	 * 查询购物车
	 *
	 * @param userId
	 * @return
	 */
	List<CartItemDTO> queryCartItems(Object userId);

	/**
	 * 保存购物车
	 *
	 * @param userId
	 * @param cartItemDTO
	 */
	void save(Object userId, CartItemDTO cartItemDTO);

	/**
	 * 修改购物车
	 *
	 * @param userId
	 * @param cartItemDTO
	 */
	void update(Object userId, CartItemDTO cartItemDTO);

	/**
	 * 修改购物车
	 *
	 * @param userId
	 * @param cartItemList
	 */
	void update(Object userId, List<CartItemDTO> cartItemList);

	/**
	 * 删除购物车
	 *
	 * @param userId
	 * @param cartIds
	 */
	void delete(Object userId, List<Long> cartIds);


	/**
	 * 删除所有
	 */
	void deleteAll(Object userId);
}
