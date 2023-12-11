/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.entity;


import cn.legendshop.jpaplus.persistence.Column;
import cn.legendshop.jpaplus.persistence.Entity;
import cn.legendshop.jpaplus.persistence.Table;
import lombok.Data;

/**
 * 用户角色对应表(UserRole)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_admin_user_role")
public class AdminUserRole {

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;


	/**
	 * 角色ID
	 */
	@Column(name = "role_id")
	private Long roleId;

}
