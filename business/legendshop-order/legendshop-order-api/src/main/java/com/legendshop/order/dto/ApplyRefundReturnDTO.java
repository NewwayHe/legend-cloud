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
import java.util.List;

/**
 * 申请退款页面数据
 *
 * @author legendshop
 * @create: 2020-12-23 16:57
 */
@Data
public class ApplyRefundReturnDTO implements Serializable {

	private static final long serialVersionUID = 6769747979475047328L;

	@Schema(description = "退款子项")
	private List<ApplyRefundReturnItemDTO> orderItemList;

	@Schema(description = "退款原因")
	private List<String> refundReasonList;
}
