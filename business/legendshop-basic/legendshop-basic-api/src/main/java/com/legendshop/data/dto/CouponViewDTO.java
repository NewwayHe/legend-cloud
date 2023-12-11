/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.data.dto;

import com.legendshop.common.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * (CouponView)DTO
 *
 * @author legendshop
 * @since 2022-04-06 11:49:52
 */
@Data
@Schema(description = "优惠券访问记录")

public class CouponViewDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -40833171464379091L;

	private Long id;

	/**
	 * 优惠券id
	 */
	@Schema(description = "优惠券id")
	private Long couponId;

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
	 * 访问时间
	 */
	@Schema(description = "访问时间")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	private Date updateTime;
	/**
	 * userid
	 */
	@Schema(description = "用户id")
	private Long userId;


	/**
	 * 优惠券类型
	 */
//	@Schema(description = "优惠券类型")
	//private Date couponType;
}
