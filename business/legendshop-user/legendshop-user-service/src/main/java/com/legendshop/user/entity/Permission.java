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
import lombok.Data;

import java.io.Serializable;

/**
 * 权限表(Perm)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_permission")
public class Permission implements Serializable {

	private static final long serialVersionUID = 389031862188185397L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	private PerssionId id;


	/**
	 * default constructor.
	 */
	public Permission() {
	}

	public Permission(Long roleId, Long functionId) {
		id = new PerssionId(roleId, functionId);
	}

}
