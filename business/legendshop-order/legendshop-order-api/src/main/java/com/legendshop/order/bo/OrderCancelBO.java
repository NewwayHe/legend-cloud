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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 取消订单Bo
 *
 * @author legendshop
 */
@Schema(description = "取消订单bo")
@Data
public class OrderCancelBO implements Serializable {

	private static final long serialVersionUID = -174881782865256705L;

	@NotEmpty(message = "订单编号不能为空")
	@Schema(description = "订单编号集合")
	private List<String> orderNumbers;

	@NotBlank(message = "原因不能为空")
	@Schema(description = "原因")
	private String reason;
}
