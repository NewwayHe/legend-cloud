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
import lombok.Data;

/**
 * 用户角色对应表(UserRole)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_ordinary_user_role")
public class OrdinaryUserRole implements GenericEntity<Long> {

	private static final long serialVersionUID = 2224631577585461239L;
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "ORDINARY_USER_ROLE_SEQ")
	private Long id;

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

	public OrdinaryUserRole() {
	}

	public OrdinaryUserRole(Long userId, Long roleId) {
		this.userId = userId;
		this.roleId = roleId;
	}
}
