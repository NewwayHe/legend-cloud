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
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 自定义规格组属性入参
 *
 * @author legendshop
 */
@Data
@Schema(description = "自定义规格组属性")
public class SpecificationDTO implements Serializable {

	private static final long serialVersionUID = 5255860172256471719L;

	/**
	 * 规格名id
	 */
	@Schema(description = "规格名id")
	private Long id;

	/**
	 * 规格名
	 */
	@Schema(description = "规格名")
	private String propName;

	/**
	 * 规格值
	 */
	@Valid
	@Schema(description = "规格值")
	private List<ProductPropertyValueDTO> prodPropList;

	/**
	 * 参数组来源 "USER":用户自定义，"SYS"：系统自带
	 * {@link com.legendshop.product.enums.ProductPropertySourceEnum}
	 */
	@Schema(description = "参数组来源：\"USER\":用户自定义，\"SYS\"：系统自带")
	@EnumValid(target = ProductPropertySourceEnum.class, message = "参数组来源不匹配")
	private String source;

	/**
	 * 属性类型 {@link com.legendshop.product.enums.ProductPropertyTypeEnum }
	 */
	@Schema(description = "属性类型：\"TXT\"：文本类型; \"PIC\":图片类型;")
	private String type;
}
