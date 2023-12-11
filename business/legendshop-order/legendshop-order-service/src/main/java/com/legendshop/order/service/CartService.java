/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service;

import com.legendshop.common.core.constant.R;
import com.legendshop.order.dto.CartItemDTO;
import com.legendshop.order.dto.SubmitOrderSkuDTO;
import com.legendshop.order.vo.ShopCartViewVO;

import java.util.List;

/**
 * 购物车服务
 *
 * @author legendshop
 */
public interface CartService {


	ShopCartViewVO buildShopCartVO(Long userId, Long addressId, List<CartItemDTO> shoppingCartItems);

	List<CartItemDTO> queryInvalidProductList(Long userId);

	void cleanInvalidProduct(Long userId);

	/**
	 * 清除购物车
	 *
	 * @param skuList
	 */
	void batchClean(Long userId, List<SubmitOrderSkuDTO> skuList);

	/**
	 * 购物车合并
	 *
	 * @param userId  登录的用户
	 * @param userKey 未登录的userKey
	 * @return
	 */
	R<Void> mergeCart(Long userId, String userKey);
}
