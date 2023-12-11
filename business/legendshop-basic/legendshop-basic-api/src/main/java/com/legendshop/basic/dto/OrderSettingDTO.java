/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单设置Dto
 *
 * @author legendshop
 */
@Data
public class OrderSettingDTO implements Serializable {

	private static final long serialVersionUID = 926080135256523287L;

	/**
	 * 订单评论有效时长
	 */
	private Integer orderCommentValidDay;

	/**
	 * 订单评论是否需要审核
	 */
	private Boolean commentNeedReview;

	/**
	 * 自动确认收货时长
	 */
	private Integer autoReceiveProductDay;

	/**
	 * 商家自动确认收货时长
	 */
	private Long shopAutoReceiveProductDay;

	/**
	 * 自动取消超时未支付订单
	 */
	private Integer autoCancelUnPayOrderMinutes;

	/**
	 * 自动同意售后时长
	 */
	private Integer autoAgreeRefundDay;

	/**
	 * 允许退换货时长
	 */
	private Integer refundOrExchangeValidDay;

	/**
	 * 自动取消售后时长
	 */
	private Integer autoCancelRefundDay;

	/**
	 * 平台自动审核售后单
	 */
	private Boolean automaticAuditAfterSalesOrder;


}
