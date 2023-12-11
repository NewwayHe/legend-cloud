/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;


import com.legendshop.common.core.dto.BaseDTO;
import com.legendshop.order.enums.OrderStatusEnum;
import com.legendshop.order.enums.OrderTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单DTO
 *
 * @author legendshop
 */
@Data
public class OrderDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = -90311760015808224L;

	/**
	 * 购买用户ID
	 */
	private Long userId;

	/**
	 * 所属店铺
	 */
	private Long shopId;


	/**
	 * 商城名称
	 */
	private String shopName;

	/**
	 * 订单流水号
	 */
	private String orderNumber;


	/**
	 * 产品名称,多个产品将会以逗号隔开
	 */
	private String productName;


	/**
	 * 订单类型
	 * {@link OrderTypeEnum}
	 */
	private String orderType;


	/**
	 * 总价格（没有计算优惠）
	 */
	private BigDecimal totalPrice;


	/**
	 * 真实总价格（减去了优惠）
	 */
	private BigDecimal actualTotalPrice;


	/**
	 * 支付类型标识
	 */
	private String payTypeId;

	/**
	 * 支付类型名称
	 */
	private String payTypeName;


	/**
	 * 支付时间
	 */
	private Date payTime;

	/**
	 * ls_order_settlement清算单据号
	 */
	private String paySettlementSn;


	/**
	 * 订单状态
	 * {@link OrderStatusEnum}
	 */
	private Integer status;


	/**
	 * 运费价格
	 */
	private BigDecimal freightPrice;

	/**
	 * 是否需要发票
	 */
	private Boolean needInvoiceFlag;

	/**
	 * 是否已开具发票
	 */
	private Boolean hasInvoiceFlag;

	/**
	 * 订单发票ID
	 */
	private Long orderInvoiceId;


	/**
	 * 用户订单地址Id
	 */
	private Long addressOrderId;


	/**
	 * 订单物流信息ID
	 */
	private Long orderLogisticsId;


	/**
	 * 买家留言
	 */
	private String message;

	/**
	 * 商家/后台备注
	 */
	private String remark;


	/**
	 * 订单商品总数
	 */
	private Integer productQuantity;


	/**
	 * 发货时间
	 */
	private Date deliveryTime;


	/**
	 * 完成时间
	 */
	private Date completeTime;


	/**
	 * 是否已经支付，1：已经支付过，0：，没有支付过
	 */
	private Boolean payedFlag;


	/**
	 * 用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除
	 */
	private Integer deleteStatus;


	/**
	 * 促销活动优惠总金额
	 */
	private BigDecimal discountTotalAmount;


	/**
	 * 使用的优惠券金额
	 */
	private BigDecimal couponAmount;


	/**
	 * 使用平台优惠券的金额
	 */
	private BigDecimal platformCouponAmount;


	/**
	 * 退款状态
	 * 0:默认,1:在处理,2:处理完成
	 */
	private Integer refundStatus;


	/**
	 * 是否结算[用于结算档期统计]
	 */
	private Boolean billFlag;


	/**
	 * 订单结算编号[用于结算档期统计]
	 */
	private String billSn;


	/**
	 * 是否提醒过发货
	 */
	private Boolean remindDeliveryFlag;


	/**
	 * 提醒发货时间
	 */
	private Date remindDeliveryTime;


	/**
	 * 用户/后台取消订单的原因
	 */
	private String cancelReason;


	/**
	 * 订单已调整的金额
	 */
	private BigDecimal changedPrice;

	/**
	 * 物流投递完成时间
	 */
	private Date logisticsReceivingTime;


	/**
	 * 佣金金额
	 */
	private BigDecimal commission;

	/**
	 * 活动id
	 */
	private Long activityId;


	@Schema(description = "使用积分")
	private Integer totalIntegral;

	@Schema(description = "总共抵扣金额")
	private BigDecimal totalDeductionAmount;

	@Schema(description = "结算价")
	private BigDecimal settlementPrice;

	/**
	 * 兑换积分,比例 x:1
	 */
	@Schema(description = "兑换积分,比例 x:1")
	private BigDecimal proportion;

	@Schema(description = "是否已备注 1.是  0.否")
	private Boolean remarkedFlag;

	/**
	 * 自购省金额
	 */
	@Schema(description = "自购省金额")
	private BigDecimal selfPurchaseAmount;

	/**
	 * 易宝订单ID
	 */
	@Schema(description = "易宝订单ID")
	private Long yeepayOrderId;

	/**
	 * 易宝收款订单号
	 */
	@Schema(description = "易宝收款订单号")
	private String uniqueOrderNo;

	@Schema(description = "来源")
	private String source;

	@Schema(description = "收货倒计时（天）")
	private Integer receivingDay;

	/**
	 * 订单评论有效时间
	 */
	@Schema(description = "订单评论有效时间(天)")
	private Integer commentValidDay;

	@Schema(description = "订单超时支付时间(分钟)")
	private Integer cancelUnpayMinutes;

	/**
	 * {@link com.legendshop.order.enums.OrderExceptionStatusEnum}
	 */
	@Schema(description = "异常状态   1: 重复支付")
	private Integer exceptionStatus;

	/**
	 * 图片
	 */
	private String pic;


	/**
	 * 配送类型 {@link com.legendshop.product.enums.ProductDeliveryTypeEnum}
	 */
	@Schema(description = "配送方式  0: 商家配送 10:到店自提 ")
	private Integer deliveryType;
}
