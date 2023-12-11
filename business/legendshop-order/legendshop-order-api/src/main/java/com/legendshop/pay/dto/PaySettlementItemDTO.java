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
import java.util.Date;

/**
 * 支付单项
 *
 * @author legendshop
 */
@Data
public class PaySettlementItemDTO {


	private Long id;

	@Schema(description = "商户系统内部的支付单据号")
	private String paySettlementSn;

	@Schema(description = "业务编码：第三方返回")
	private String transactionSn;

	@Schema(description = "支付方式编号")
	private String payTypeId;

	@Schema(description = "第三方支付单金额")
	private BigDecimal amount;

	@Schema(description = "退款金额")
	private BigDecimal refundAmount;

	@Schema(description = "剩余未退款金额")
	private BigDecimal remainingAmount;

	/**
	 * {@link com.legendshop.pay.enums.PaySettlementStateEnum}
	 */
	@Schema(description = "支付项状态")
	private Integer state;

	@Schema(description = "创建时间")
	private Date createTime;

	@Schema(description = "更新时间")
	private Date updateTime;
}
