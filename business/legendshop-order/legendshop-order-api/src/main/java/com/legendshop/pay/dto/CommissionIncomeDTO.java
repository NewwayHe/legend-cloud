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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author legendshop
 */
@Data
public class CommissionIncomeDTO implements Serializable {

	@Schema(description = "收益名目")
	private String name;

	@Schema(description = "金额")
	private BigDecimal amount;

	@Schema(description = "时间")
	private Date incomeDate;

}
