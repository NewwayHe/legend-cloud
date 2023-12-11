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

import java.util.Date;

/**
 * 支付单日志表DTO【主要记录支付单异常记录，协助分析用户支付行为，以防支异常或攻击】(PaySettlementLog)
 *
 * @author legendshop
 * @since 2022-04-28 11:48:11
 */
@Data
public class PaySettlementLogDTO {

	private Long id;

	@Schema(description = "用户ID")
	private Long userId;

	/**
	 * 支付单号
	 */
	@Schema(description = "支付单号")
	private String paySettlementSn;

	/**
	 * 支付方式编号
	 */
	@Schema(description = "支付方式编号")
	private String payTypeId;

	/**
	 * 日志内容
	 */
	@Schema(description = "日志内容")
	private String info;

	/**
	 * 备注
	 */
	@Schema(description = "备注")
	private String remark;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;
}
