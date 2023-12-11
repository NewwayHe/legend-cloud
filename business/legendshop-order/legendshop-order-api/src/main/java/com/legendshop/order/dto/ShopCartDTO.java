/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 店铺购物车
 * 店铺名称，店铺里面的商品项等
 *
 * @author legendshop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopCartDTO implements Serializable {

	private static final long serialVersionUID = -7156892636819200509L;
	/**
	 * 店铺id
	 */
	private Long shopId;

	/**
	 * 店铺名称
	 */
	private String shopName;

	/**
	 * 是否有优惠券可领取
	 */
	private Boolean haveCouponFlag;


	/**
	 * 商家店铺折扣集合
	 */
	private List<ShopCartDiscountDTO> shopCartDiscountList = new ArrayList<>();


	public ShopCartDTO buildShopCartDTO(Long shopId, String shopName, Boolean haveCouponFlag, List<CartItemDTO> list) {
		this.shopId = shopId;
		this.shopName = shopName;
		this.haveCouponFlag = haveCouponFlag;
		list.forEach(this::addDiscountItem);
		return this;
	}

	public ShopCartDTO buildShopCartDTO(Long shopId, String shopName, List<CartItemDTO> list) {
		this.shopId = shopId;
		this.shopName = shopName;
		list.forEach(this::addDiscountItem);
		return this;
	}

	public void addDiscountList(List<CartItemDTO> list) {
		list.forEach(this::addDiscountItem);
	}

	/**
	 * 添加营销活动分组的项
	 */
	public void addDiscountItem(CartItemDTO cartItemDTO) {
		Optional<ShopCartDiscountDTO> dto = shopCartDiscountList
				.stream()
				.filter(item -> ObjectUtil.isNotNull(item.getMarketingId()) && ObjectUtil.isNotNull(cartItemDTO.getMarketingId()) && item.getMarketingId().equals(cartItemDTO.getMarketingId()))
				.findAny();
		if (dto.isPresent()) {
			dto.get().addCartItem(cartItemDTO);
		} else {
			ShopCartDiscountDTO shoppingCartMarketingDTO = new ShopCartDiscountDTO(cartItemDTO);
			shopCartDiscountList.add(shoppingCartMarketingDTO);

		}

	}
}
