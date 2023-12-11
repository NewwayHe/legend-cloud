/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author legendshop
 */
@Data
@Schema(description = "商家待处理订单信息数量BO")
public class OrderShopMessageNoticeBO implements Serializable {


	private static final long serialVersionUID = 3907276785422125077L;
	/**
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	private Long shopId;

	/**
	 * 未发货订单数量
	 */
	@Schema(description = "未发货订单数量")
	private Integer waitDelivery;


	/**
	 * 未处理售后订单数量
	 */
	@Schema(description = "未处理售后订单数量")
	private Integer refundCount;
}
