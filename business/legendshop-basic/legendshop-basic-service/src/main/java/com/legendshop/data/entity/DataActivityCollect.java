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
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 营销活动汇总表(DataActivityCollect)实体类
 *
 * @author legendshop
 * @since 2021-06-30 20:35:08
 */
@Data
@Entity
@Table(name = "ls_data_activity_collect")
public class DataActivityCollect implements GenericEntity<Long> {

	private static final long serialVersionUID = -53076789368244931L;

	/**
	 * ID
	 */
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "dataActivityCollect_SEQ")
	private Long id;

	/**
	 * 活动id
	 */
	@Column(name = "activity_id")
	private Long activityId;

	/**
	 * 成交金额
	 */
	@Column(name = "deal_amount")
	private BigDecimal dealAmount;

	/**
	 * 成交商品数
	 */
	@Column(name = "deal_number")
	private Integer dealNumber;

	/**
	 * 成交新用户数
	 */
	@Column(name = "deal_new_user")
	private Integer dealNewUser;

	/**
	 * 成交旧用户数
	 */
	@Column(name = "deal_old_user")
	private Integer dealOldUser;

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
	 * 创建时间
	 */
	@Column(name = "create_time")
	private Date createTime;

	/**
	 * 活动状态 -2：拒绝  -1：失效  1：成功  2：失败
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 活动类型
	 */
	@Column(name = "type")
	private String type;

	@Column(name = "deal_user_num")
	private Integer dealUserNum;

	/**
	 * 店铺id
	 */
	@Column(name = "shop_id")
	private Long shopId;

	/**
	 * 审核状态  -1: 拒绝  0:待审核 1:通过
	 */
	@Column(name = "op_status")
	private Integer opStatus;

	/**
	 * 活动状态
	 */
	@Column(name = "activity_status")
	private Integer activityStatus;
}
