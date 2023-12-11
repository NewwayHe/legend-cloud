/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 举报的用户类型，由接口指定对应的用户类型
 *
 * @author legendshop
 */
@Data
public class AccusationUserTypeDTO implements Serializable {

	/**
	 * 举报人ID
	 */
	private Long userId;

	/**
	 * 举报人类型（1、用户 2、平台）
	 */
	private Integer userType;

	/**
	 * 举报人名称
	 */
	private String userName;

}
