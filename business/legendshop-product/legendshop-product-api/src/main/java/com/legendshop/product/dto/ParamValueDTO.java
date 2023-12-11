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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商品 参数值dto
 *
 * @author legendshop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "参数值dto")
public class ParamValueDTO implements Serializable {

	private static final long serialVersionUID = 9127844181627450794L;

	/**
	 * 参数值Id
	 */
	@Schema(description = "参数值Id")
	private Long valueId;

	/**
	 * 参数值
	 */
	@Schema(description = "参数值")
	private String valueName;

	/**
	 * 是否被sku选中
	 */
	@Schema(description = "是否被sku选中")
	private Boolean selectFlag = false;

}
