/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dao.impl;

import cn.hutool.core.collection.CollUtil;
import cn.legendshop.jpaplus.impl.GenericDaoImpl;
import cn.legendshop.jpaplus.support.lambda.LambdaEntityCriterion;
import com.legendshop.order.dao.CartDao;
import com.legendshop.order.dto.CartItemDTO;
import com.legendshop.order.entity.Cart;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 购物车Dao实现类.
 *
 * @author legendshop
 */
@Repository
public class CartDaoImpl extends GenericDaoImpl<Cart, Long> implements CartDao {

	@Override
	public List<CartItemDTO> queryByUserId(Long userId) {
		String sql = getSQL("Cart.queryByUserId");
		return query(sql, CartItemDTO.class, userId);
	}

	@Override
	public List<CartItemDTO> queryByUserId(Long userId, List<Long> cartIds) {
		if (CollUtil.isEmpty(cartIds) || userId == null) {
			return Collections.emptyList();
		}

		LambdaEntityCriterion<CartItemDTO> criterion = new LambdaEntityCriterion<>(CartItemDTO.class);
		criterion.eq(CartItemDTO::getUserId, userId);
		criterion.in(CartItemDTO::getId, cartIds);
		return queryDTOByProperties(criterion);
	}

	@Override
	public void delete(Long userId, List<Long> shoppingCartIds) {
		List<Long> arr = new ArrayList<>();
		arr.add(userId);
		StringBuffer sb = new StringBuffer("delete from ls_cart where user_id = ? and id in( ");
		for (Long id : shoppingCartIds) {
			arr.add(id);
			sb.append("?,");
		}
		sb.setLength(sb.length() - 1);
		sb.append(" ) ");
		update(sb.toString(), arr.toArray());
	}

	@Override
	public void deleteAll(Long userId) {
		String sql = "delete from ls_cart where user_id = ?";
		update(sql, userId);
	}

	@Override
	public List<CartItemDTO> queryInvalidProductList(Long userId) {
		String sql = getSQL("Cart.queryInvalidProductList");
		return query(sql, CartItemDTO.class, userId);
	}

	@Override
	public void cleanInvalidProduct(Long userId) {
		String sql = getSQL("Cart.cleanExpiryProdList");
		update(sql, userId);
	}


	@Override
	public void batchClean(Long userId, List<Long> skuIds) {
		List<Long> args = new ArrayList<>();
		args.add(userId);
		StringBuffer sb = new StringBuffer("delete from ls_cart  where user_id=? and sku_id in( ");
		for (Long id : skuIds) {
			sb.append("?,");
			args.add(id);
		}
		sb.setLength(sb.length() - 1);
		sb.append(" ) ");
		update(sb.toString(), args.toArray());
	}
}
