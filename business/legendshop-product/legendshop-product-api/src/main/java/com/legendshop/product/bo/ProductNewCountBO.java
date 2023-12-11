/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
@Builder
@Schema(description = "商品新增信息统计BO")
public class ProductNewCountBO {

	/**
	 * 当天新增发布商品数量
	 */
	@Schema(description = "当天新增发布商品数量")
	private Integer newProductCount;

	/**
	 * 当天新增商品订单数量
	 */
	@Schema(description = "当天新增商品订单数量")
	private Integer newOrderCount;

	/**
	 * 当天新增售后订单数量
	 */
	@Schema(description = "当天新增售后订单数量")
	private Integer newAfterSaleOrderCount;

	/**
	 * 当天新增举报商品数量
	 */
	@Schema(description = "当天新增举报商品数量")
	private Integer newReportProductCount;


	/**
	 * 当天新增商品咨询数量
	 */
	@Schema(description = "当天新增商品咨询数量")
	private Integer newReferProductCount;
}
