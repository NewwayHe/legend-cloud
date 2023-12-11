/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import cn.hutool.core.util.NumberUtil;
import cn.legendshop.jpaplus.persistence.Transient;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.legendshop.common.core.serialize.BigDecimalSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品项DTO(也就是sku)
 * 用于购物车和下单和营销活动继承
 *
 * @author legendshop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemDTO implements Serializable {

	private static final long serialVersionUID = 6449666342591446099L;
	/**
	 * 商品id
	 */
	@Schema(description = "商品id")
	private Long productId;

	/**
	 * skuId
	 */
	@Schema(description = "skuId")
	private Long skuId;

	/**
	 * sku名称
	 */
	@Schema(description = "sku名称")
	private String skuName;

	/**
	 * 商品规格
	 */
	@Schema(description = "商品规格")
	private String specification;


	@Schema(description = "商品规格id")
	private String propertiesIds;

	/**
	 * sku图片
	 */
	@Schema(description = "sku主图")
	private String pic;

	/**
	 * 库存
	 */
	@Schema(description = "库存")
	private Integer stock;

	/**
	 * 单价
	 */
	@Schema(description = "单价")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal price;

	/**
	 * 数量
	 */
	@Schema(description = "数量")
	private Integer totalCount;

	/**
	 * 优惠券抵扣金额
	 */
	@Schema(description = "优惠券抵扣金额")
	private BigDecimal couponAmount;

	/**
	 * 总价格=单价*数量
	 */
	@Schema(description = "总价格=单价*数量")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal totalPrice;

	/**
	 * 运费模板id
	 */
	@Schema(description = "运费模板id")
	private Long transId;

	@Schema(description = "减去促销优惠后的商品金额金额")
	private BigDecimal discountedPrice;


	/**
	 * 返回购物车项的价格
	 * 数量*单价=价格
	 *
	 * @return
	 */
	public BigDecimal getTotalPrice() {
		return NumberUtil.mul(this.price, this.totalCount);
	}

	@Transient
	public BigDecimal getTotalDiscountedPrice() {
		return NumberUtil.mul(this.discountedPrice, this.totalCount);
	}


	@Override
	public boolean equals(Object obj) {
		//比较地址
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ProductItemDTO other = (ProductItemDTO) obj;
		if (skuId == null) {
			if (other.skuId != null) {
				return false;
			}
		} else if (!skuId.equals(other.skuId)) {
			return false;
		}
		return true;
	}
}
