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
 * 用户角色对应表(UserRole)实体类
 *
 * @author legendshop
 */
@Data
public class UserRoleDTO implements Serializable {


	private static final long serialVersionUID = 8028847962468311308L;


	/**
	 * 用户ID
	 */
	private Long userId;


	/**
	 * 角色ID
	 */
	private Integer roleId;

}
