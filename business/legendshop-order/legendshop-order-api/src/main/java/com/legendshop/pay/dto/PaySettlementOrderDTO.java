/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;


import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

/**
 * 支付单订单关联DTO
 *
 * @author legendshop
 */
@Data
public class PaySettlementOrderDTO implements GenericEntity<Long> {

	private static final long serialVersionUID = -87217805630866838L;

	/**
	 * 主键ID
	 */
	private Long id;

	/**
	 * 商户系统内部的支付单据号
	 */
	private String paySettlementSn;


	/**
	 * 订单流水号
	 */
	private String orderNumber;


	/**
	 * 用户ID
	 */
	private Long userId;

}
