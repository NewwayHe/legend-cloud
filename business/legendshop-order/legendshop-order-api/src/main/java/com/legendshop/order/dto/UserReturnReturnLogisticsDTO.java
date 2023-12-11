/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户退款物流dto
 *
 * @author legendshop
 */
@Data
@Schema(description = "用户退款物流dto")
public class UserReturnReturnLogisticsDTO implements Serializable {
	private static final long serialVersionUID = -71568936819200509L;

	/**
	 * 退款id
	 */
	@Schema(description = "退款id")
	private Long refundId;

	/**
	 * 运单号
	 */
	@Schema(description = "运单号")
	private String logisticsNumber;

	/**
	 * 物流公司id
	 */
	@Schema(description = "物流公司id")
	private Long logisticsId;

	/**
	 * 物流公司名字
	 */
	@Schema(description = "物流公司")
	private String company;

	@Schema(hidden = true)
	private Long userId;
}
