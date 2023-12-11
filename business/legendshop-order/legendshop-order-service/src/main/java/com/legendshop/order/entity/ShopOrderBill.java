/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.entity;


import cn.legendshop.jpaplus.persistence.*;
import cn.legendshop.jpaplus.support.GenericEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商家订单结算表(ShopOrderBill)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_shop_order_bill")
public class ShopOrderBill implements GenericEntity<Long> {

	private static final long serialVersionUID = -28628503012605289L;

	/**
	 * ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "SHOP_ORDER_BILL_SEQ")
	private Long id;


	/**
	 * 店铺ID
	 */
	@Column(name = "shop_id")
	private Long shopId;


	/**
	 * 店铺名
	 */
	@Column(name = "shop_name")
	private String shopName;


	/**
	 * 档期
	 */
	@Column(name = "flag")
	private String flag;


	/**
	 * 订单结算编号
	 */
	@Column(name = "sn")
	private String sn;


	/**
	 * 开始时间
	 */
	@Column(name = "start_date")
	private Date startDate;


	/**
	 * 结束时间
	 */
	@Column(name = "end_date")
	private Date endDate;


	/**
	 * 这期订单的实际金额
	 */
	@Column(name = "order_amount")
	private BigDecimal orderAmount;


	/**
	 * 这期的运费
	 */
	@Column(name = "shipping_totals")
	private BigDecimal shippingTotals;


	/**
	 * 退单金额
	 */
	@Column(name = "order_return_totals")
	private BigDecimal orderReturnTotals;


	/**
	 * 平台佣金金额
	 */
	@Column(name = "commis_totals")
	private BigDecimal commisTotals;


	/**
	 * 分销佣金
	 */
	@Column(name = "dist_commis_totals")
	private BigDecimal distCommisTotals;


	/**
	 * 退还佣金
	 */
	@Column(name = "commis_return_totals")
	private BigDecimal commisReturnTotals;


	/**
	 * 红包总额
	 */
	@Column(name = "redpack_totals")
	private BigDecimal redpackTotals;


	/**
	 * 积分抵扣金额
	 */
	@Column(name = "total_deduction_amount")
	private BigDecimal totalDeductionAmount;


	/**
	 * 积分结算金额
	 */
	@Column(name = "total_settlement_price")
	private BigDecimal totalSettlementPrice;


	/**
	 * 应结金额
	 */
	@Column(name = "result_total_amount")
	private BigDecimal resultTotalAmount;


	/**
	 * 生成结算单日期
	 */
	@Column(name = "create_date")
	private Date createDate;


	/**
	 * 结算单年月
	 */
	@Column(name = "month")
	private String month;


	/**
	 * 1默认 2店家已确认  3平台已审核  4结算完成
	 */
	@Column(name = "status")
	private int status;


	/**
	 * 付款日期
	 */
	@Column(name = "pay_date")
	private Date payDate;


	/**
	 * 支付备注
	 */
	@Column(name = "pay_content")
	private String payContent;


	/**
	 * 管理员备注
	 */
	@Column(name = "admin_note")
	private String adminNote;


	/**
	 * 当前档期使用的平台结算佣金比例
	 */
	@Column(name = "commis_rate")
	private Double commisRate;


	/**
	 * 档期内退款红包金额
	 */
	@Column(name = "order_return_redpack_totals")
	private BigDecimal orderReturnRedpackTotals;


	/**
	 * 档期内结算的预售定金总额
	 */
	@Column(name = "pre_deposit_price_total")
	private BigDecimal preDepositPriceTotal;


	/**
	 * 档期内结算的拍卖保证金总额
	 */
	@Column(name = "auction_deposit_total")
	private BigDecimal auctionDepositTotal;

	/**
	 * 商家确认时间
	 */
	@Column(name = "shop_confirm_date")
	private Date shopConfirmDate;

	/**
	 * 商家审核时间
	 */
	@Column(name = "platform_audit_date")
	private Date platformAuditDate;

}
