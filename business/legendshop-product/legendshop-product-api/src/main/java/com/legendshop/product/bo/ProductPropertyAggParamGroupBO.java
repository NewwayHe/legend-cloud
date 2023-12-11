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
 * 类目关联管理-参数属性
 *
 * @author legendshop
 */
@Data
@Schema(description = "类目关联管理-参数属性BO")
public class ProductPropertyAggParamGroupBO implements Serializable {

	private static final long serialVersionUID = 3610928732859215821L;

	/**
	 * 参数组id
	 */
	@Schema(description = "参数组id")
	private Long id;

	/**
	 * 参数组-类目关联表关联id
	 */
	@Schema(description = "参数组-类目关联表关联id")
	private Long aggGroupId;

	/**
	 * 名称
	 */
	@Schema(description = "名称")
	private String name;

	/**
	 * 副标题
	 */
	@Schema(description = "副标题")
	private String memo;

	/**
	 * 参数列表
	 */
	@Schema(description = "参数列表")
	private String propertyStr;
}
