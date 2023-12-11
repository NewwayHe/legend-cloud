/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 商家类目(ShopCat)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "商家类目DTO")
public class ShopCategoryDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -4012805210764741496L;

	/**
	 * 类目ID
	 */
	@Schema(description = "类目ID")
	private Long id;


	/**
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	private Long shopId;


	/**
	 * 父节点
	 */
	@Schema(description = "父分类,根节点传-1")
	@NotNull(message = "父分类不能为空")
	@Min(value = -1L, message = "父分类id不能小于-1")
	private Long parentId;


	/**
	 * 类目名称
	 */
	@Schema(description = "类目名称")
	@NotBlank(message = "类目名称不能为空")
	private String name;

	/**
	 * 排序
	 */
	@Schema(description = "排序")
	@NotNull(message = "排序不能为空")
	@Min(value = 0, message = "排序不能小于0")
	private Integer seq;


	/**
	 * 状态，表示正常状态,0为下线状态
	 */
	@Schema(description = "状态，表示正常状态,0为下线状态")
	@NotNull(message = "状态不能为空")
	private Integer status;

	/**
	 * 分类层级
	 */
	@Schema(description = "分类层级, 1~3")
	@NotNull(message = "层级不能为空")
	@Min(value = 1, message = "分类层级不能小于1")
	@Max(value = 3, message = "分类层级不能大于3")
	private Integer grade;

}
