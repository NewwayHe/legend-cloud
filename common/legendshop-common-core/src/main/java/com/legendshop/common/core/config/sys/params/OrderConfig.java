/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.common.core.config.sys.params;

import lombok.Data;

/**
 * 订单配置
 *
 * @author legendshop
 */
@Data
public class OrderConfig implements ParamsConfig {

	/**
	 * 自动确认收货时长
	 */
	private Long autoReceiveProductDay;

	/**
	 * 自动取消未支付订单时长
	 */
	private Long autoCancelUnPayOrderMinutes;

	/**
	 * 订单评论有效时间
	 */
	private Long orderCommentValidDay;

	/**
	 * 退换货有效时间
	 */
	private Long refundOrExchangeValidDay;
}
