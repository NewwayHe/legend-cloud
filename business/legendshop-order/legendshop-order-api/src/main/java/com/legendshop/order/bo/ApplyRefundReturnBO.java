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

import java.util.Date;

/**
 * @author legendshop
 * @Description 退款及退货的DTO
 */
@Schema(description = "退货退款Dto")
public class ApplyRefundReturnBO {

	/**
	 * 申请类型:1,仅退款,2退款退货,默认为1
	 */
	@Schema(description = "申请类型:1,仅退款,2退款退货,默认为1")
	private Long applyType;

	/**
	 * 订单ID
	 */
	@Schema(description = "订单ID")
	private Long orderId;

	/**
	 * 订单类型
	 */
	@Schema(description = "订单类型 ")
	private String orderType;

	/**
	 * 订单项Id
	 */
	@Schema(description = "订单项Id")
	private Long orderItemId;

	/**
	 * 订单号
	 */
	@Schema(description = "订单号")
	private String orderNumber;

	/**
	 * 订单总金额
	 */
	@Schema(description = "订单总金额")
	private Double orderbMoney;

	/**
	 * 商品名称 或 订单编号
	 */
	@Schema(description = "商品名称 或 订单编号")
	private String name;

	/**
	 * 退款金额
	 */
	@Schema(description = "退款金额")
	private Double refundAmount;

	/**
	 * 退货数量
	 */
	@Schema(description = "退货数量 ")
	private Integer goodsCount;

	/**
	 * shop Id
	 */
	@Schema(description = "店铺id")
	private Long shopId;

	/**
	 * shopName
	 */
	@Schema(description = "店铺名称")
	private String shopName;

	/**
	 * 支付类型Id
	 */
	@Schema(description = "支付类型Id")
	private String payTypeId;

	/**
	 * 支付类型名称
	 */
	@Schema(description = "支付类型名称")
	private String payTypeName;

	/**
	 * 支付流水号
	 */
	@Schema(description = "支付流水号 ")
	private String paySerialNumber;

	/**
	 * settlementNumber
	 */
	@Schema(description = " 清算单据号")
	private String settlementNumber;

	/**
	 * settlementTime
	 */
	@Schema(description = " 清算单据号时间")
	private Date settlementTime;

	/**
	 * 订单商品ID
	 */
	@Schema(description = "订单商品ID")
	private Long productId;

	/**
	 * 订单SKU ID
	 */
	@Schema(description = "订单SKU ID")
	private Long skuId;

	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String productName;

	/**
	 * 商品图片
	 */
	@Schema(description = "商品图片")
	private String productImage;

	/**
	 * 订单支付方式 : 在线支付,货到付款
	 */
	@Schema(description = "订单支付方式 : 在线支付,货到付款")
	private Integer payManner;

	/**
	 * 是否货到付款
	 */
	@Schema(description = "是否货到付款 ")
	private Integer isCod;

	/**
	 * 订单状态
	 */
	@Schema(description = "订单状态")
	private Integer orderStatus;

	/**
	 * 订单退款状态
	 */
	@Schema(description = "订单退款状态")
	private Integer refundStatus;

	/**
	 * 订单项退款/退货状态
	 */
	@Schema(description = "订单项退款/退货状态")
	private Integer itemRefundStatus;

	/**
	 * 订单完成时间
	 */
	@Schema(description = "订单完成时间 ")
	private Date finallyDate;


	/**
	 * 商品属性
	 */
	@Schema(description = "商品属性")
	private String productAttribute;

	/**
	 * 商品购买数量
	 */
	@Schema(description = "商品购买数量")
	private Long basketCount;

	/**
	 * 商品价格
	 */
	@Schema(description = "商品价格")
	private Double prodCash;

	/**
	 * 商品价格
	 */
	@Schema(description = "用户id（用于商家端）")
	private Long userId;

}
