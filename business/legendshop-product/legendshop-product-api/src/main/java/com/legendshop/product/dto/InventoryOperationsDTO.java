/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.legendshop.product.enums.InventoryOperationsTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
@Schema(description = "库存操作")
public class InventoryOperationsDTO implements Serializable {

	@Schema(description = "操作流水号 【订单号、库存编辑流水号】，可以为空")
	private String number;

	@NotNull
	@Schema(description = "商品ID")
	private Long productId;

	@NotNull
	@Schema(description = "skuID")
	private Long skuId;

	@NotNull
	@Schema(description = "操作库存")
	private Integer stocks;

	@NotNull
	@Schema(description = "库存操作类型")
	private InventoryOperationsTypeEnum type;


}
