/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户配送地址(UserAddr)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_user_address")
public class UserAddress implements GenericEntity<Long> {

	private static final long serialVersionUID = -28716747785460097L;

	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "USER_ADDRESS_SEQ")
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
	 * 详情地址
	 */
	@Column(name = "detail_address")
	private String detailAddress;


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
	 * 手机
	 */
	@Column(name = "mobile")
	private String mobile;


	/**
	 * 是否常用地址
	 */
	@Column(name = "common_flag")
	private Boolean commonFlag;


	/**
	 * 建立时间
	 */
	@Column(name = "create_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;


	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;


}
