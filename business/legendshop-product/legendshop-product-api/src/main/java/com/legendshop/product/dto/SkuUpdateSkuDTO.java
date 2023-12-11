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
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * sku更新DTO
 *
 * @author legendshop
 */
@Data
public class SkuUpdateSkuDTO implements Serializable {

	private static final long serialVersionUID = 448624759857938407L;

	private Long id;

	/**
	 * 操作库存数
	 */
	@Schema(description = "操作库存数")
	@Min(value = 0, message = "库存数必须大于等于0")
	private Integer editStocks;

	/**
	 * 入库标识
	 */
	@Schema(description = "入库标识：true增加 false减少，默认true")
	private Boolean putStorageFlag;

//	@Schema(description = "修改标识：true改库存 false改价，默认true")
//	private Boolean flag;

	@Schema(description = "价格")
	@Min(value = 0, message = "必须大于等于0")
	private BigDecimal editPrice;
}
