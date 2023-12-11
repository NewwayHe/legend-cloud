/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
public class RefundResultItemDTO {

	@Schema(description = "退款类型")
	private String payTypeId;

	@Schema(description = "成功退款金额")
	private BigDecimal amount;

	@Schema(description = "退款业务单号")
	private String externalRefundSn;

}
