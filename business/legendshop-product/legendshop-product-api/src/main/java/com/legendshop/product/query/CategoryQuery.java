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
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分类搜索DTO
 *
 * @author legendshop
 */
@Data
@Accessors(chain = true)
public class CategoryQuery extends PageParams {

	/**
	 * 排序类型
	 */
	private String sortType;

	/**
	 * 是否Header菜单展示，0否，1是
	 */
	private Integer headerMenu;

	/**
	 * 导航菜单中显示，0否1是
	 */
	private Integer navigationMenu;

	/**
	 * 默认是1，表示正常状态,0为下线状态
	 */
	private Integer status;

	/**
	 * 等级
	 */
	private Integer level;


}
