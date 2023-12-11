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
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 申请订单全额退款
 *
 * @author legendshop
 * @create: 2021-08-27 15:22
 */
@Data
@Schema(description = "申请订单全额退款")
@Accessors(chain = true)
public class ApplyOrderRefundDTO implements Serializable {

	private static final long serialVersionUID = 3146386750559608567L;

	@NotNull(message = "订单ID不能为空")
	@Schema(description = "订单id")
	private Long orderId;

	@NotNull(message = "退款原因不能为空")
	@Schema(description = "退款原因")
	private String reason;

	@Schema(description = "退款说明")
	private String buyerMessage;

	@Schema(description = "用户ID", hidden = true)
	private Long userId;
}
