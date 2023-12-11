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
 * 售后物流信息DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "售后物流信息")
public class RefundReturnLogisticsDTO implements Serializable {

	@Schema(description = "订单物流信息id")
	private Long id;
	/**
	 * 承运物流公司ID
	 */
	@Schema(description = "承运物流公司ID", hidden = true)
	private Long logisticsCompanyId;

	/**
	 * 承运物流公司
	 */
	@Schema(description = "承运物流公司")
	private String logisticsCompany;

	/**
	 * 运单号
	 */
	@Schema(description = "运单号")
	private String shipmentNumber;

	/**
	 * 物流追踪信息【JSON格式】
	 */
	@Schema(description = "物流追踪信息【JSON格式】")
	private String trackingInformation;
}
