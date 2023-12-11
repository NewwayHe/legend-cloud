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
import java.util.List;

/**
 * @author legendshop
 */
@Data
public class RefundResultDTO {

	@Schema(description = "成功退款的金额总和")
	private BigDecimal successRefundAmount;

	@Schema(description = "退款项")
	private List<RefundResultItemDTO> itemList;
}
