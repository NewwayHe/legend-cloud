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
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author legendshop
 */
@Setter
@Getter
@Accessors(chain = true)
public class ShopCategoryQuery extends PageParams {

	private static final long serialVersionUID = 1953369548488721337L;

	private Long shopId;

	/**
	 * 产品类目名称
	 */
	private String name;

	private Long shopCatId;

	/**
	 * 分类层级
	 */
	private Integer grade;

	/**
	 * 父节点
	 */
	private Long parentId;
}
