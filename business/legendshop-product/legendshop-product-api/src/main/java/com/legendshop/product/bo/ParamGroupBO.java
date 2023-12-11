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
import java.util.List;

/**
 * 参数组表(ProductPropertyParamGroup)BO
 *
 * @author legendshop
 */
@Data
@Schema(description = "参数组BO")
public class ParamGroupBO implements Serializable {


	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;

	/**
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	private Long shopId;

	/**
	 * 类目关联管理id
	 */
	@Schema(description = "类目关联管理id")
	private Long aggId;

	/**
	 * 名称
	 */
	@Schema(description = "名称")
	private String name;

	/**
	 * 属性来源 {@link com.legendshop.product.enums.ProductPropertySourceEnum}
	 */
	@Schema(description = "属性来源：\"USER\"; 用户自定义，\"SYS\"：系统自带")
	private String source;

	/**
	 * 副标题
	 */
	@Schema(description = "副标题")
	private String memo;

	/**
	 * 参数列表
	 */
	@Schema(description = "参数列表")
	private String paramsStr;

	/**
	 * 类目关联管理
	 */
	@Schema(description = "类目关联管理")
	private String PropertyAggStr;

	/**
	 * 参数属性id
	 */
	@Schema(description = "参数属性id")
	private List<Long> paramsList;

	/**
	 * 参数属性列表
	 */
	@Schema(description = "参数属性列表")
	private List<ProductPropertyBO> params;

	/**
	 * 添加对应的属性到属性列表中
	 *
	 * @param ProductPropertyBO
	 */
	public void addParams(ProductPropertyBO ProductPropertyBO) {
		if (this.getId().equals(ProductPropertyBO.getGroupId())) {
			params.add(ProductPropertyBO);
		}
	}
}
