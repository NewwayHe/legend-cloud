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
import com.legendshop.common.core.dto.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订购表(order)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_order")
public class Order extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = -90311760015804829L;
	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "ORDER_SEQ")
	private Long id;


	/**
	 * 购买用户ID
	 */
	@Column(name = "user_id")
	private Long userId;

	/**
	 * 所属店铺
	 */
	@Column(name = "shop_id")
	private Long shopId;


	/**
	 * 商城名称
	 */
	@Column(name = "shop_name")
	private String shopName;

	/**
	 * 订单流水号
	 */
	@Column(name = "order_number")
	private String orderNumber;


	/**
	 * 产品名称,多个产品将会以逗号隔开
	 */
	@Column(name = "product_name")
	private String productName;


	/**
	 * 订单状态
	 * {@link com.legendshop.order.enums.OrderStatusEnum}
	 */
	@Column(name = "status")
	private Integer status;

	/**
	 * 订单类型
	 * {@link com.legendshop.order.enums.OrderTypeEnum}
	 */
	@Column(name = "order_type")
	private String orderType;


	/**
	 * 营销活动ID[秒杀、团购、拼团、拍卖]
	 */
	@Column(name = "activity_id")
	private Long activityId;


	/**
	 * 总价格（没有计算优惠）
	 */
	@Column(name = "total_price")
	private BigDecimal totalPrice;


	/**
	 * 真实总价格（减去了优惠）
	 */
	@Column(name = "actual_total_price")
	private BigDecimal actualTotalPrice;


	/**
	 * 支付类型ID
	 */
	@Column(name = "pay_type_id")
	private String payTypeId;


	/**
	 * 支付类型名称
	 */
	@Column(name = "pay_type_name")
	private String payTypeName;


	/**
	 * 支付时间
	 */
	@Column(name = "pay_time")
	private Date payTime;


	/**
	 * ls_pay_settlement清算单据号
	 */
	@Column(name = "pay_settlement_sn")
	private String paySettlementSn;


	/**
	 * 订单物流信息ID
	 */
	@Column(name = "order_logistics_id")
	private Long orderLogisticsId;


	/**
	 * 运费价格
	 */
	@Column(name = "freight_price")
	private BigDecimal freightPrice;


	/**
	 * 是否需要发票
	 */
	@Column(name = "need_invoice_flag")
	private Boolean needInvoiceFlag;

	/**
	 * 是否已开具发票
	 */
	@Column(name = "has_invoice_flag")
	private Boolean hasInvoiceFlag;

	/**
	 * 订单发票ID
	 */
	@Column(name = "order_invoice_id")
	private Long orderInvoiceId;


	/**
	 * 用户订单地址Id
	 */
	@Column(name = "address_order_id")
	private Long addressOrderId;


	/**
	 * 商家备注
	 */
	@Column(name = "remark")
	private String remark;

	/**
	 * 是否已备注 1.是  0.否
	 */
	@Column(name = "remarked_flag")
	private Boolean remarkedFlag;

	/**
	 * 商家备注更新时间
	 */
	@Column(name = "remark_date")
	private Date remarkDate;

	/**
	 * 买家提交的留言
	 */
	@Column(name = "message")
	private String message;


	/**
	 * 订单商品总数
	 */
	@Column(name = "product_quantity")
	private Integer productQuantity;


	/**
	 * 发货时间
	 */
	@Column(name = "delivery_time")
	private Date deliveryTime;


	/**
	 * 完成时间
	 */
	@Column(name = "complete_time")
	private Date completeTime;


	/**
	 * 是否已经支付，1：已经支付过，0：，没有支付过
	 */
	@Column(name = "payed_flag")
	private Boolean payedFlag;


	/**
	 * 用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除
	 */
	@Column(name = "delete_status")
	private Integer deleteStatus;


	/**
	 * 促销活动优惠总金额
	 */
	@Column(name = "discount_total_amount")
	private BigDecimal discountTotalAmount;


	/**
	 * 使用的优惠券金额
	 */
	@Column(name = "coupon_amount")
	private BigDecimal couponAmount;


	/**
	 * 使用的平台优惠券金额
	 */
	@Column(name = "platform_coupon_amount")
	private BigDecimal platformCouponAmount;


	/**
	 * 退款状态
	 * 0:默认,1:在处理,2:处理完成
	 */
	@Column(name = "refund_status")
	private Integer refundStatus;


	/**
	 * 是否结算[用于结算档期统计]
	 */
	@Column(name = "bill_flag")
	private Boolean billFlag;


	/**
	 * 订单结算编号[用于结算档期统计]
	 */
	@Column(name = "bill_sn")
	private String billSn;


	/**
	 * 是否提醒过发货
	 */
	@Column(name = "remind_delivery_flag")
	private Boolean remindDeliveryFlag;


	/**
	 * 提醒发货时间
	 */
	@Column(name = "remind_delivery_time")
	private Date remindDeliveryTime;


	/**
	 * 物流投递完成时间
	 */
	@Column(name = "logistics_receiving_time")
	private Date logisticsReceivingTime;


	/**
	 * 用户/后台取消订单的原因
	 */
	@Column(name = "cancel_reason")
	private String cancelReason;


	/**
	 * 订单已调整的金额
	 */
	@Column(name = "changed_price")
	private BigDecimal changedPrice;


	/**
	 * 使用积分
	 */
	@Column(name = "total_integral")
	private Integer totalIntegral;


	/**
	 * 抵扣金额
	 */
	@Column(name = "total_deduction_amount")
	private BigDecimal totalDeductionAmount;

	/**
	 * 结算价
	 */
	@Column(name = "settlement_price")
	private BigDecimal settlementPrice;

	/**
	 * 兑换积分,比例 x:1
	 */
	@Column(name = "proportion")
	private BigDecimal proportion;

	/**
	 * 自购省金额
	 */
	@Column(name = "self_purchase_amount")
	private BigDecimal selfPurchaseAmount;

	/**
	 * 易宝订单ID
	 */
	@Column(name = "yeepay_order_id")
	private Long yeepayOrderId;

	/**
	 * 易宝收款订单号
	 */
	@Column(name = "unique_order_no")
	private String uniqueOrderNo;

	/**
	 * 订单最终售后截止时间（取订单项里最晚的售后截止时间）
	 */
	@Column(name = "final_return_deadline")
	private Date finalReturnDeadline;

	/**
	 * 来源
	 */
	@Column(name = "source")
	private String source;

	/**
	 * 收货倒计时 天
	 */
	@Column(name = "receiving_day")
	private Integer receivingDay;

	/**
	 * 订单评论有效时间 天
	 */
	@Column(name = "comment_valid_day")
	private Integer commentValidDay;

	/**
	 * 订单超时支付时间(分钟)
	 */
	@Column(name = "cancel_unpay_minutes")
	private Integer cancelUnpayMinutes;

	/**
	 * 异常状态   1: 重复支付
	 * {@link com.legendshop.order.enums.OrderExceptionStatusEnum}
	 */
	@Column(name = "exception_status")
	private Integer exceptionStatus;

	/**
	 * 配送类型 {@link com.legendshop.product.enums.ProductDeliveryTypeEnum}
	 */
	@Column(name = "delivery_type")
	private Integer deliveryType;

	@Column(name = "shop_user_id")
	private Long shopUserId;

	@Column(name = "shop_user_name")
	private String shopUserName;
}
