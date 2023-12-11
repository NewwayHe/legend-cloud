/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 经纬度
 *
 * @author legendshop
 */
@Data
public class LngAndLatBO implements Serializable {

	private static final long serialVersionUID = -12262838602703236L;

	/**
	 * 经度
	 */
	private String longitude;

	/**
	 * 维度
	 */
	private String Latitude;

	/**
	 * 省
	 */
	private String province;

	/**
	 * 省(locationID)
	 */
	private Long provinceCode;

	/**
	 * 市
	 */
	private String city;

	/**
	 * 市(locationID)
	 */
	private Long cityCode;

	/**
	 * 区
	 */
	private String district;

	/**
	 * 区(locationID)
	 */
	private Long districtCode;

	/**
	 * 街道
	 */
	private String township;

	/**
	 * 区(locationID)
	 */
	private Long townshipCode;

	/**
	 * 完整地址
	 */
	private String formattedAddress;

}
