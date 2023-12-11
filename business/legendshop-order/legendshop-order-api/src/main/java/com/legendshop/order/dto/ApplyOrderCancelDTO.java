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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author legendshop
 */
@Data
@Schema(description = "申请订单取消订单")
public class ApplyOrderCancelDTO {

	@Schema(description = "取消原因", required = true)
	@NotEmpty(message = "取消原因不能为空")
	private String reason;

	/**
	 * 店铺id
	 */
	private Long shopId;

	@Length(min = 0, max = 50, message = "审核意见字数过多!")
	@Schema(description = "说明")
	private String sellerMessage;

	/**
	 * 订单id
	 */
	@NotNull(message = "订单id不能为空")
	@Schema(description = "订单id")
	private Long orderId;


	@Schema(description = "操作人")
	private String operator;


}
