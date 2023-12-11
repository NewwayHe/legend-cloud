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
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品 参数属性dto
 *
 * @author legendshop
 */
@Schema(description = "商品参数属性dto")
@Data
public class ProductParamDTO implements Serializable {

	private static final long serialVersionUID = 9127844181627450794L;

	/**
	 * 属性id
	 */
	@Schema(description = "属性id")
	private Long propId;

	/**
	 * 参数id
	 */
	@Schema(description = "参数id")
	private Long id;

	/**
	 * 参数名
	 */
	@Schema(description = "参数名")
	private String name;

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
	 * 参数值列表
	 */
	private List<ParamValueDTO> paramList;

	/**
	 * 分组名称
	 */
	@Schema(description = "分组名称")
	private String groupName;

	/**
	 * 规格来源 "USER":用户自定义，"SYS"：系统自带
	 * {@link com.legendshop.product.enums.ProductPropertySourceEnum}
	 */
	@Schema(description = "规格来源 \"USER\":用户自定义，\"SYS\"：系统自带")
	private String source;
}
