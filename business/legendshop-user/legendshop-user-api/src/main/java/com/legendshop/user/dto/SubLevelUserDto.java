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
import java.util.Date;

/**
 * 分销下级用户DTO.
 *
 * @author legendshop
 */
@Data
public class SubLevelUserDto implements Serializable {

	private static final long serialVersionUID = 471562742691105059L;

	/**
	 * The user id.
	 */
	private Long userId;

	/**
	 * The user name.
	 */
	private String userName;

	/**
	 * The nick name.
	 */
	private String nickName;

	/**
	 * The user reg time.
	 */
	private Date userRegTime;

	/**
	 * The total dist commis.
	 */
	private Double totalDistCommis;
}
