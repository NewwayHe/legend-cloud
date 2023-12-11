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
import com.legendshop.product.enums.ProductPropertySourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 参数组属性（用于商品发布保存）
 *
 * @author legendshop
 */
@Schema(description = "参数组属性")
@Data
public class ParamPropertyGroupDTO implements Serializable {

	private static final long serialVersionUID = 3416067024783849640L;
	/**
	 * 属性ID
	 */
	@Schema(description = "属性ID")
	@NotNull(message = "属性ID不能为空")
	private Long id;

	/**
	 * 参数组id
	 */
	@Schema(description = "参数组ID")
	@NotNull(message = "参数组id不能为空")
	private Long groupId;

	/**
	 * 参数组名称
	 */
	@Schema(description = "参数组名称")
	@NotBlank(message = "参数组名称不能为空")
	private String groupName;

	/**
	 * 属性名称
	 */
	@Schema(description = "属性名称")
	@NotBlank(message = "属性名称不能为空")
	private String propName;

	/**
	 * 参数组来源 "USER":用户自定义，"SYS"：系统自带
	 * {@link com.legendshop.product.enums.ProductPropertySourceEnum}
	 */
	@Schema(description = "参数组来源：\"USER\":用户自定义，\"SYS\"：系统自带")
	@EnumValid(target = ProductPropertySourceEnum.class, message = "参数组来源不匹配")
	private String source;

	/**
	 * 属性对应的属性值集合
	 */
	@Valid
	@Schema(description = "属性值列表")
	private List<ProductPropertyValueDTO> prodPropList;
}
