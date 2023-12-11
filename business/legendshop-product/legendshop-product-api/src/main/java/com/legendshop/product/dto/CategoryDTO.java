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
import com.legendshop.product.bo.CategoryBO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 公用的产品类目(Category)实体类
 *
 * @author legendshop
 */
@Schema(description = "产品类目DTO")
@Data
public class CategoryDTO extends BaseDTO implements Serializable {


	private static final long serialVersionUID = -6033886614124691380L;

	/**
	 * 产品类目ID
	 */
	@Schema(description = "主键")
	private Long id;


	/**
	 * 父节点
	 */
	@NotNull
	@Schema(description = "父节点")
	private Long parentId;


	/**
	 * 产品类目名称
	 */
	@NotEmpty
	@Schema(description = "产品类目名称")
	private String name;


	/**
	 * 类目图标
	 */
	@Schema(description = "类目图标")
	private String icon;


	/**
	 * 产品类目类型,{@link com.legendshop.product.enums.ProductTypeEnum}
	 */
	@Schema(description = "产品类目类型")
	private String type;


	/**
	 * 排序
	 */
	@Schema(description = "排序")
	@NotNull(message = "排序不能为空")
	@Min(value = 0, message = "排序不能小于0")
	private Integer seq;


	/**
	 * 默认是1，表示正常状态,0为下线状态
	 */
	@NotNull
	@Schema(description = "默认是1，表示正常状态,0为下线状态")
	private Integer status;

	/**
	 * 分类层级
	 */
	@NotNull(message = "层级不能为空")
	@Min(value = 0, message = "分类层级不能小于0")
	@Max(value = 3, message = "分类层级不能大于3")
	@Schema(description = "分类层级")
	private Integer grade;


	/**
	 * 推荐内容
	 */
	@Schema(description = "推荐内容")
	private String recommCon;


	/**
	 * 退换货有效时间
	 */
	@Schema(description = "退换货有效时间")
	@Min(value = 0, message = "退换货有效时间不能小于0")
	private Integer returnValidPeriod;

	/**
	 * 分类导航 推荐内容
	 */
	@Schema(description = "分类导航 推荐内容")
	private RecommConDTO recommConDTO;

	/**
	 * 子分类
	 */
	@Schema(description = "子分类")
	private List<CategoryBO> childrenList;

	@Schema(description = "参数组ID")
	private Long aggId;


}
