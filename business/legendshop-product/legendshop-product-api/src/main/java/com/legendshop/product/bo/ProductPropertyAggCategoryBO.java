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

/**
 * 类目关联管理-平台类目关联BO
 *
 * @author legendshop
 */
@Data
@Schema(description = "类目关联管理-平台类目关联BO")
public class ProductPropertyAggCategoryBO implements Serializable {

	private static final long serialVersionUID = 6311694119704058009L;

	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;


	/**
	 * 聚合Id
	 */
	@Schema(description = "聚合Id")
	private Long aggId;


	/**
	 * 分类ID
	 */
	@Schema(description = "分类ID")
	private Long categoryId;


	/**
	 * 顺序
	 */
	@Schema(description = "顺序")
	private Integer seq;


	/**
	 * 父节点
	 */
	@Schema(description = "父节点")
	private Long parentId;


	/**
	 * 产品类目名称
	 */
	@Schema(description = "产品类目名称")
	private String name;


	/**
	 * 类目图标
	 */
	@Schema(description = "类目图标")
	private String icon;


}
