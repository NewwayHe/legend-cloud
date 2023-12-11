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
import lombok.Data;

import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "登陆历史统计")
public class LoginHistoryCountDTO {


	/**
	 * 用户Id
	 */
	@Schema(description = "用户Id")
	private Long userId;


	/**
	 * 昵称
	 */
	@Schema(description = "昵称")
	private String nickName;


	/**
	 * 登录次数
	 */
	@Schema(description = "登录次数")
	private Integer frequency;


	/**
	 * 最后登录时间
	 */
	@Schema(description = "最后登录时间")
	private Date time;
}
