/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

/**
 * 后台菜单权限表(admin_menu)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_shop_menu")
public class ShopMenu extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 840537586301688660L;

	/**
	 * 菜单ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SHOP_MENU_SEQ")
	private Long id;


	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;


	/**
	 * 权限
	 */
	@Column(name = "permission")
	private String permission;


	/**
	 * 连接地址
	 */
	@Column(name = "path")
	private String path;


	/**
	 * 组件名称
	 */
	@Column(name = "component")
	private String component;

	/**
	 * 图标
	 */
	@Column(name = "icon")
	private String icon;


	/**
	 * 菜单类型
	 */
	@Column(name = "type")
	private String type;

	/**
	 * 父节点
	 */
	@Column(name = "parent_id")
	private Long parentId;


	/**
	 * 顺序
	 */
	@Column(name = "sort")
	private Float sort;


	/**
	 * 显示或隐藏,true为隐藏，false为不隐藏
	 */
	@Column(name = "hidden_flag")
	private Boolean hiddenFlag;

}
