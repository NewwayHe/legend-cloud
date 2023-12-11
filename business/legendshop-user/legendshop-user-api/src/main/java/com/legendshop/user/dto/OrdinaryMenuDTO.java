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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
@Schema(description = "普通用户菜单权限DTO")
public class OrdinaryMenuDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 2063399983267954393L;
	@Schema(description = "主键")
	private Long id;

	@Schema(description = "名称")
	private String name;

	@Schema(description = "权限")
	private String permission;

	/**
	 * 菜单描述
	 */
	@Length(max = 20, message = "菜单描述不能超过20个字")
	@Schema(description = "菜单描述")
	private String menuDesc;

	@Schema(description = "连接地址")
	private String path;

	@Schema(description = "组件名称")
	private String component;

	@Schema(description = "图标")
	private String icon;

	@Schema(description = "菜单类型")
	private String type;

	@Schema(description = "父节点")
	private Long parentId;

	@Schema(description = "顺序")
	private Float sort;

	@Schema(description = "显示或隐藏,true为隐藏，false为不隐藏")
	private Boolean hiddenFlag;
}
