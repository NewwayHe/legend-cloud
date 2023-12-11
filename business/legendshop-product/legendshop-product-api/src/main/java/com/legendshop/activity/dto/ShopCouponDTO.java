/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.legendshop.activity.enums.CouponSelectStatusEnum;
import com.legendshop.common.core.serialize.BigDecimalSerialize;
import com.legendshop.product.dto.ProductItemDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券项DTO
 *
 * @author legendshop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商家优惠券集合（订单级别）DTO")
public class ShopCouponDTO implements Serializable {

	private static final long serialVersionUID = 3708524537924008389L;

	/**
	 * 传递的信息
	 * ------------------------------------------------------------------
	 */
	/**
	 * 店铺id
	 */
	@Schema(description = "店铺id")
	private Long shopId;

	/**
	 * 传递的商品列表集合
	 */
	@Schema(description = "商品集合")
	private List<ProductItemDTO> productItems;

	@Schema(description = "商品总金额")
	private BigDecimal productTotalAmount;

	@Schema(description = "用户选择的优惠券id集合")
	private List<Long> couponIds;

	@Schema(description = "商品促销及优惠券后的金额")
	private BigDecimal productCouponAfterAmount;

	/**
	 * 返回的结果
	 * ------------------------------------------------------------------
	 */


	/**
	 * 优惠券列表
	 */
	@Schema(description = "优惠券集合")
	private List<CouponItemDTO> couponItems;

	@Schema(description = "用户选择的优惠券集合优惠券集合")
	private List<CouponItemDTO> selectItems;

	/**
	 * 优惠券列表
	 */
	@Schema(description = "无用优惠券集合")
	private List<CouponItemDTO> unavailableCouponItems;

	/**
	 * 优惠券抵扣金额
	 */
	@JsonSerialize(using = BigDecimalSerialize.class)
	@Schema(description = "优惠券抵扣金额")
	private BigDecimal discountAmount;

	/**
	 * 使用优惠券张数
	 */
	@Schema(description = "使用优惠券数量")
	private Integer useCouponCount;

	@Schema(description = "商家优惠劵对象")
	private List<CouponOrderDTO> couponOrderList;

	public ShopCouponDTO(Long shopId, List<ProductItemDTO> productItems) {
		this.shopId = shopId;
		this.productItems = productItems;
	}

	public ShopCouponDTO(Long shopId, List<ProductItemDTO> productItems, BigDecimal productTotalAmount, BigDecimal productCouponAfterAmount) {
		this.shopId = shopId;
		this.productItems = productItems;
		this.productTotalAmount = productTotalAmount;
		this.productCouponAfterAmount = productCouponAfterAmount;
	}

	public BigDecimal getDiscountAmount() {
		if (CollUtil.isEmpty(couponItems)) {
			return BigDecimal.ZERO;
		}
		return getSelectCouponItems().stream().map(CouponItemDTO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public Integer getUseCouponCount() {
		if (CollUtil.isEmpty(couponItems)) {
			return 0;
		}
		return getSelectCouponItems().size();
	}

	private List<CouponItemDTO> getSelectCouponItems() {
		return couponItems.stream().filter(couponItemDTO -> CouponSelectStatusEnum.SELECTED.getStatus().equals(couponItemDTO.getSelectStatus())).collect(Collectors.toList());
	}
}
