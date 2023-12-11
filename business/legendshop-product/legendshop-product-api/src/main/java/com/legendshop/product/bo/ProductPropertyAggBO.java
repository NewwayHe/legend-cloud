/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 类目关联管理表(ProductPropertyAgg)实体类
 *
 * @author legendshop
 */
@Schema(description = "类目关联管理表BO")
@Data
public class ProductPropertyAggBO implements Serializable {


	private static final long serialVersionUID = -1488429275507659256L;

	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;

	/**
	 * 产品类型名称
	 */
	@Schema(description = "产品类型名称")
	private String name;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

	/**
	 * 品牌id
	 */
	@Schema(description = "品牌id")
	private Long brandId;

	/**
	 * 品牌列表
	 */
	@Schema(description = "品牌列表")
	private String categoryStr;

	/**
	 * 品牌列表
	 */
	@Schema(description = "品牌列表")
	private String brandStr;

	/**
	 * 规格属性列表
	 */
	@Schema(description = "规格属性列表")
	private String specificationStr;

	/**
	 * 规格属性列表
	 */
	@Schema(description = "规格属性列表")
	private String paramStr;

	/**
	 * 规格属性列表
	 */
	@Schema(description = "规格属性列表")
	private String paramGroupStr;

	/**
	 * 参数组id
	 */
	@Schema(description = "参数组id")
	private Long groupId;

	/**
	 * 类目id
	 */
	@Schema(description = "类目id")
	private List<ProductPropertyAggCategoryBO> categoryList;

	/**
	 * 参数属性
	 */
	@Schema(description = "参数属性")
	private List<ProductPropertyBO> paramList;

	/**
	 * 规格属性
	 */
	@Schema(description = "规格属性")
	private List<ProductPropertyBO> specificationList;

	/**
	 * 参数组属性
	 */
	@Schema(description = "参数组属性")
	private List<ProductPropertyAggParamGroupBO> paramGroupList;

	/**
	 * 品牌
	 */
	@Schema(description = "品牌")
	private List<BrandBO> brandList;


}
