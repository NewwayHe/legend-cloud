/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
@Schema(description = "运营概况")
public class BusinessDataDTO {

	@Schema(description = "访问商城次数")
	private Integer pv;

	@Schema(description = "访问商城用户数")
	private Integer uv;

	@Schema(description = "访问商品次数")
	private Integer productPv;

	@Schema(description = "访问商品用户数")
	private Integer productUv;

	@Schema(description = "加入购物车商品数")
	private Integer cartSkuNum;

	@Schema(description = "加入收藏商品数")
	private Integer favoriteNum;

	@Schema(description = "下单用户数")
	private Integer placeUserNum;

	@Schema(description = "下单订单数量")
	private Integer placeOrderNum;

	@Schema(description = "下单商品数量")
	private Integer placeSkuNum;

	@Schema(description = "下单订单金额")
	private BigDecimal placeAmount;

	@Schema(description = "支付用户数")
	private Integer payUserNum;

	@Schema(description = "支付订单数量")
	private Integer payOrderNum;

	@Schema(description = "支付商品数量")
	private Integer paySkuNum;

	@Schema(description = "支付订单金额")
	private BigDecimal payAmount;

	@Schema(description = "成交用户数")
	private Integer dealUserNum;

	@Schema(description = "成交订单数量")
	private Integer dealOrderNum;

	@Schema(description = "成交商品数量")
	private Integer dealSkuNum;

	@Schema(description = "成交订单金额")
	private BigDecimal dealAmount;

	@Schema(description = "售后订单数量")
	private Integer returnOrderNum;

	@Schema(description = "售后订单金额")
	private BigDecimal returnAmount;

	@Schema(description = "转化率")
	private BigDecimal inversionRate;

	@Schema(description = "访客-意向转化率 = 加入购物车商品数 / 访问商品用户数")
	private BigDecimal viewAddRate;

	@Schema(description = "意向-下单转化率 = 下单用户数 / 加入购物车商品数")
	private BigDecimal addPlaceRate;

	@Schema(description = "下单-支付转化率 = 支付用户数 / 下单用户数")
	private BigDecimal placePayRate;

	@Schema(description = "成交-支付转化率 = 成交用户数 / 支付用户数")
	private BigDecimal dealPayRate;

}
