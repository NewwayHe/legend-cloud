/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

/**
 * 用户配送地址(UserAddrSub)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_order_user_address")
public class OrderUserAddress implements GenericEntity<Long> {

	private static final long serialVersionUID = -94532312674902419L;

	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "USER_ADDR_ORDER_SEQ")
	private Long id;


	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private Long userId;


	/**
	 * 收货人名称
	 */
	@Column(name = "receiver")
	private String receiver;


	/**
	 * 收货手机号码
	 */
	@Column(name = "mobile")
	private String mobile;


	/**
	 * 省份ID
	 */
	@Column(name = "province_id")
	private Long provinceId;


	/**
	 * 城市ID
	 */
	@Column(name = "city_id")
	private Long cityId;


	/**
	 * 区域ID
	 */
	@Column(name = "area_id")
	private Long areaId;

	/**
	 * 街道ID
	 */
	@Column(name = "street_id")
	private Long streetId;


	/**
	 * 详细地址
	 */
	@Column(name = "detail_address")
	private String detailAddress;

}
