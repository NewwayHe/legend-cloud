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
import com.legendshop.common.core.dto.BaseDTO;
import com.legendshop.product.enums.ProductGroupSortEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * (ProdGroup)实体类
 *
 * @author legendshop
 */
@Schema(description = "商品分组DTO")
@Data
public class ProductGroupDTO extends BaseDTO implements Serializable {


	private static final long serialVersionUID = -1262425870499299039L;
	/**
	 * 主键ID
	 */
	@Schema(description = "主键ID")
	private Long id;


	/**
	 * 商品分组名称
	 */
	@Schema(description = "商品分组名称")
	@NotBlank(message = "分组名称不能为空")
	private String name;


	/**
	 * 分组类型 0:系统定义 1:自定义
	 */
	@Schema(description = "分组类型 0:系统定义 1:自定义")
	private Integer type;


	/**
	 * 分组条件
	 */
	@Schema(description = "分组条件")
	private String conditional;


	/**
	 * 组内排序条件
	 */
	@EnumValid(target = ProductGroupSortEnum.class, message = "排序条件状态错误")
	@Schema(description = "组内排序条件 [销量：buys   发布时间： createTime]")
	private String sort;


	/**
	 * 商品分组描述
	 */
	@Schema(description = "商品分组描述")
	private String description;


}
