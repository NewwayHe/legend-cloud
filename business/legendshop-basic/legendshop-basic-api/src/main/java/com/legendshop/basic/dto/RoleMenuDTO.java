/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色和菜单的关系表
 *
 * @author legendshop
 */
@Data
public class RoleMenuDTO implements Serializable {

	/**
	 * 角色id
	 */
	private Long roleId;

	/**
	 * 菜单id集合
	 */
	private String menuIds;
}
