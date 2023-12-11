/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 批量修改sku库存
 * 该类用于修改销售库存，不操作实际库存
 *
 * @author legendshop
 */
@Schema(description = "产品类目DTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BatchUpdateStockDTO {

	@Schema(description = "商品id")
	@NotNull(message = "商品id不能为空")
	private Long productId;

	@Schema(description = "skuId")
	@NotNull(message = "skuId不能为空")
	private Long skuId;

	@Schema(description = "扣减的库存数（如果传负数，则为添加销售库存）")
	@NotNull(message = "扣减的库存数不能为空")
	private Integer basketCount;

	@Schema(description = "订单类型（O普通订单P预售G团购S秒杀MG拼团I积分 ）")
	@NotNull(message = "订单类型不能为空")
	private String orderType;

	@Schema(description = "操作类型（O发布 1修改 2结束 ）")
	@NotNull(message = "操作类型不能为空")
	private String operationType;


}
