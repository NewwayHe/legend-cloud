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

/**
 * 分销下级用户数量DTO
 *
 * @author legendshop
 */
@Data
public class SubUserCountDto implements Serializable {

	private static final long serialVersionUID = 471562742691105059L;

	/**
	 * 直接下级用户数量
	 */
	private String firstCount;

	/**
	 * 下二级用户数量
	 */
	private String secondCount;

	/**
	 * 下三级用户数量
	 */
	private String thirdCount;

}
