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
 * 订单物流信息DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "订单物流信息")
public class OrderLogisticsDTO implements Serializable {

	private static final long serialVersionUID = -1046342065218087496L;

	/**
	 * 主键id
	 */
	@Schema(description = "主键id", hidden = true)
	private Long id;


	/**
	 * 订单ID
	 */
	@Schema(description = "订单ID", hidden = true)
	private Long orderId;

	/**
	 * 订单号
	 */
	@Schema(description = "订单号")
	private String orderNumber;

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

	@Schema(description = "物流公司编号根据快递100查询")
	private String companyCode;

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

	/**
	 * 物流状态
	 * QueryTrackStatusEnum
	 */
	@Schema(description = "物流状态")
	private String logisticsStatus;
}
