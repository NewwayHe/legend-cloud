/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 易宝支付订单信息(YeepayOrder)BO
 *
 * @author legendshop
 * @since 2021-03-26 15:59:28
 */
@Data
public class YeepayOrderBO implements Serializable {

	private static final long serialVersionUID = -93036991766135754L;

	/**
	 * ID
	 */
	@Schema(description = "ID")
	private Long id;

	/**
	 * 易宝收款订单号
	 */
	@Schema(description = "易宝收款订单号")
	private String uniqueOrderNo;

	/**
	 * 订单状态
	 */
	@Schema(description = "订单状态")
	private String token;

	/**
	 * 发起方商编
	 */
	@Schema(description = "发起方商编")
	private String parentMerchantNo;

	/**
	 * 商户编号
	 */
	@Schema(description = "商户编号")
	private String merchantNo;

	/**
	 * 支付流水号（内部编号）
	 */
	@Schema(description = "支付流水号（内部编号）")
	private String paySettlementSn;

	/**
	 * 渠道侧商户请求号
	 */
	@Schema(description = "渠道侧商户请求号")
	private String bankOrderId;

	/**
	 * 预支付标识信息
	 */
	@Schema(description = "预支付标识信息")
	private String prePayIn;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

	/**
	 * 修改时间
	 */
	@Schema(description = "修改时间")
	private Date updateTime;

}
