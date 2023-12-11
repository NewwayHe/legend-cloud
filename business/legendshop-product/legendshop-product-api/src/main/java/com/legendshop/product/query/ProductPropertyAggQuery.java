/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.query;


import cn.legendshop.jpaplus.support.PageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 类目关联表(ProductPropertyAgg)查询对象
 *
 * @author legendshop
 */
@Data
@Schema(description = "类目关联查询参数")
public class ProductPropertyAggQuery extends PageParams {


	/**
	 * 主键(要排除的id)
	 */
	@Schema(description = "主键(要排除的id)")
	private Long id;

	private Long brandId;

	/**
	 * 产品类型名称
	 */
	@Schema(description = "产品类型名称")
	private String name;

	/**
	 * 顺序
	 */
	@Schema(description = "顺序")
	private Integer sequence;

	/**
	 * 分类id
	 */
	@Schema(description = "分类id")
	private Long categoryId;
	private Long aggId;

	@Schema(description = "简单搜索来源 [param:参数属性, specification:规格属性 ,paramGroup:参数组属性,other:新增，没有要排除的关联管理 ]")
	private String source;
}
