/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author legendshop
 */
@Schema(description = "订单商品BO")
@Data
public class OrderItemBO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 订单项id
	 */
	private Long orderItemId;

	/**
	 * 订单ID
	 */
	private Long orderId;

	/**
	 * 订单号
	 */
	private String orderNumber;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 订单项流水号
	 */
	private String orderItemNumber;

	/**
	 * 商品id
	 */
	private Long productId;

	/**
	 * skuId
	 */
	private Long skuId;

	/**
	 * 商品快照id
	 */
	private Long snapshotId;

	/**
	 * 购物车商品数量
	 */
	private Integer basketCount;

	/**
	 * 商品名称
	 */
	private String productName;

	/**
	 * 商品属性
	 */
	private String attribute;

	/**
	 * 商品图片
	 */
	private String pic;

	/**
	 * 商品现价
	 */
	private BigDecimal price;

	/**
	 * 商品原价
	 */
	private BigDecimal originalPrice;

	/**
	 * 商品总金额
	 */
	private BigDecimal productTotalAmount;

	/**
	 * 获得积分数
	 */
	private Double obtainIntegral;

	/**
	 * 物流重量(千克)
	 */
	private Double weight;

	/**
	 * 物流体积(立方米)
	 */
	private Double volume;

	/**
	 * 分销的佣金金额
	 */
	private Double distCommissionCash;


	/**
	 * 评论状态： 0 未评价 1 已评价
	 */
	private Boolean commFlag;

	/**
	 * 退款/退货记录ID
	 */
	private Long refundId;

	/**
	 * 退款/退货状态
	 */
	private Integer refundStatus;

	/**
	 * 退款金额
	 */
	private BigDecimal refundAmount;

	/**
	 * 申请类型 1:仅退款,2:退货
	 */
	private Integer refundType;

	/**
	 * 退货数量
	 */
	private Integer refundCount;

	/**
	 * 是否在退换货有效时间内
	 */
	private Boolean inReturnValidPeriodFlag;

	/**
	 * 库存计数方式，false：拍下减库存，true：付款减库存
	 */
	private Boolean stockCounting;


	/**
	 * 佣金返佣类型: CASH 现金, INTEGRAL 积分
	 */
	@Schema(description = "佣金返佣类型: CASH 现金, INTEGRAL 积分")
	private String commissionType;

	/**
	 * 返佣的形式为积分时, 每多少元
	 */
	@Schema(description = "返佣的形式为积分时, 每多少元")
	private BigDecimal eachCommissionRmb;

	/**
	 * 返佣的形式为积分时, 多少积分
	 */
	@Schema(description = "返佣的形式为积分时, 多少积分")
	private Integer commissionIntegral;

	/**
	 * 结算方式 0 ： 收货后  1：售后期结束
	 */
	@Schema(description = "结算方式 0 ： 收货后  1：售后期结束")
	private String commissionSettlementType;

	@Schema(description = "是否有分销，1：分销单，0：非分销单")
	private Boolean distFlag;

	/**
	 * 佣金结算状态: 0 待结算  10 已结算   -10 已取消
	 */
	@Schema(description = "佣金结算状态: 0 待结算  10 已结算   -10 已取消")
	private Integer commissionSettleStatus;

	/**
	 * 成本价
	 */
	@Schema(description = "成本价")
	private BigDecimal costPrice;

	/**
	 * 退换货有效时间(单位：天)
	 */
	@Schema(description = "退换货有效时间(单位：天)")
	private Integer returnValidPeriod;

	@Schema(description = "退换货截止时间")
	private Date returnDeadline;

	@Schema(description = "使用积分")
	private BigDecimal integral;

	@Schema(description = "抵扣金额")
	private BigDecimal deductionAmount;

	/**
	 * 自购省金额
	 */
	@Schema(description = "自购省金额")
	private BigDecimal selfPurchaseAmount;

	/**
	 * 自购省总金额
	 */
	@Schema(description = "自购省总金额")
	private BigDecimal selfPurchaseTotalAmount;

	@Schema(description = "支付时间")
	private Date payTime;
}
