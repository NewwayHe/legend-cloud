/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;


import com.legendshop.common.core.dto.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

/**
 * 后台菜单定义表(Menu)实体类
 *
 * @author legendshop
 */
@Data
public class AdminMenuDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -4281852439559678335L;

	/**
	 * 菜单ID
	 */
	private Long id;


	/**
	 * 名称
	 */
	@NotBlank(message = "菜单名称不能为空")
	@Length(max = 45, message = "名称长度不能超过45")
	private String name;


	/**
	 * 权限
	 */
	private String permission;


	/**
	 * 连接地址
	 */
	private String path;


	/**
	 * 组件名称
	 */
	private String component;


	/**
	 * 菜单类型
	 */
	@NotNull(message = "菜单类型不能为空,0(left),1(button),2(top)")
	private String type;

	/**
	 * 菜单图标
	 */
	private String icon;


	/**
	 * 父节点
	 */
	@NotNull(message = "菜单父ID不能为空")
	private Long parentId;


	/**
	 * 顺序
	 */
	private Float sort;


	/**
	 * 显示或隐藏,true为隐藏，false为不隐藏
	 */
	private Boolean hiddenFlag;

	/**
	 * 子菜单列表
	 */
	private List<AdminMenuDTO> children;

}
