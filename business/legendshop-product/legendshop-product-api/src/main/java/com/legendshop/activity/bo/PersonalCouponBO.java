/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
public class PersonalCouponBO implements Serializable {

	private static final long serialVersionUID = 3241764358214699188L;

	/**
	 * 用户礼券ID
	 */
	private long userCouponId;

	/**
	 * 礼券ID
	 */
	private long couponId;

	/**
	 * 礼券名称
	 */
	private String couponName;

	/**
	 * 券号
	 */
	private String couponSn;

	/**
	 * 消费者ID
	 */
	private Long userId;

	/**
	 * 满多少钱可以使用
	 */
	private double fullPrice;

	/**
	 * 优惠多少钱
	 */
	private double offPrice;

	/**
	 * 礼券图片
	 */
	private String couponPicture;

	/**
	 * 领取时间
	 */
	private Date getTime;

	/**
	 * 开始时间
	 */
	private Date startDate;

	/**
	 * 结束时间
	 */
	private Date endDate;

	/**
	 * 使用时间
	 */
	private Date useTime;

	/**
	 * 使用状态
	 */
	private int useStatus;

	/**
	 * 礼券状态
	 */
	private int status;

	/**
	 * 礼券提供方
	 */
	private String couponProvider;

	/**
	 * 礼券类型
	 */
	private String couponType;

	/**
	 * 领取方式
	 */
	private String getType;

	/**
	 * 商家店名
	 */
	private String siteName;

	/**
	 * 商家Id
	 */
	private Long shopId;
}
