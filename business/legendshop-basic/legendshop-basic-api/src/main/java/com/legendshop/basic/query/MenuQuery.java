/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.query;

import cn.legendshop.jpaplus.support.PageParams;
import lombok.Getter;
import lombok.Setter;

/**
 * 菜单查询DTO
 *
 * @author legendshop
 */
@Setter
@Getter
public class MenuQuery extends PageParams {

	private static final long serialVersionUID = -5448061706443386147L;

	Integer grade;

	Long parentId;

	String name;

}
