/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import cn.legendshop.jpaplus.persistence.Column;
import cn.legendshop.jpaplus.persistence.Embeddable;
import cn.legendshop.jpaplus.persistence.Id;

/**
 * 用户角色ID.
 *
 * @author legendshop
 */
@Embeddable
public class UserRoleId implements java.io.Serializable {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = 5614770818903175975L;

	/**
	 * The user id.
	 */
	private Long userId;

	/**
	 * The role id.
	 */
	private String roleId;

	private String appNo;

	/**
	 * Instantiates a new user role id.
	 */
	public UserRoleId() {
	}

	/**
	 * Instantiates a new user role id.
	 *
	 * @param userId the user id
	 * @param roleId the role id
	 */
	public UserRoleId(Long userId, String roleId, String appNo) {
		this.userId = userId;
		this.roleId = roleId;
		this.appNo = appNo;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	@Id
	@Column(name = "user_id")
	public Long getUserId() {
		return this.userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Gets the role id.
	 *
	 * @return the role id
	 */
	@Column(name = "role_id")
	public String getRoleId() {
		return roleId;
	}

	/**
	 * Sets the role id.
	 *
	 * @param roleId the new role id
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}


	@Column(name = "app_no")
	public String getAppNo() {
		return appNo;
	}

	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appNo == null) ? 0 : appNo.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}
}
