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
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 类目关联管理表(ProductPropertyAgg)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "类目关联管理DTO")
public class ProductPropertyAggDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -1513927048009333164L;
	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;


	/**
	 * 类目关联名称
	 */
	@NotBlank(message = "类目关联名称不能为空")
	@Schema(description = "类目关联名称")
	private String name;

	/**
	 * 主类目ID
	 */
	@Schema(description = "主类目ID")
	private Long categoryId;

	/**
	 * 分类列表
	 */
	@Schema(description = "分类列表")
	private List<ProductPropertyAggCategoryDTO> categoryList;

	/**
	 * 参数属性列表
	 */
	@Schema(description = "参数属性列表")
	private List<ProductPropertyAggParamDTO> paramList;

	/**
	 * 规格属性列表
	 */
	@Schema(description = "规格属性列表")
	private List<ProductPropertyAggSpecificationDTO> specificationList;

	/**
	 * 品牌列表
	 */
	@Schema(description = "品牌列表")
	private List<ProductPropertyAggBrandDTO> brandIdList;

	/**
	 * 参数组列表
	 */
	@Schema(description = "参数组列表")
	private List<ProductPropertyAggParamGroupDTO> paramGroupList;

}
