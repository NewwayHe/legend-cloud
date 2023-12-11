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
import lombok.Data;

import java.io.Serializable;

/**
 * 角色DTO
 *
 * @author legendshop
 */
@Data
public class AdminRoleDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -8869410262743900621L;

	/**
	 * 主键
	 */
	private Long id;


	/**
	 * 角色名称
	 */
	@NotBlank(message = "角色名称不能为空")
	private String roleName;

	/**
	 * 角色表示和编码  ROLE_ADMIN、ROLE_USER
	 */
	private String roleCode;


	/**
	 * 角色描述
	 */
	private String roleDesc;

	/**
	 * 状态
	 */
	private Boolean delFlag;

}
