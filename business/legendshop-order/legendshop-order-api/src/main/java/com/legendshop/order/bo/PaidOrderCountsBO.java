/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付订单数量BO
 *
 * @author legendshop
 */
@Data
@Schema(description = "支付订单数量BO")
@NoArgsConstructor
@AllArgsConstructor
public class PaidOrderCountsBO implements Serializable {

	private static final long serialVersionUID = 7421903970225895240L;

	/**
	 * 统计的订单数量
	 */
	@Schema(description = "支付订单数量")
	private Integer subCounts;

	/**
	 * 支付金额
	 */
	@Schema(description = "支付金额")
	private BigDecimal paidAmount;

	/**
	 * 日期
	 */
	@Schema(description = "日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date date;

	public PaidOrderCountsBO(Date date) {
		this.date = date;
		this.subCounts = 0;
		this.paidAmount = BigDecimal.valueOf(0);
	}
}
