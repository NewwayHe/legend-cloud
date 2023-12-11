/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.legendshop.common.core.serialize.BigDecimalSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 店铺促销活动DTO
 *
 * @author legendshop
 */
@Data
@AllArgsConstructor
public class ShopCartDiscountDTO {

	/**
	 * 促销活动的id
	 */
	private Long marketingId = 0L;

	/**
	 * 促销活动的描述
	 */
	private String desc;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 总优惠金额
	 */
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal totalDiscountAmount;

	/**
	 * 促销活动的商品
	 */
	@Schema(description = "商品列表")
	private List<CartItemDTO> cartItemList = new ArrayList<>();

	/**
	 * 添加购物车项
	 *
	 * @param cartItemDTO
	 */
	public void addCartItem(CartItemDTO cartItemDTO) {
		//判断是否包含同款
		if (cartItemList.contains(cartItemDTO)) {
			//追加数量
			for (CartItemDTO item : cartItemList) {
				if (item.equals(item)) {
					item.setTotalCount(item.getTotalCount() + item.getTotalCount());
				}
			}
		} else {
			cartItemList.add(cartItemDTO);
		}

	}


	/**
	 * 构建shoppingCart
	 *
	 * @param cartItem
	 * @return
	 */
	public ShopCartDiscountDTO(CartItemDTO cartItem) {
		this.marketingId = cartItem.getMarketingId();
		this.desc = cartItem.getMarketingDesc();
		this.type = cartItem.getMarketingType();
		addCartItem(cartItem);
	}

	public BigDecimal getTotalDiscountAmount() {
		return cartItemList.stream().filter(CartItemDTO::getSelectFlag).map(CartItemDTO::getDiscountAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

}
