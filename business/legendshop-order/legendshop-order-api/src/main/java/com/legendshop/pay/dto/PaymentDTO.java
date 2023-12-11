/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;

import com.legendshop.common.core.enums.VisitSourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
@Schema(description = "付款参数")
public class PaymentDTO {

	@Schema(description = "商户订单号：dev生成 [ 支付单号、余额充值流水号等 ]")
	private String number;

	@Schema(description = "支付类型Id")
	private String payTypeId;

	@Schema(description = "付款金额")
	private BigDecimal amount;

	@Schema(description = "支付信息描述")
	private String payDescription;

	@Schema(description = "支付详情")
	private String payDetail;

	@Schema(description = "支付Ip：移动端为用户请求ip，pc端为发起服务器ip")
	private String ip;

	@Schema(description = "支付来源：pc、mini、mp等")
	private VisitSourceEnum visitSource;

	@Schema(description = "是否使用钱包余额，TRUE：使用，FALSE：未使用, (默认未使用)")
	private Boolean walletPay = Boolean.FALSE;

	@Schema(description = "钱包余额抵扣金额")
	private BigDecimal walletPayAmount;

	@Schema(description = "易宝支付订单ID")
	private Long yeepayId;

}
