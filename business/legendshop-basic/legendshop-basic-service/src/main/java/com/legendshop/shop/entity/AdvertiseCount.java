/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * (AdvertiseCount)实体类
 *
 * @author legendshop
 * @since 2022-04-27 17:21:39
 */
@Data
@Entity
@Table(name = "ls_advertise_count")
public class AdvertiseCount implements GenericEntity<Long> {

	private static final long serialVersionUID = -35829138569621270L;


	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "advertiseCount_SEQ")
	private Long id;

	/**
	 * 广告id
	 */
	@Column(name = "advertise_id")
	private Long advertiseId;


	/**
	 * 投放人数
	 */
	@Column(name = "put_people")
	private BigDecimal putPeople;

	/**
	 * 投放次数
	 */
	@Column(name = "put_count")
	private BigDecimal putCount;

	/**
	 * 点击人数
	 */
	@Column(name = "click_people")
	private BigDecimal clickPeople;

	/**
	 * 点击次数
	 */
	@Column(name = "click_count")
	private BigDecimal clickCount;

	/**
	 * 渠道来源
	 */
	@Column(name = "source")
	private String source;

	/**
	 * 日期
	 */
	@Column(name = "create_time")
	private Date createTime;


}
