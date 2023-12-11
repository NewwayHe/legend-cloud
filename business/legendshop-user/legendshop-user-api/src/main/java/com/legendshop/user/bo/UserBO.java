/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.bo;


import lombok.Data;

import java.io.Serializable;

/**
 * 用户业务实体
 *
 * @author legendshop
 */
@Data
public class UserBO implements Serializable {

	private static final long serialVersionUID = 672473978338963121L;

	private Long id;


	/**
	 * 名称
	 */
	private String name;


	/**
	 * 密码
	 */
	private String password;


	/**
	 * 状态
	 */
	private String enabled;


	/**
	 * 注释
	 */
	private String note;


	/**
	 * 部门ID
	 */
	private Long deptId;


	/**
	 * 微信openid
	 */
	private String openId;

}
