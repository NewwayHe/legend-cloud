/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 商家权限表(ShopPerm)实体类
 *
 * @author legendshop
 */
@Data
public class ShopPermDTO implements Serializable {


	private static final long serialVersionUID = -6010736141873779955L;
	/**
	 * 角色ID
	 */
	private Long roleId;


	/**
	 * 对应的菜单页面，参见shopMenu
	 */
	private String label;


	/**
	 * 权限标识（check为查看，editor为编辑）以逗号分隔
	 */
	private String function;

}
