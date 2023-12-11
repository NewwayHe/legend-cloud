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
import java.util.List;

/**
 * 菜单DTO TreeSelect专用
 *
 * @author legendshop
 */
@Data
public class TreeSelectMenuDTO implements Serializable {


	private static final long serialVersionUID = 5180076576628792769L;

	/**
	 * 菜单ID
	 */
	private Long id;

	/**
	 * 父菜单ID
	 */
	private Long parentId;


	/**
	 * 菜单名称
	 */
	private String name;


	/**
	 * 子菜单列表
	 */
	private List<TreeSelectMenuDTO> children;


}
