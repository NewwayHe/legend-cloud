/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.vo;

import com.legendshop.common.core.dto.BaseDTO;
import lombok.Data;

import java.util.List;

/**
 * @author legendshop
 */
@Data
public class AdminUserVO extends BaseDTO {


	/**
	 * 用户ID
	 */
	private Long id;


	/**
	 * 名称
	 */
	private String username;


	/**
	 * 部门ID
	 */
	private Integer deptId;

	/**
	 * 部门名称
	 */
	private String deptName;


	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 密码
	 */
	private String password;


	/**
	 * 删除状态
	 */
	private Boolean delFlag;

	/**
	 * 锁定标记
	 */
	private Boolean lockFlag;


	/**
	 * 角色列表
	 */
	private List<AdminRoleVO> roleList;


}
