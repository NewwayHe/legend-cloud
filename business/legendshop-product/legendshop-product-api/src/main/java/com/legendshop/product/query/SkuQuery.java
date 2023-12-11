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
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 单品SKU表(Sku)实体类
 *
 * @author legendshop
 */
@Data
@Accessors(chain = true)
@Schema(description = "单品SKU参数查询")
public class SkuQuery extends PageParams {


	/**
	 * 单品ID
	 */
	@Schema(description = "单品ID")
	private Long id;


	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	private Long productId;


	/**
	 * 价格
	 */
	@Schema(description = "价格")
	private BigDecimal price;


	/**
	 * 虚拟库存（商品在付款减库存的状态下，该sku上未付款的订单数量）
	 */
	@Schema(description = "虚拟库存（商品在付款减库存的状态下，该sku上未付款的订单数量）")
	private Integer stocks;


	/**
	 * 实际库存
	 */
	@Schema(description = "实际库存")
	private Integer actualStocks;


	/**
	 * SKU名称
	 */
	@Schema(description = "SKU名称")
	private String name;


	/**
	 * sku状态。 1l:正常 ；0:删除
	 */
	@Schema(description = "sku状态。 1l:正常 ；0:删除")
	private int status;


	/**
	 * 商家编码
	 */
	@Schema(description = "商家编码")
	private String partyCode;


	/**
	 * 商品条形码
	 */
	@Schema(description = "商品条形码")
	private String modelId;

	/**
	 * sku营销活动类型
	 * {@link com.legendshop.product.enums.SkuActiveTypeEnum}
	 */
	@Schema(description = "sku营销活动类型")
	private String skuType;

}
