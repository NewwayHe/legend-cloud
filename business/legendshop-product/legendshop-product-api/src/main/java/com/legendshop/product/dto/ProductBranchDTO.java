/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.legendshop.common.core.annotation.EnumValid;
import com.legendshop.product.enums.ProductStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用于批量处理商品状态的类
 *
 * @author legendshop
 */
@Data
@Schema(description = "批量处理商品状态")
public class ProductBranchDTO implements Serializable {

	/**
	 * id集合
	 */
	@NotEmpty(message = "处理的id集合不能为空")
	@Schema(description = "商品id集合")
	private List<Long> ids;

	/**
	 * 状态 {@link com.legendshop.product.enums.ProductStatusEnum}
	 */
	@Schema(description = "商品状态")
	@EnumValid(target = ProductStatusEnum.class, message = "状态标识不匹配")
	private Integer status;

	private Long shopId;
}
