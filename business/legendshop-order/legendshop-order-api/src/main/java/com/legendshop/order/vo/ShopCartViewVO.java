/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.legendshop.common.core.serialize.BigDecimalSerialize;
import com.legendshop.order.dto.ShopCartDTO;
import com.legendshop.user.bo.UserAddressBO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车展示对象
 *
 * @author legendshop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "购物车对象")
public class ShopCartViewVO {


	/**
	 * 按店铺拆分的店铺购物车集合
	 */
	@Schema(description = "店铺购物车项")
	private List<ShopCartDTO> shopCartList;


	/**
	 * 无效的商品列表
	 */
	private List<ShopCartDTO> invalidShopCartList;

	/**
	 * 选择商品数量
	 */
	@Schema(description = "已选择的商品数量")
	private Integer selectCount;

	/**
	 * 用户默认地址
	 */
	@Schema(description = "用户默认地址")
	private UserAddressBO defaultUserAddress;

	/**
	 * 选择商品促销活动优惠金额
	 */
	@Schema(description = "选择商品促销活动优惠金额")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal discountAmount = new BigDecimal("0.00");

	/**
	 * 选择商品总价格，减去优惠价格
	 */
	@Schema(description = "选择商品总价格=商品总价格-优惠价格")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal totalPrice;


}
