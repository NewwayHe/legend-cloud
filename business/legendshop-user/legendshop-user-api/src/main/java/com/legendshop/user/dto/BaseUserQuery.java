/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author legendshop
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "查询用户基本信息")
public class BaseUserQuery {

	@Schema(description = "用户Id")
	private Long userId;

	@Schema(description = "用户类型")
	private String userType;

	@Schema(description = "用户手机号")
	private String mobile;

	public BaseUserQuery(Long userId, String userType) {
		this.userId = userId;
		this.userType = userType;
	}

	public BaseUserQuery(String mobile, String userType) {
		this.userType = userType;
		this.mobile = mobile;
	}
}
