/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
@Schema(description = "拍卖保证金列表")
public class ShopOrderBillAuctionDepositDTO implements Serializable {

	private static final long serialVersionUID = -3153609346856445L;

	@Schema(description = "订单ID")
	private Long orderId;

	@Schema(description = "活动名称")
	private String activityName;

	@Schema(description = "用户名")
	private String userName;

	@Schema(description = "保证金金额")
	private BigDecimal marginAmount;

	@Schema(description = "支付时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date payTime;

}
