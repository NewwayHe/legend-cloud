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

import java.io.Serializable;

/**
 * 角色和菜单的关系表
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_admin_role_menu")
public class AdminRoleMenu implements Serializable {

	private static final long serialVersionUID = -2411471746246321328L;
	@Column(name = "user_id")
	private Long roleId;

	@Column(name = "user_id")
	private Long menuId;
}
