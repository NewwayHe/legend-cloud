/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (CouponView)BO
 *
 * @author legendshop
 * @since 2022-04-06 11:49:52
 */
@Data
public class CouponViewBO implements Serializable {

	private static final long serialVersionUID = 779481672581939891L;

	private Long id;

	/**
	 * 优惠券id
	 */
	@Schema(description = "优惠券id")
	private Integer couponId;

	/**
	 * 访问人数
	 */
	@Schema(description = "访问人数")
	private Integer viewPeople;

	/**
	 * 访问次数
	 */
	@Schema(description = "访问次数")
	private Integer viewFrequency;

	/**
	 * 来源
	 */
	@Schema(description = "来源")
	private String source;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	private Date updateTime;

}
