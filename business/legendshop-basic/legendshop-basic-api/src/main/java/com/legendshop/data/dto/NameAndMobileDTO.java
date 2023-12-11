/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dto;

import lombok.Data;

/**
 * 昵称和手机号DTO
 *
 * @author legendshop
 */
@Data
public class NameAndMobileDTO {

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 手机号
	 */
	private String mobile;
}
