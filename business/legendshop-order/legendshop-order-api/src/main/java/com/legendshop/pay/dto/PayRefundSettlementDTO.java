/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;

import com.legendshop.common.core.dto.BaseDTO;
import com.legendshop.pay.enums.PayRefundStateEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (PayRefundSettlement)DTO
 *
 * @author legendshop
 * @since 2021-05-12 18:09:17
 */
@Data
@Schema(description = "DTO")
@NoArgsConstructor
@AllArgsConstructor
public class PayRefundSettlementDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -96850138709360227L;

	private Long id;

	@Schema(description = "对应支付单，支付金额")
	private BigDecimal payAmount;

	/**
	 * 支付退款流水号
	 */
	@Schema(description = "支付退款流水号")
	private String payRefundSn;

	/**
	 * 订单号
	 */
	@Schema(description = "订单号")
	private String orderNumber;

	@Schema(description = "退款金额")
	private BigDecimal refundAmount;

	@Schema(description = "支付退款类型")
	private String payRefundType;

	@Schema(description = "支付来源")
	private String paySource;

	@Schema(description = "状态")
	private Integer state;

	@Schema(description = "支付单号")
	private String settlementSn;

	@Schema(description = "退款单号")
	private String refundSn;

	@Schema(description = "外部退款单号")
	private String externalRefundSn;

	@Schema(description = "退款结果描述")
	private String resultDesc;

	@Schema(description = "创建时间")
	private Date createTime;

	@Schema(description = "修改时间")
	private Date updateTime;

	public PayRefundSettlementDTO(RefundItemDTO item) {

		this.refundSn = item.getRefundSn();
		this.payAmount = item.getPayAmount();
		this.settlementSn = item.getNumber();
		this.refundAmount = item.getRefundAmount();
		this.payRefundType = item.getPayTypeId();
		this.state = PayRefundStateEnum.DEFAULT.getCode();
		this.createTime = new Date();
		this.updateTime = new Date();

	}
}
