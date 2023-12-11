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
 * 订单退换货表(orderRefundReturn)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_order_refund_return")
public class OrderRefundReturn implements GenericEntity<Long> {

	private static final long serialVersionUID = 635466333513382942L;

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "ORDER_REFUND_RETURN_SEQ")
	private Long id;


	/**
	 * 买家ID
	 */
	@Column(name = "user_id")
	private Long userId;


	/**
	 * 店铺ID
	 */
	@Column(name = "shop_id")
	private Long shopId;


	/**
	 * 店铺名称
	 */
	@Column(name = "shop_name")
	private String shopName;


	/**
	 * 订购ID
	 */
	@Column(name = "order_id")
	private Long orderId;


	/**
	 * 订单流水号(商户系统内部的订单号)
	 */
	@Column(name = "order_number")
	private String orderNumber;


	/**
	 * 订单商品ID,全部退款是0
	 */
	@Column(name = "product_id")
	private Long productId;


	/**
	 * 商品名称
	 */
	@Column(name = "product_name")
	private String productName;


	/**
	 * 订单SKU ID,全部退款是0
	 */
	@Column(name = "sku_id")
	private Long skuId;


	/**
	 * 退货数量
	 */
	@Column(name = "goods_num")
	private Integer goodsNum;


	/**
	 * 订单总金额
	 */
	@Column(name = "order_money")
	private BigDecimal orderMoney;


	/**
	 * 订单项ID 全部退款是0
	 */
	@Column(name = "order_item_id")
	private Long orderItemId;


	/**
	 * 申请编号(商户退款单号)
	 */
	@Column(name = "refund_sn")
	private String refundSn;

	/**
	 * ls_order_settlement 清算单据号
	 */
	@Column(name = "pay_settlement_sn")
	private String paySettlementSn;


	/**
	 * 第三方退款单号(微信退款单号)
	 */
	@Column(name = "out_refund_no")
	private String outRefundNo;


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
	 * 退款金额（应该是用户自己输入的金额）
	 */
	@Column(name = "refund_amount")
	private BigDecimal refundAmount;


	/**
	 * 商品图片
	 */
	@Column(name = "product_image")
	private String productImage;


	/**
	 * 申请类型:1,仅退款,2退款退货,-1已撤销
	 */
	@Column(name = "apply_type")
	private Integer applyType;


	/**
	 * 卖家处理状态:1为待审核,2为同意,3为不同意
	 */
	@Column(name = "seller_status")
	private Integer sellerStatus;


	/**
	 * 申请状态:1待卖家处理 ,2为待管理员处理,3为已完成 -2用户超时未发货，系统取消售后
	 */
	@Column(name = "apply_status")
	private Integer applyStatus;


	/**
	 * 退货类型:1为不用退货,2为需要退货,默认为0
	 */
	@Column(name = "return_type")
	private Integer returnType;


	/**
	 * 物流状态:1为待发货,2为待收货,3为未收到,4为已收货,默认为0
	 */
	@Column(name = "goods_status")
	private Integer goodsStatus;


	/**
	 * 处理退款状态: 0:退款处理中 1:退款成功 -1:退款失败
	 */
	@Column(name = "handle_success_status")
	private Integer handleSuccessStatus;


	/**
	 * 处理退款的方式
	 */
	@Column(name = "handle_type")
	private String handleType;

	/**
	 * 订单类型  {@link com.legendshop.order.enums.OrderTypeEnum}
	 */
	@Column(name = "order_type")
	private String orderType;


	/**
	 * 申请时间
	 */
	@Column(name = "create_time")
	private Date createTime;


	/**
	 * 卖家处理时间
	 */
	@Column(name = "seller_time")
	private Date sellerTime;


	/**
	 * 管理员处理时间 [最后审核的处理时间--用于统计退货金额]
	 */
	@Column(name = "admin_time")
	private Date adminTime;


	/**
	 * 退款原因
	 */
	@Column(name = "reason")
	private String reason;


	/**
	 * 退货照片凭证
	 */
	@Column(name = "photo_voucher")
	private String photoVoucher;


	/**
	 * 退款说明
	 */
	@Column(name = "buyer_message")
	private String buyerMessage;


	/**
	 * 卖家备注
	 */
	@Column(name = "seller_message")
	private String sellerMessage;


	/**
	 * 管理员备注
	 */
	@Column(name = "admin_message")
	private String adminMessage;

	/**
	 * 物流公司id
	 */
	@Column(name = "logistics_id")
	private Long logisticsId;


	/**
	 * 物流公司名称
	 */
	@Column(name = "logistics_company_name")
	private String logisticsCompanyName;


	/**
	 * 物流公司名称
	 */
	@Column(name = "logistics_company_code")
	private String logisticsCompanyCode;


	/**
	 * 物流单号
	 */
	@Column(name = "logistics_number")
	private String logisticsNumber;


	/**
	 * 发货时间
	 */
	@Column(name = "ship_time")
	private Date shipTime;

	/**
	 * 物流投递完成时间
	 */
	@Column(name = "logistics_receiving_time")
	private Date logisticsReceivingTime;

	/**
	 * 收货时间
	 */
	@Column(name = "receive_time")
	private Date receiveTime;


	/**
	 * 第三方退款金额
	 */
	@Column(name = "third_party_refund")
	private BigDecimal thirdPartyRefund;


	/**
	 * 平台退款金额(预存款)
	 */
	@Column(name = "platform_refund")
	private BigDecimal platformRefund;


	/**
	 * 退款单创建来源  0 用户 1 商家 2 平台
	 */
	@Column(name = "refund_source")
	private Integer refundSource;


	/**
	 * 订单商品数量
	 */
	@Column(name = "product_num")
	private Integer productNum;


	/**
	 * 订单商品规格属性
	 */
	@Column(name = "product_attribute")
	private String productAttribute;


	/**
	 * 订单商品金额
	 */
	@Column(name = "order_item_money")
	private BigDecimal orderItemMoney;

	/**
	 * 退的积分
	 */
	@Column(name = "integral")
	private Integer integral;


	/**
	 * 商家操作人
	 */
	@Column(name = "shop_operator")
	private String shopOperator;

	/**
	 * 商家操作时间
	 */
	@Column(name = "shop_operator_time")
	private Date shopOperatorTime;

	/**
	 * 用户操作人
	 */
	@Column(name = "user_operator")
	private String userOperator;

	/**
	 * 用户操作时间
	 */
	@Column(name = "user_operator_time")
	private Date userOperatorTime;

	/**
	 * 平台操作人
	 */
	@Column(name = "admin_operator")
	private String adminOperator;

	/**
	 * 平台操作时间
	 */
	@Column(name = "admin_operator_time")
	private Date adminOperatorTime;

	/**
	 * 售后描述
	 */
	@Column(name = "description")
	private String description;

	/**
	 * 取消时间
	 */
	@Column(name = "cancellation_time")
	private Date cancellationTime;

	/**
	 * 取消创建来源
	 */
	@Column(name = "cancellation_type")
	private Integer cancellationType;
}
