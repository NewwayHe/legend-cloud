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
import lombok.Data;

/**
 * @author legendshop
 */
@Data
public class SysUserDTO extends BaseDTO {

	/**
	 * 名称
	 */
	private String username;

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 店铺id
	 */
	private Long shopId;

	/**
	 * 店铺用户ID
	 */
	private Long shopUserId;

	/**
	 * 密码
	 */
	private String password;


	/**
	 * 删除标记,1:正常,0:已删除
	 */
	private Boolean delFlag;


	/**
	 * 删除标记,1:正常,0:已锁定
	 */
	private Boolean lockFlag;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 部门ID
	 */
	private Long deptId;

}
