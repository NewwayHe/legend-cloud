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
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.legendshop.common.core.constant.CacheConstants;
import com.legendshop.common.core.constant.RequestHeaderConstant;
import com.legendshop.order.dao.CartDao;
import com.legendshop.order.dto.CartChangePromotionDTO;
import com.legendshop.order.dto.CartItemDTO;
import com.legendshop.order.entity.Cart;
import com.legendshop.order.service.convert.CartConverter;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.legendshop.common.core.constant.CacheConstants.CART_ITEMS;
import static com.legendshop.common.core.constant.CacheConstants.CART_ITEMS_TMP;

/**
 * 登录用户购物车的实现
 *
 * @author legendshop
 */
@Component("login_cart")
@AllArgsConstructor
public class LoginCartHandlerImpl extends AbstractCartHandler {

	private final CartDao cartDao;
	private final CartConverter cartConverter;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<CartItemDTO> queryCartItems(Object userId) {
		Long id = Long.parseLong(userId.toString());

		//有合并购物车的动作
		String cookieKey = request.getHeader(RequestHeaderConstant.USER_KEY);

		List<CartItemDTO> cartItemList = cacheManagerUtil.getCache(CART_ITEMS_TMP, cookieKey);
		if (CollUtil.isEmpty(cartItemList)) {
			cartItemList = cartDao.queryByUserId(id);
		} else {
			// 将用户ID放入
			cartItemList.forEach(e -> e.setUserId(id));
			//数据库查询
			List<CartItemDTO> dbCartItemList = cartDao.queryByUserId(id);
			cartItemList = merge(dbCartItemList, cartItemList);
			cacheManagerUtil.evictCache(CART_ITEMS_TMP, cookieKey);
		}
		return cartItemList;
	}

	/**
	 * 合并cooKie购物车项目
	 *
	 * @param dbCartItemList
	 * @param cooKieCartItemList
	 * @return
	 */
	private List<CartItemDTO> merge(List<CartItemDTO> dbCartItemList, List<CartItemDTO> cooKieCartItemList) {
		Map<Long, CartItemDTO> map = new HashMap<>(dbCartItemList.size() + cooKieCartItemList.size());
		// 将dbCartItemList元素放入Map，以skuId为key
		for (CartItemDTO db : dbCartItemList) {
			map.put(db.getSkuId(), db);
		}
		// 循环cooKieCartItemList，存在则修改，不存在则添加
		for (CartItemDTO cooKie : cooKieCartItemList) {
			CartItemDTO db = map.get(cooKie.getSkuId());
			//新增数据库
			if (ObjectUtil.isNull(db)) {
				map.put(cooKie.getSkuId(), cooKie);
				//保存数据库
				cooKie.setSelectFlag(Boolean.FALSE);
				cartDao.save(cartConverter.convertShoppingCart(cooKie));
			} else {
				//如果有一方为选中状态，则都为选中
				if (db.getSelectFlag() || cooKie.getSelectFlag()) {
					db.setSelectFlag(Boolean.TRUE);
				}
				db.setTotalCount(db.getTotalCount() + cooKie.getTotalCount());
				//修改数据库
				cartDao.update(cartConverter.convertShoppingCart(db));
			}
		}
		// 返回map的集合
		return new ArrayList(map.values());
	}

	@Override
	@CacheEvict(cacheNames = CART_ITEMS, key = "#userId")
	public void save(Object userId, CartItemDTO itemDTO) {
		Cart cart = new Cart();
		cart.setProductId(itemDTO.getProductId());
		cart.setShopId(itemDTO.getShopId());
		cart.setPrice(itemDTO.getPrice());
		cart.setSkuId(itemDTO.getSkuId());
		cart.setSelectFlag(itemDTO.getSelectFlag());
		cart.setCreateTime(DateUtil.date());
		cart.setTotalCount(itemDTO.getTotalCount());
		cart.setUserId(Long.parseLong(userId.toString()));
		cart.setMaterialUrl(itemDTO.getMaterialUrl());
		cartDao.save(cart);
	}

	@CacheEvict(cacheNames = CART_ITEMS, key = "#userId")
	public void save(Long userId, List<Cart> cartList) {
		cartDao.save(cartList);
	}

	@Override
	@CacheEvict(cacheNames = CART_ITEMS, key = "#userId")
	public void update(Object userId, CartItemDTO cartItemDTO) {
		cartDao.update(cartConverter.convertShoppingCart(cartItemDTO));
	}

	@Override
	@CacheEvict(cacheNames = CART_ITEMS, key = "#userId")
	public void update(Object userId, List<CartItemDTO> cartItemList) {
		cartDao.update(cartConverter.convertShoppingCartList(cartItemList));
	}

	@Override
	@CacheEvict(cacheNames = CART_ITEMS, key = "#userId")
	public void delete(Object userId, List<Long> cartIds) {
		List<CartItemDTO> carts = cartDao.queryByUserId(Long.parseLong(userId.toString()), cartIds);
		cartDao.delete(Long.parseLong(userId.toString()), cartIds);

		// 还需要清除购物车促销缓存
		List<CartChangePromotionDTO> list = cacheManagerUtil.getCache(CacheConstants.CART_PROMOTION_ITEMS, userId);
		//没有缓存，初始化用户商品项促销活动信息
		if (list == null) {
			return;
		}

		List<Long> skuIds = carts.stream().map(CartItemDTO::getSkuId).distinct().collect(Collectors.toList());
		//如果已存在SKU的促销活动选择，则删除
		list.removeIf(u -> skuIds.contains(u.getId()));

		cacheManagerUtil.putCache(CacheConstants.CART_PROMOTION_ITEMS, userId, list);
	}

	@Override
	@CacheEvict(cacheNames = CART_ITEMS, key = "#userId")
	public void deleteAll(Object userId) {
		cartDao.deleteAll(Long.parseLong(userId.toString()));
	}

}
