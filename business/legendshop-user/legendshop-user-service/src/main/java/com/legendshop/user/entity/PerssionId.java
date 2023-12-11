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
import cn.legendshop.jpaplus.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 权限Id
 *
 * @author legendshop
 */
@Embeddable
@Data
@AllArgsConstructor
public class PerssionId implements java.io.Serializable {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = -5510312063680243453L;

	/**
	 * 角色Id.
	 */
	@Column(name = "role_id")
	private Long roleId;

	/**
	 * 权限Id.
	 */
	@Column(name = "function_id")
	private Long functionId;


}
