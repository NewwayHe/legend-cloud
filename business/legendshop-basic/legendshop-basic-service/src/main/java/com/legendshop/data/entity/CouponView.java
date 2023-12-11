/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * (CouponView)实体类
 *
 * @author legendshop
 * @since 2022-04-06 11:49:50
 */
@Data
@Entity
@Table(name = "ls_coupon_view")
public class CouponView extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -11837541865920627L;


	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "couponView_SEQ")
	private Long id;

	/**
	 * 优惠券id
	 */
	@Column(name = "coupon_id")
	private Integer couponId;

	/**
	 * 访问人数
	 */
	@Column(name = "view_people")
	private Integer viewPeople;

	/**
	 * 访问次数
	 */
	@Column(name = "view_frequency")
	private Integer viewFrequency;

	/**
	 * 来源
	 */
	@Column(name = "source")
	private String source;

	/**
	 * 访问时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	private Date updateTime;

	/**
	 * 优惠券类型
	 */
	/*@Column(name = "coupon_type")
	private Date couponType;*/

}
