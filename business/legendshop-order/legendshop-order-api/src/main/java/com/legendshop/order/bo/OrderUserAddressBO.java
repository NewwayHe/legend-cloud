/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.bo;


import lombok.Data;

import java.io.Serializable;

/**
 * 记录用户订单配送地址
 *
 * @author legendshop
 */
@Data
public class OrderUserAddressBO extends OrderBO implements Serializable {


	private static final long serialVersionUID = 2686042478262640046L;


	private Long id;

	/**
	 * 用户ID
	 */
	private Long userId;


	/**
	 * 接受人名称
	 */
	private String receiver;


	/**
	 * 省份ID
	 */
	private Integer provinceId;


	/**
	 * 省份
	 */
	private Integer province;


	/**
	 * 城市ID
	 */
	private Integer cityId;

	/**
	 * 城市
	 */
	private Integer city;


	/**
	 * 区域ID
	 */
	private Integer areaId;

	/**
	 * 区域
	 */
	private Integer area;


	/**
	 * 详细地址
	 */
	private String detailAddress;


	/**
	 * 手机
	 */
	private String mobile;


}
