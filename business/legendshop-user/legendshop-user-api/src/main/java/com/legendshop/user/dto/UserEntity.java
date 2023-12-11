/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 用户实体
 *
 * @author legendshop
 */
@Data
public class UserEntity implements Serializable {

	/**
	 * The Constant serialVersionUID.
	 */
	private static final long serialVersionUID = -8401714272377570649L;

	/**
	 * The id.
	 */
	private Long id;

	/**
	 * The name.
	 */
	private String name;

	/**
	 * The password.
	 */
	private String password;

	/**
	 * The passwordag.
	 */
	private String passwordag;

	/**
	 * 用户状态 1：有效， 0：无效.
	 */
	private String enabled;

	/**
	 * The note.
	 */
	private String note;

	/**
	 * 用户商城等级
	 **/
	private Integer gradeId;

	/**
	 * 头像
	 **/
	private String portraitPic;

	/**
	 * 昵称
	 **/
	private String nickName;

	/**
	 * 部门Id
	 */
	private Integer deptId;

	private Long shopId;

	private String openId;

	private String shopName;

	/**
	 * 角色
	 **/
	private Collection<String> roles;

	/**
	 * 权限
	 **/
	private List<String> functions;

}
