/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.impl.handler;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.legendshop.common.core.constant.RequestHeaderConstant;
import com.legendshop.order.dto.CartItemDTO;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.legendshop.common.core.constant.CacheConstants.CART_ITEMS_TMP;

/**
 * 未登录用户的购物车处理
 *
 * @author legendshop
 */
@Component("un_login_cart")
public class UnLoginCartHandlerImpl extends AbstractCartHandler {

	@Override
	public List<CartItemDTO> queryCartItems(Object userId) {
		if (ObjectUtil.isNull(userId)) {
			userId = request.getHeader(RequestHeaderConstant.USER_KEY);
		}
		return cacheManagerUtil.getCache(CART_ITEMS_TMP, userId);
	}

	@Override
	public void save(Object userId, CartItemDTO cartItemDTO) {
		if (ObjectUtil.isNull(userId)) {
			userId = request.getHeader(RequestHeaderConstant.USER_KEY);
		}
		cartItemDTO.setSelectFlag(false);
		saveOrUpdateCache(userId.toString(), cartItemDTO);
	}

	@Override
	public void update(Object userId, CartItemDTO cartItemDTO) {
		if (ObjectUtil.isNull(userId)) {
			userId = request.getHeader(RequestHeaderConstant.USER_KEY);
		}
		saveOrUpdateCache(userId.toString(), cartItemDTO);
	}

	@Override
	public void update(Object userId, List<CartItemDTO> cartItemList) {
		if (ObjectUtil.isNull(userId)) {
			userId = request.getHeader(RequestHeaderConstant.USER_KEY);
		}
		Object finalUserId = userId;
		cartItemList.forEach(cartItemDTO -> update(finalUserId, cartItemDTO));
	}

	@Override
	public void delete(Object userId, List<Long> cartIds) {
		if (ObjectUtil.isNull(userId)) {
			userId = request.getHeader(RequestHeaderConstant.USER_KEY);
		}
		List<CartItemDTO> cartItemList = cacheManagerUtil.getCache(CART_ITEMS_TMP, userId);
		List<CartItemDTO> changeCartItem = cartItemList
				.stream()
				.filter(cartItemDTO -> cartIds.stream().anyMatch(id -> !cartItemDTO.getId().equals(id)))
				.collect(Collectors.toList());
		cacheManagerUtil.putCache(CART_ITEMS_TMP, userId, changeCartItem);
	}

	@Override
	public void deleteAll(Object userId) {
		if (ObjectUtil.isNull(userId)) {
			userId = request.getHeader(RequestHeaderConstant.USER_KEY);
		}
		cacheManagerUtil.evictCache(CART_ITEMS_TMP, userId);
	}

	/**
	 * 更新未登录用户的缓存
	 *
	 * @param userId
	 * @param itemDTO
	 */
	private void saveOrUpdateCache(String userId, CartItemDTO itemDTO) {
		if (ObjectUtil.isNull(userId)) {
			userId = request.getHeader(RequestHeaderConstant.USER_KEY);
		}
		List<CartItemDTO> cartItemList = cacheManagerUtil.getCache(CART_ITEMS_TMP, userId);
		if (CollUtil.isEmpty(cartItemList)) {
			cacheManagerUtil.putCache(CART_ITEMS_TMP, userId, Arrays.asList(itemDTO));
			//保存
		} else {
			//合并
			CartItemDTO cartItem = cartItemList.stream().filter(item -> item.getSkuId().equals(itemDTO.getSkuId())).findFirst().orElse(null);
			if (ObjectUtil.isNotNull(cartItem)) {
				cartItem.setTotalCount(itemDTO.getTotalCount());
			} else {
				cartItemList.add(itemDTO);
			}
			cacheManagerUtil.putCache(CART_ITEMS_TMP, userId, cartItemList);
		}
	}
}
