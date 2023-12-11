/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 属性图片
 *
 * @author legendshop
 */
@Data
public class ProductPropertyImageBO implements Serializable {


	/**
	 * 主键
	 */
	private Long id;

	/**
	 * 商品ID
	 */
	private Long productId;

	/**
	 * 属性ID
	 */
	private Long propId;

	/**
	 * 属性值ID
	 */
	private Long valueId;

	/**
	 * 属性值名称
	 */
	private String valueName;

	/**
	 * 图片Url
	 */
	private String url;

	/**
	 * 顺序
	 */
	private Long seq;

	/**
	 * 创建时间
	 */
	private Date createDate;

} 
