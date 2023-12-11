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
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_ordinary_role_menu")
public class OrdinaryRoleMenu implements GenericEntity<Long> {

	private static final long serialVersionUID = 5446179648587994177L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "ORDINARY_ROLE_MENU_SEQ")
	private Long id;

	@Column(name = "role_id")
	private Long roleId;

	@Column(name = "menu_id")
	private Long menuId;

}
