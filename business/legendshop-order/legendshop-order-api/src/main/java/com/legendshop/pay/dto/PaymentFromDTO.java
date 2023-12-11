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
import java.util.List;

/**
 * @author legendshop
 */
@Data
@Schema(description = "付款参数")
public class PaymentFromDTO {

	@Schema(description = "用户ID")
	private Long userId;

	@Schema(description = "支付的业务单号集合")
	private List<String> businessOrderNumberList;

	/**
	 * 支付单据类型[订单支付:USER_ORDER,预付款充值:USER_RECHARGE]
	 * {@link com.legendshop.pay.enums.SettlementTypeEnum}
	 */
	@Schema(description = "支付单据类型[订单支付:USER_ORDER,预付款充值:USER_RECHARGE]")
	private String settlementType;

	@Schema(description = "商户订单号：dev生成 [ 支付单号、余额充值流水号等 ]")
	private String number;

	@Schema(description = "支付类型Id")
	private String payTypeId;

	@Schema(description = "待付款金额")
	private BigDecimal amount;

	@Schema(description = "支付信息描述")
	private String payDescription;

	@Schema(description = "支付详情")
	private String payDetail;

	@Schema(description = "支付Ip：移动端为用户请求ip，pc端为发起服务器ip")
	private String ip;

	@Schema(description = "支付来源：pc、mini、mp等")
	private VisitSourceEnum visitSource;

	@Schema(description = "易宝支付订单ID")
	private Long yeepayId;

	/**
	 * 使用钱包抵扣的记录
	 */
	private List<UserWalletDetailsDTO> useWalletList;
}
