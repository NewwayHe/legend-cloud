/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import com.legendshop.activity.enums.CouponSelectStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 优惠券项扩展信息DTO
 * 优惠劵基础信息，用劵过程中的信息
 *
 * @author legendshop
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponItemExtDTO extends CouponDTO implements Serializable {
	private static final long serialVersionUID = 3708524537924008389L;

	@Schema(description = "用户优惠券ID")
	private Long userCouponId;

	/**
	 * 选着状态
	 */
	@Schema(description = "优惠券选中状态，1，已选中，0，未选中，-1，不可选")
	private Integer selectStatus = CouponSelectStatusEnum.UN_OPTIONAL.getStatus();

	/**
	 * 不可用原因
	 */
	@Schema(description = "不可用原因")
	private String unAvailableReason;

}
