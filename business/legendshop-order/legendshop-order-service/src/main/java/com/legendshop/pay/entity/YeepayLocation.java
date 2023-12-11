/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

/**
 * 易宝支付地区编码表(YeepayLocation)实体类
 *
 * @author legendshop
 * @since 2021-04-08 14:28:07
 */
@Data
@Entity
@Table(name = "ls_yeepay_location")
public class YeepayLocation implements GenericEntity<Long> {

	private static final long serialVersionUID = -88691170738817257L;

	/**
	 * ID
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "yeepayLocation_SEQ")
	private Long id;

	/**
	 * 省编码
	 */
	@Column(name = "province_code")
	private String provinceCode;

	/**
	 * 省名称
	 */
	@Column(name = "province_name")
	private String provinceName;

	/**
	 * 市编码
	 */
	@Column(name = "city_code")
	private String cityCode;

	/**
	 * 市名称
	 */
	@Column(name = "city_name")
	private String cityName;

	/**
	 * 区编码
	 */
	@Column(name = "area_code")
	private String areaCode;

	/**
	 * 区名称
	 */
	@Column(name = "area_name")
	private String areaName;

}
