/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.query;

import cn.legendshop.jpaplus.support.PageParams;
import lombok.Data;

/**
 * 角色查询对象
 *
 * @author legendshop
 */
@Data
public class RoleQuery extends PageParams {

	/**
	 * 角色名称
	 */
	private String name;

}
