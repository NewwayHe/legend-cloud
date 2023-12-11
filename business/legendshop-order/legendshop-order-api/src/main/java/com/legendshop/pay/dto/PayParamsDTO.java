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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 确认支付入参
 *
 * @author legendshop
 */
@Data
@Schema(description = "确认支付入参")
public class PayParamsDTO {

	@Schema(description = "支付的业务单号集合")
	private Long userId;

	/**
	 * 支付的业务单号集合
	 */
	@Schema(description = "支付的业务单号集合")
	@NotEmpty(message = "支付的业务单号集合")
	private List<String> businessOrderNumberList;

	/**
	 * 支付类型
	 */
	@Schema(description = "支付类型")
	@NotBlank(message = "支付类型不能为空")
	private String payTypeId;

	/**
	 * 支付单据类型[订单支付:USER_ORDER,预付款充值:USER_RECHARGE]
	 * {@link com.legendshop.pay.enums.SettlementTypeEnum}
	 */
	@Schema(description = "支付单据类型[订单支付:ORDINARY_ORDER,预付款充值:USER_RECHARGE,团购订单:GROUP_ORDER,秒杀订单:SEC_KILL_ORDER,拼团订单:MERGE_GROUP_ORDER]",
			allowableValues = "ORDINARY_ORDER,USER_RECHARGE,GROUP_ORDER,SEC_KILL_ORDER,MERGE_GROUP_ORDER")
	@NotBlank(message = "支付单据类型不能为空")
	private String settlementType;


	@Schema(description = "支付来源:PC,H5,MINI,MP,APP", allowableValues = "PC,H5,MINI,MP,APP")
	@NotNull(message = "支付来源不能为空")
	private VisitSourceEnum visitSource;


	@Schema(description = "支付Ip：移动端为用户请求ip，pc端为发起服务器ip", hidden = true)
	private String ip;

	@Schema(description = "是否使用钱包支付，TRUE：使用，FALSE：不使用")
	private Boolean useWallet;

	@Schema(description = "钱包支付参数")
	private WalletPayDTO walletPay;

}
