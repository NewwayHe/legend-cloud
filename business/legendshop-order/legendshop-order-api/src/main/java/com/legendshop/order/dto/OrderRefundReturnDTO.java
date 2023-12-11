/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单退款/退货DTO
 *
 * @author legendshop
 */
@Schema(description = "订单退款/退货DTO")
@Data
public class OrderRefundReturnDTO implements Serializable {

	private static final long serialVersionUID = 635466333513382942L;

	/**
	 * 主键id
	 */
	@Schema(description = "主键id")
	private Long id;


	/**
	 * 买家ID
	 */
	@Schema(description = "买家ID")
	private Long userId;


	/**
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	private Long shopId;


	/**
	 * 店铺名称
	 */
	@Schema(description = "店铺名称")
	private String shopName;


	/**
	 * 订单ID
	 */
	@Schema(description = "订单ID")
	private Long orderId;


	/**
	 * 订单流水号(商户系统内部的订单号)
	 */
	@Schema(description = "订单流水号")
	private String orderNumber;


	/**
	 * 订单商品ID,全部退款是0
	 */
	@Schema(description = "订单商品ID")
	private Long productId;


	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String productName;


	/**
	 * 订单SKU ID,全部退款是0
	 */
	@Schema(description = "订单SKU ID")
	private Long skuId;


	/**
	 * 退货数量
	 */
	@Schema(description = "退货数量")
	private Integer goodsNum;


	/**
	 * 订单总金额
	 */
	@Schema(description = "订单总金额")
	private BigDecimal orderMoney;


	/**
	 * 订单项ID
	 */
	@Schema(description = "订单项ID")
	private Long orderItemId;


	/**
	 * 申请编号(商户退款单号)
	 */
	@Schema(description = "退款单号")
	private String refundSn;


	/**
	 * 支付流水号
	 */
	@Schema(description = "支付流水号")
	private String paySettlementSn;


	/**
	 * 第三方退款单号(微信退款单号)
	 */
	@Schema(description = " 第三方退款单号")
	private String outRefundNo;


	/**
	 * 是否结算[用于结算档期统计]
	 */
	@Schema(description = "是否结算[用于结算档期统计]")
	private Boolean billFlag;


	/**
	 * 订单结算编号[用于结算档期统计]
	 */
	@Schema(description = "订单结算编号[用于结算档期统计]")
	private String billSn;


	/**
	 * 订单支付Id
	 */
	@Schema(description = "订单支付Id")
	private String payTypeId;


	/**
	 * 订单支付名称
	 */
	@Schema(description = "订单支付名称")
	private String payTypeName;


	/**
	 * 退款金额（应该是用户自己输入的金额）
	 */
	@Schema(description = "退款金额")
	private BigDecimal refundAmount;


	/**
	 * 商品图片
	 */
	@Schema(description = "商品图片")
	private String productImage;


	/**
	 * 申请类型:1,仅退款,2退款退货  {@link com.legendshop.order.enums.OrderRefundReturnTypeEnum}
	 */
	@Schema(description = "申请类型:1,仅退款,2退款退货,-1已撤销 3商家申请取消订单")
	private Integer applyType;


	/**
	 * 卖家处理状态:0为待审核,1为同意,-1为不同意 {@link com.legendshop.order.enums.OrderRefundReturnStatusEnum}
	 */
	@Schema(description = "卖家处理状态:0为待审核,1为同意,-1为不同意")
	private Integer sellerStatus;


	/**
	 * 申请状态:1待卖家处理 ,2为待管理员处理,3为已完成 {@link com.legendshop.order.enums.OrderRefundReturnStatusEnum}
	 */
	@Schema(description = "申请状态:1待卖家处理 ,2为待管理员处理,3为已完成 ")
	private Integer applyStatus;


	/**
	 * 退货类型:1为不用退货,2为需要退货,默认为0 {@link com.legendshop.order.enums.OrderRefundReturnTypeEnum}
	 */
	@Schema(description = "退货类型:1为不用退货,2为需要退货")
	private Integer returnType;


	/**
	 * 物流状态:1为待发货,3为待签收，5为待收货，7为已收货 {@link com.legendshop.order.enums.OrderRefundReturnStatusEnum}
	 */
	@Schema(description = "物流状态:1为待发货,3为待签收，5为待收货，7为已收货")
	private Integer goodsStatus;


	/**
	 * 处理退款状态: 0:退款处理中 1:退款成功 -1:退款失败
	 */
	@Schema(description = "处理退款状态: 0:退款处理中 1:退款成功 -1:退款失败")
	private Integer handleSuccessStatus;


	/**
	 * 处理退款的方式
	 */
	@Schema(description = "处理退款的方式")
	private String handleType;

	/**
	 * 订单类型  {@link com.legendshop.order.enums.OrderTypeEnum}
	 */
	@Schema(description = "订单类型")
	private String orderType;


	/**
	 * 申请时间
	 */
	@Schema(description = "申请时间")
	private Date createTime;


	/**
	 * 卖家处理时间
	 */
	@Schema(description = "卖家处理时间")
	private Date sellerTime;


	/**
	 * 管理员处理时间 [最后审核的处理时间--用于统计退货金额]
	 */
	@Schema(description = "管理员处理时间")
	private Date adminTime;


	/**
	 * 退款原因
	 */
	@Schema(description = "退款原因")
	private String reason;


	/**
	 * 退货照片凭证
	 */
	@Schema(description = "退货照片凭证")
	private String photoVoucher;


	/**
	 * 退款说明
	 */
	@Schema(description = "退款说明")
	private String buyerMessage;


	/**
	 * 卖家备注
	 */
	@Schema(description = "卖家备注")
	private String sellerMessage;


	/**
	 * 管理员备注
	 */
	@Schema(description = "管理员备注")
	private String adminMessage;


	/**
	 * 物流公司名称
	 */
	@Schema(description = "物流公司名称")
	private String logisticsCompanyName;

	/**
	 * 物流公司code
	 */
	@Schema(description = "物流公司code")
	private String logisticsCompanyCode;


	/**
	 * 物流单号
	 */
	@Schema(description = "物流单号")
	private String logisticsNumber;


	/**
	 * 发货时间
	 */
	@Schema(description = "发货时间")
	private Date shipTime;


	/**
	 * 收货时间
	 */
	@Schema(description = "收货时间")
	private Date receiveTime;


	/**
	 * 第三方退款金额
	 */
	@Schema(description = "第三方退款金额")
	private BigDecimal thirdPartyRefund;


	/**
	 * 平台退款金额(预存款)
	 */
	@Schema(description = "平台退款金额")
	private BigDecimal platformRefund;

	/**
	 * 退款单创建来源  0 用户 1 商家 2 平台
	 */
	@Schema(description = "退款单创建来源  0 用户 1 商家 2 平台")
	private Integer refundSource;


	/**
	 * 订单商品数量
	 */
	@Schema(description = "订单商品数量")
	private Integer productNum;


	/**
	 * 订单商品规格属性
	 */
	@Schema(description = "订单商品规格属性")
	private String productAttribute;


	/**
	 * 订单商品金额
	 */
	@Schema(description = "订单商品金额")
	private BigDecimal orderItemMoney;

	/**
	 * 订单项退款状态
	 */
	@Schema(description = "订单项退款状态")
	private Integer itemRefundStatus;

	/**
	 * 订单状态
	 */
	@Schema(description = "订单状态")
	private Integer orderStatus;

	/**
	 * 订单完成时间
	 */
	@Schema(description = "订单完成时间")
	private Date completeTime;

	@Schema(description = "退的积分")
	private Integer integral;

}
