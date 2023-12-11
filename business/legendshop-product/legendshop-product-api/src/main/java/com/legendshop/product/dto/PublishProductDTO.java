/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 发布产品Dto
 * 专门用于发布界面，装载商品分类下的属性，参数和品牌
 *
 * @author legendshop
 */
@Data
public class PublishProductDTO implements Serializable {

	private static final long serialVersionUID = 624277799086482609L;

	private Integer isAttrEditable;

	private Integer isParamEditable;

	/**
	 * 参数列表
	 */
	private List<ProductPropertyDTO> propertyDtoList;

	/**
	 * 属性列表
	 */
	private List<ProductPropertyDTO> specDtoList;

	/**
	 * 品牌列表
	 */
	private List<BrandDTO> brandList;

	/**
	 * 个人自定义属性列表
	 */
	private List<ProductPropertyDTO> userPropList;

}
