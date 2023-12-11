/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.vo;


import lombok.Data;

import java.io.Serializable;

/**
 * 角色DTO
 *
 * @author legendshop
 */
@Data
public class AdminRoleVO implements Serializable {

	private static final long serialVersionUID = -8869410262743900621L;

	/**
	 * 主键
	 */
	private String id;


	/**
	 * 角色名称
	 */
	private String roleName;

	/**
	 * 角色表示和编码  ROLE_ADMIN、ROLE_USER
	 */
	private String roleCode;


}
