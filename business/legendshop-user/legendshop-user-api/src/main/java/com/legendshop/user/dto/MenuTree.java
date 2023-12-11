/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import com.legendshop.user.bo.MenuBO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 菜单的树形结构
 *
 * @author legendshop
 */
@Data
@Schema(description = "菜单树")
@EqualsAndHashCode(callSuper = true)
public class MenuTree extends TreeNode implements Serializable {

	/**
	 * 菜单图标
	 */
	@Schema(description = "菜单图标")
	private String icon;

	/**
	 * 菜单名称
	 */
	@Schema(description = "菜单名称")
	private String name;

	private boolean spread = false;

	/**
	 * 前端路由标识路径
	 */
	@Schema(description = "前端路由标识路径")
	private String path;

	/**
	 * 前端路由标识路径
	 */
	@Schema(description = "路由组件名称")
	private String component;

	/**
	 * 路由缓冲
	 */
	@Schema(description = "路由缓冲")
	private String keepAlive;

	/**
	 * 权限编码
	 */
	@Schema(description = "权限编码")
	private String permission;

	/**
	 * 菜单类型 （0菜单 1按钮）
	 */
	@Schema(description = "菜单类型,0:菜单 1:按钮")
	private String type;

	/**
	 * 菜单标签
	 */
	@Schema(description = "菜单标签")
	private String label;

	/**
	 * 排序值
	 */
	@Schema(description = "排序值")
	private Float sort;

	/**
	 * 排序值
	 */
	@Schema(description = "隐藏或显示")
	private Boolean hiddenFlag;

	/**
	 * 是否包含子节点
	 *
	 * @since 3.7
	 */
	private Boolean hasChildren;

	public MenuTree() {
	}

	public MenuTree(Long id, String name, Long parentId) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.label = name;
	}

	public MenuTree(Long id, String name, MenuTree parent) {
		this.id = id;
		this.parentId = parent.getId();
		this.name = name;
		this.label = name;
	}

	public MenuTree(MenuBO menuBO) {
		this.id = menuBO.getId();
		this.parentId = menuBO.getParentId();
		this.icon = menuBO.getIcon();
		this.name = menuBO.getName();
		this.path = menuBO.getPath();
		this.component = menuBO.getComponent();
		this.type = menuBO.getType();
		this.permission = menuBO.getPermission();
		this.label = menuBO.getName();
		this.sort = menuBO.getSort();
		this.keepAlive = menuBO.getKeepAlive();
		this.hiddenFlag = menuBO.getHiddenFlag();
	}

	public Float getSort() {
		return sort == null ? 0 : sort;
	}
}
