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
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

/**
 * 角色表(Role)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_admin_role")
public class AdminRole extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 257081580826060539L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "ROLE_SEQ")
	private Long id;


	/**
	 * 角色名称
	 */
	@Column(name = "role_name")
	private String roleName;

	/**
	 * 角色表示和编码  ROLE_ADMIN、ROLE_USER
	 */
	@Column(name = "role_code")
	private String roleCode;

	/**
	 * 角色描述
	 */
	@Column(name = "role_desc")
	private String roleDesc;

	/**
	 * 状态
	 */
	@Column(name = "del_flag")
	private Boolean delFlag;
}
