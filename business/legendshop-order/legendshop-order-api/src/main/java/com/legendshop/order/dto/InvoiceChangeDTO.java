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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 确认订单更改发票信息入参DTO
 *
 * @author legendshop
 */
@Data
@Schema(description = "确认订单更改发票信息入参")
public class InvoiceChangeDTO {

	@Schema(description = "预订单ID", required = true)
	@NotBlank(message = "预订单ID不能为空")
	private String confirmOrderId;

	@Schema(description = "商家ID", required = true)
	@NotNull(message = "商家ID不能为空")
	private Long shopId;

	@Schema(description = "用户开启发票标识", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull(message = "用户开启发票标识不能为空")
	private Boolean invoiceFlag;

	@Schema(description = "用户选择的发票ID,如果只是用户开启关闭发票标识，则不需要传")
	private Long invoiceId;

	@Schema(description = "用户ID", hidden = true)
	private Long userId;


}
