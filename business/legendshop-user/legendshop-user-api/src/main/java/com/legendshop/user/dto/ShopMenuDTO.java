/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author legendshop
 */
@Getter
@Setter
@Schema(description = "商家菜单BO")
public class ShopMenuDTO implements Serializable {

	/**
	 * 菜单ID
	 */
	@Schema(description = "菜单ID")
	private Long id;


	/**
	 * 名称
	 */
	@Schema(description = "名称")
	@NotNull(message = "权限菜单名称不能为空")
	private String name;


	/**
	 * 权限
	 */
	@Schema(description = "权限")
	private String permission;


	/**
	 * 连接地址
	 */
	@Schema(description = "连接地址")
	private String path;


	/**
	 * 组件名称
	 */
	@Schema(description = "组件名称")
	private String component;

	/**
	 * 图标
	 */
	@Schema(description = "图标")
	private String icon;


	/**
	 * 菜单类型
	 */
	@Schema(description = "菜单类型")
	@NotNull(message = "权限菜单类型不能为空")
	private String type;

	/**
	 * 父节点
	 */
	@Schema(description = "父节点")
	private Long parentId;


	/**
	 * 顺序
	 */
	@Schema(description = "顺序")
	private Float sort;


	/**
	 * 显示或隐藏,true为隐藏，false为不隐藏
	 */
	@Schema(description = "显示或隐藏, true为隐藏，false为不隐藏")
	private Boolean hiddenFlag;

	/**
	 * 子菜单列表
	 */
	private List<ShopMenuDTO> children;
}
