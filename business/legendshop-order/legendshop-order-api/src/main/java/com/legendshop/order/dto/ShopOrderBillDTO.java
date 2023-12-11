/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.legendshop.common.core.serialize.BigDecimalSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商家订单结算表(ShopOrderBill)实体类
 *
 * @author legendshop
 */
@Data
@Schema(description = "结算账单DTO")
public class ShopOrderBillDTO implements Serializable {


	private static final long serialVersionUID = 2498385003161594762L;
	/**
	 * ID
	 */
	private Long id;


	/**
	 * 档期
	 */
	@Schema(description = "档期")
	private String flag;


	/**
	 * 订单结算编号
	 */
	@Schema(description = "订单结算单号")
	private String sn;


	/**
	 * 开始时间
	 */
	@Schema(description = "开始时间")
	private Date startDate;


	/**
	 * 结束时间
	 */
	@Schema(description = "结束时间")
	private Date endDate;


	/**
	 * 这期订单的实际金额
	 */
	@Schema(description = "这期订单的实际金额")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal orderAmount;


	/**
	 * 这期的运费
	 */
	@Schema(description = "这期的运费")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal shippingTotals;


	/**
	 * 退单金额
	 */
	@Schema(description = "退单金额")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal orderReturnTotals;


	/**
	 * 平台佣金金额
	 */
	@Schema(description = "平台佣金金额")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal commisTotals;


	/**
	 * 分销佣金
	 */
	@Schema(description = "分销佣金")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal distCommisTotals;


	/**
	 * 退还佣金
	 */
	@Schema(description = "退还佣金")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal commisReturnTotals;

	/**
	 * 总订单佣金
	 */
	@Schema(description = "总订单佣金")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal orderCommissionTotals;

	/**
	 * 红包总额
	 */
	@Schema(description = "红包总额")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal redpackTotals;


	/**
	 * 应结金额
	 */
	@Schema(description = "应结金额")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal resultTotalAmount;

	/**
	 * 积分抵扣金额
	 */
	@Schema(description = "积分抵扣金额")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal totalDeductionAmount;


	/**
	 * 积分结算金额
	 */
	@Schema(description = "积分结算金额")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal totalSettlementPrice;

	/**
	 * 生成结算单日期
	 */
	@Schema(description = "生成结算单日期")
	private Date createDate;


	/**
	 * 结算单年月
	 */
	@Schema(description = "结算单年月")
	private String month;


	/**
	 * 1默认 2店家已确认  3平台已审核  4结算完成
	 */
	@Schema(description = "1默认 2店家已确认  3平台已审核  4结算完成")
	private String status;

	/**
	 * 店铺名
	 */
	@Schema(description = "店铺名")
	private String shopName;

	/**
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	private Long shopId;

	/**
	 * 付款日期
	 */
	@Schema(description = "付款日期/")
	private Date payDate;


	/**
	 * 支付备注
	 */
	@Schema(description = "支付备注")
	private String payContent;


	/**
	 * 管理员备注
	 */
	@Schema(description = "管理员备注")
	private String adminNote;


	/**
	 * 当前档期使用的平台结算佣金比例
	 */
	@Schema(description = "当前档期使用的平台结算佣金比例")
	private Double commisRate;


	/**
	 * 档期内退款红包金额
	 */
	@Schema(description = "档期内退款红包金额")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal orderReturnRedpackTotals;


	/**
	 * 档期内结算的预售定金总额
	 */
	@Schema(description = "档期内结算的预售定金总额")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal preDepositPriceTotal;


	/**
	 * 档期内结算的拍卖保证金总额
	 */
	@Schema(description = "档期内结算的拍卖保证金总额")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal auctionDepositTotal;

	/**
	 * 商家确认时间
	 */
	@Schema(description = "商家确认时间")
	private Date shopConfirmDate;

	/**
	 * 商家审核时间
	 */
	@Schema(description = "商家审核时间")
	private Date platformAuditDate;
}
