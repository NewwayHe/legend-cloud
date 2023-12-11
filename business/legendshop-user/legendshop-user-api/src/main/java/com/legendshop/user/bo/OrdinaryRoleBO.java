/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色BO
 *
 * @author legendshop
 */
@Data
@Schema(description = "角色bo")
public class OrdinaryRoleBO implements Serializable {

	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long id;


	/**
	 * 角色名称
	 */
	@Schema(description = "角色名称")
	private String roleName;

	/**
	 * 角色表示和编码  ROLE_ADMIN、ROLE_USER
	 */
	@Schema(description = "角色编码")
	private String roleCode;

	/**
	 * 角色描述
	 */
	@Schema(description = "角色描述")
	private String roleDesc;

	/**
	 * 状态
	 */
	@Schema(description = "状态（有效、无效）")
	private Boolean delFlag;


	/**
	 * 关联状态
	 */
	@Schema(description = "关联状态")
	private Boolean selectFlag = false;

}
