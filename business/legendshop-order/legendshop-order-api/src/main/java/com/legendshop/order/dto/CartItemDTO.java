/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import cn.hutool.core.util.NumberUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.legendshop.common.core.serialize.BigDecimalSerialize;
import com.legendshop.product.bo.SkuBO;
import com.legendshop.product.dto.ProductDTO;
import com.legendshop.product.dto.ProductItemDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 店铺购物车项DTO
 *
 * @author legendshop
 */
@Data
public class CartItemDTO extends ProductItemDTO implements Serializable {

	private static final long serialVersionUID = 5217675609901241331L;

	/**
	 * 购物车项的id
	 */
	@Schema(description = "购物车id")
	private Long id;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private Long userId;

	/**
	 * 店铺id
	 */
	@Schema(description = "shopId")
	private Long shopId;

	/**
	 * 店铺名称
	 */
	@Schema(description = "店铺名称")
	private String shopName;

	/**
	 * 选中状态
	 */
	@Schema(description = "选中状态")
	private Boolean selectFlag;

	/**
	 * 促销活动优惠金额
	 */
	@Schema(description = "促销活动优惠金额")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal discountAmount = new BigDecimal("0.00");

	/**
	 * 真实的总价=总价-优惠金额
	 */
	@Schema(description = "真实的总价=总价-优惠金额")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal actualTotalPrice;

	@Schema(description = "限时营销Id，为空时，默认选中最优活动")
	private Long limitDiscountsMarketingId;

	@Schema(description = "限时营销减免后商品单价")
	private BigDecimal limitDiscountsMarketingUnitPrice = BigDecimal.ZERO;

	@Schema(description = "限时营销减免价格")
	private BigDecimal limitDiscountsMarketingPrice = BigDecimal.ZERO;


	/**
	 * 促销活动id
	 */
	@Schema(description = "促销活动id")
	private Long marketingId;

	/**
	 * 促销活动类型
	 */
	@Schema(description = "营销活动类型")
	private String marketingType;

	/**
	 * 促销活动描述
	 */
	@Schema(description = "促销活动描述")
	private String marketingDesc;


	@Schema(description = "区域限售标识")
	private Boolean regionalSalesFlag = Boolean.FALSE;

	@Schema(description = "物料URL")
	private String materialUrl;

	@Schema(description = "是否预售")
	private Boolean preSellFlag;


	public CartItemDTO(ProductDTO productDTO, SkuBO skuBO, CartParam cartParam) {
		super(productDTO.getId(), skuBO.getId(), productDTO.getName(), skuBO.getCnProperties(), skuBO.getProperties(), skuBO.getPic(), skuBO.getActualStocks(), skuBO.getPrice(), cartParam.getCount(), BigDecimal.ZERO, BigDecimal.ZERO, productDTO.getTransId(), skuBO.getPrice());
		this.shopId = productDTO.getShopId();
		this.selectFlag = cartParam.getSelectFlag();
		this.materialUrl = cartParam.getMaterialUrl();
	}

	public CartItemDTO() {

	}

	/**
	 * 真实付款金额
	 * 总价格-优惠价格=真实付款价格
	 *
	 * @return
	 */
	public BigDecimal getActualTotalPrice() {
		return NumberUtil.sub(this.getTotalPrice(), discountAmount);
	}


}
