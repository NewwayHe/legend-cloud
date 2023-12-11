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
 * 管理员用户表(AdminUser)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_admin_user")
public class AdminUser extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -15934681953183653L;

	/**
	 * 用户ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "ADMIN_USER_SEQ")
	private Long id;


	/**
	 * 名称
	 */
	@Column(name = "username")
	private String username;


	/**
	 * 密码
	 */
	@Column(name = "password")
	private String password;

	/**
	 * 用户头像
	 */
	@Column(name = "avatar")
	private String avatar;


	/**
	 * 部门ID
	 */
	@Column(name = "dept_id")
	private Long deptId;

	/**
	 * 删除标记,1:正常,0:已删除
	 */
	@Column(name = "del_flag")
	private Boolean delFlag;

	/**
	 * 删除标记,1:正常,0:已锁定
	 */
	@Column(name = "lock_flag")
	private Boolean lockFlag;
}
