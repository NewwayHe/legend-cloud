/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.service.convert;

import cn.hutool.core.date.DateUtil;
import com.legendshop.common.core.service.BaseConverter;
import com.legendshop.order.dto.CartDTO;
import com.legendshop.order.dto.CartItemDTO;
import com.legendshop.order.entity.Cart;
import com.legendshop.product.dto.TransFeeCalProductDTO;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author legendshop
 */
@Mapper
public interface CartConverter extends BaseConverter<Cart, CartDTO> {


	default Cart convertShoppingCart(CartItemDTO cartItemDTO) {
		Cart cart = new Cart();
		cart.setId(cartItemDTO.getId());
		cart.setUserId(cartItemDTO.getUserId());
		cart.setTotalCount(cartItemDTO.getTotalCount());
		cart.setProductId(cartItemDTO.getProductId());
		cart.setSkuId(cartItemDTO.getSkuId());
		cart.setShopId(cartItemDTO.getShopId());
		cart.setSelectFlag(cartItemDTO.getSelectFlag());
		cart.setPrice(cartItemDTO.getPrice());
		cart.setCreateTime(DateUtil.date());
		cart.setMaterialUrl(cartItemDTO.getMaterialUrl());
		return cart;
	}


	default List<Cart> convertShoppingCartList(List<CartItemDTO> cartItemList) {
		return cartItemList.stream().map(this::convertShoppingCart).collect(Collectors.toList());
	}

	List<TransFeeCalProductDTO> convert2TransFeeCalProductDtoList(List<CartItemDTO> cartItemList);
}
