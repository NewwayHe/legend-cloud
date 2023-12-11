/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;


import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 包邮活动表(ShippingActive)实体类
 *
 * @author legendshop
 */
@Data
public class ShippingActiveDTO implements Serializable {


	private static final long serialVersionUID = 3668910604631637231L;
	/**
	 * 活动ID
	 */
	private Long id;


	/**
	 * 商家ID
	 */
	private Long shopId;


	/**
	 * 活动名称
	 */
	private String name;


	/**
	 * 状态有效:1 下线：0
	 */
	private Integer status;


	/**
	 * 店铺:0,商品:1
	 */
	private Integer fullType;


	/**
	 * 类型1：元 2 件
	 */
	private Integer reduceType;


	/**
	 * 满足的金额或件数 full_value
	 */
	private BigDecimal fullValue;


	/**
	 * 优惠信息，用于活动列表展示
	 */
	private String description;


	/**
	 * 开始时间
	 */
	private Date startDate;


	/**
	 * 结束时间
	 */
	private Date endDate;


	/**
	 * 创建时间
	 */
	private Date createDate;

}
