/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.query;


import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品到货通知
 *
 * @author legendshop
 */
@Data
@Schema(description = "商品到货通知DTO")
@Accessors(chain = true)
public class ProductArrivalNoticeQuery extends PageParams {

	/**
	 * 商家ID
	 */
	@Schema(description = "商家ID")
	private Long shopId;

	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String productName;

	/**
	 * skuId
	 */
	@NotNull(message = "skuId不能为空")
	@Schema(description = "skuId")
	private Long skuId;

}
