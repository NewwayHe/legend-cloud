/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 记录用户订单配送地址
 *
 * @author legendshop
 */
@Data
public class OrderUserAddressDTO implements Serializable {


	private static final long serialVersionUID = 2686042478262640046L;


	private Long id;

	/**
	 * 用户ID
	 */
	private Long userId;


	/**
	 * 收货人名称
	 */
	private String receiver;


	/**
	 * 收货手机号码
	 */
	private String mobile;


	/**
	 * 省份ID
	 */
	private Long provinceId;


	/**
	 * 城市ID
	 */
	private Long cityId;


	/**
	 * 区域ID
	 */
	private Long areaId;

	/**
	 * 街道ID
	 */
	private Long streetId;

	/**
	 * 地址别名
	 */
	private String aliasAddr;


	/**
	 * 详细地址
	 */
	private String detailAddress;


}
