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

/**
 * @author legendshop
 */
@Data
public class ShopCategoryBO implements Serializable {

	private static final long serialVersionUID = -5448136245859888000L;

	/**
	 * 分类ID
	 */
	private Long id;

	/**
	 * 分类名称
	 */
	private String name;

	/**
	 * 父类ID
	 */
	private Long parentId;

	/**
	 * 父类名称
	 */
	private String parentName;

	/**
	 * 分类层级
	 */
	private Integer grade;

	/**
	 * 排序
	 */
	private Integer seq;

	/**
	 * 默认是1，表示正常状态,0为下线状态
	 */
	private Integer status;

}

