/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
@Builder
@Schema(description = "【后台】首页用户注册统计BO")
public class RegisterCountBO {

	/**
	 * 当天注册用户数量
	 */
	@Schema(description = "当天注册用户数量")
	private Integer userRegisterToday;

	/**
	 * 累计注册用户数量
	 */
	@Schema(description = "累计注册用户数量")
	private Integer userRegisterTotal;

	/**
	 * 当天注册商家数量
	 */
	@Schema(description = "当天注册商家数量")
	private Integer shopRegisterToday;

	/**
	 * 累计注册商家数量
	 */
	@Schema(description = "累计注册商家数量")
	private Integer shopRegisterTotal;
}
