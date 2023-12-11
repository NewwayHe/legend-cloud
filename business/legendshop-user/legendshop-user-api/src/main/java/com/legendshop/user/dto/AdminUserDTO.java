/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;


import com.legendshop.common.core.dto.BaseDTO;
import com.legendshop.user.vo.AdminRoleVO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

/**
 * 管理员用户表(AdminUser)实体类
 *
 * @author legendshop
 */
@Data
public class AdminUserDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -15934681953183653L;


	/**
	 * 名称
	 */
	@NotBlank(message = "管理员用户名不能为空")
	@Length(max = 10, min = 1, message = "管理员用户名长度在1~10之间")
	private String username;


	/**
	 * 部门ID
	 */
	private Long deptId;


	/**
	 * 头像
	 */
	@Length(max = 255, min = 0, message = "头像长度在0~255之间")
	private String avatar;

	/**
	 * 密码
	 */
	@Length(max = 16, min = 5, message = "管理员用户名长度在5-16之间")
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
	 * 角色ID
	 */
	private List<Long> roleIdList;


	/**
	 * 新密码
	 */
	private String newPassword;

	/**
	 * 操作人id
	 */
	private Long operatorId;

	/**
	 * 操作人名称
	 */
	private String operatorName;

	/**
	 * 操作人ip
	 */
	private String clientIP;

	/**
	 * 请求来源
	 */
	private String source;

	/**
	 * 角色列表
	 */
	private List<AdminRoleVO> roleList;

	/**
	 * 部门名称
	 */
	private String deptName;
}
