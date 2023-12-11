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
 * 角色表(ls_shop_user_role)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_shop_user_role")
public class ShopUserRole implements GenericEntity<Long> {
	private static final long serialVersionUID = -5946708288552834158L;
	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SHOP_ROLE_ROLE_SEQ")
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "role_id")
	private Long roleId;

	public ShopUserRole() {
	}

	public ShopUserRole(Long userId, Long roleId) {
		this.userId = userId;
		this.roleId = roleId;
	}
}
