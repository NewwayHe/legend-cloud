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

/**
 * @author legendshop
 */
@Data
public class ProductPropertyInfoDTO implements Serializable {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = -5791341454636722721L;

	/**
	 * The attribute name, e.g. "大小","颜色"
	 */
	private String attrName;

	/**
	 * The attribute value, e.g. "XL","红色"
	 */
	private String attrValue;

}
