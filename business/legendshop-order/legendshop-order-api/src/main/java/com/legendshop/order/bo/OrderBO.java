/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.bo;


import cn.hutool.core.util.NumberUtil;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.legendshop.activity.dto.OrderActivityDetailDTO;
import com.legendshop.common.core.serialize.BigDecimalSerialize;
import com.legendshop.order.dto.OrderAftersalesInformationDTO;
import com.legendshop.order.dto.OrderItemDTO;
import com.legendshop.order.dto.OrderLogisticsDTO;
import com.legendshop.order.dto.OrderPayTypeDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单Bo
 *
 * @author legendshop
 */
@Schema(description = "订单bo")
@Data
public class OrderBO implements Serializable {

	private static final long serialVersionUID = -90311760015808929L;


	@Schema(description = "主键id")
	private Long id;


	@Schema(description = "订购流水号")
	private String orderNumber;


	@Schema(description = "产品名称,多个产品将会以逗号隔开")
	private String productName;


	@Schema(description = "订单商品总数")
	private Integer productQuantity;


	@Schema(description = "用户名")
	private String nickName;


	@Schema(description = "订购用户ID")
	private Long userId;


	@Schema(description = "下单时间")
	private Date createTime;


	@Schema(description = "购买时间")
	private Date payTime;


	@Schema(description = "发货时间")
	private Date deliveryTime;

	@Schema(description = "状态: 待审核（2） 已同意（3）已取消（-1） 已拒绝(-4)")
	private Integer applyStatus;

	@Schema(description = "申请类型:1,仅退款,2退款退货,-1已撤销 3商家申请取消订单")
	private Integer applyType;


	@Schema(description = "完成时间")
	private Date completeTime;


	@Schema(description = "订单更新时间")
	private Date updateDate;


	@Schema(description = "订单类型 [O:普通订单， P：预售订单,G:团购订单,S:秒杀订单,MG:拼团订单]")
	private String orderType;


	@Schema(description = "总值(没有扣除优惠)")
	private BigDecimal totalPrice;


	@Schema(description = "实际总值（减去优惠,加上运费）")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal actualTotalPrice;


	@Schema(description = "支付类型名称")
	private String payTypeName;


	@Schema(description = "支付流水号")
	private String paySerialNumber;


	@Schema(description = "ls_order_settlement清算单据号")
	private String settlementNumber;


	@Schema(description = "所属店铺")
	private Long shopId;


	@Schema(description = "商城名称")
	private String shopName;


	/**
	 * {@link com.legendshop.order.enums.OrderStatusEnum}
	 */
	@Schema(description = "订单状态,待付款（1）、已付定金(3)、待发货（5）、待收货（10）、待收货（15）、已完成（20）、已取消（25）")
	private Integer status;


	@Schema(description = "用户积分ID")
	private Long scoreId;


	@Schema(description = "使用积分数")
	private Integer score;


	@Schema(description = "收货人")
	private String receiver;


	@Schema(description = "收货手机号码")
	private String mobile;


	@Schema(description = "收货详细地址")
	private String detailAddress;


	@Schema(description = "运费模板ID")
	private Long logisticsTemplate;


	@Schema(description = "订单物流信息ID")
	private Long orderLogisticsId;


	@Schema(description = "订单物流配送信息")
	private OrderLogisticsDTO orderLogisticsDTO;


	@Schema(description = "订单运费")
	private BigDecimal freightPrice;


	@Schema(description = "订单发票ID")
	private Long orderInvoiceId;


	@Schema(description = "买家留言")
	private String message;


	@Schema(description = "商家/后台备注")
	private String remark;


	@Schema(description = "用户订单地址Id")
	private Integer addressOrderId;


	@Schema(description = "是否已经支付，1：已经支付过，0：，没有支付过")
	private Boolean payedFlag;


	@Schema(description = "用户订单删除状态，0：没有删除， 1：回收站， 2：永久删除")
	private Integer deleteStatus;


	@Schema(description = "分销的佣金总额")
	private BigDecimal distCommisAmount;


	@Schema(description = "促销活动优惠总金额")
	private BigDecimal discountTotalAmount;


	@Schema(description = "使用的商家优惠券金额")
	private BigDecimal couponAmount;


	@Schema(description = "使用的平台优惠券金额")
	private BigDecimal platformCouponAmount;


	@Schema(description = "使用的红包金额")
	private BigDecimal redpackOffPrice;


	@Schema(description = "收货人城市ID（冗余)")
	private Integer cityId;


	@Schema(description = "收货人省ID（冗余)")
	private Integer provinceId;


	@Schema(description = " 退款状态 0:默认,1:在处理,2:处理完成 3已结束")
	private Integer refundStatus;


	@Schema(description = "是否结算[用于结算档期统计]")
	private Boolean billFlag;


	@Schema(description = "订单结算编号[用于结算档期统计]")
	private String billSn;


	@Schema(description = "是否提醒过发货")
	private Boolean remindDeliveryFlag;


	@Schema(description = "提醒发货时间")
	private Date remindDeliveryTime;


	@Schema(description = "秒杀活动ID/团购活动id/预售id")
	private Long activeId;


	/**
	 * 发票信息 start
	 */
	@Schema(description = "是否需要发票")
	private Boolean needInvoiceFlag;


	@Schema(description = "发票类型，NORMAL:增值税普票 DEDICATED:增值税专票")
	private String invoiceType;


	@Schema(description = "普票类型，PERSONAL:个人，COMPANY:企业")
	private String invoiceTitleType;


	@Schema(description = "个人普票：发票抬头信息 公司普票：发票抬头信息 增值税专票：公司名称")
	private String invoiceCompany;


	@Schema(description = "发票税号")
	private String invoiceHumNumber;


	@Schema(description = "开户银行（增值税发票）")
	private String invoiceDepositBank;


	@Schema(description = "开户银行账号（增值税发票）")
	private String invoiceBankAccountNum;


	@Schema(description = "注册地址（增值税发票）")
	private String invoiceRegisterAddr;


	@Schema(description = "注册电话（增值税发票）")
	private String invoiceRegisterPhone;


	@Schema(description = "是否已开发票")
	private Boolean invoiceFlag;
	/* 发票信息 end */


	@Schema(description = "用户/后台取消订单的原因")
	private String cancelReason;


	@Schema(description = "商家备注时间")
	private Date remarkDate;

	@Schema(description = "下单时商品所属商家是否开启发票 1开启 0关闭")
	private Boolean switchInvoice;


	@Schema(description = "订单已调整的金额")
	private BigDecimal changedPrice;


	@Schema(description = "订单项DTO")
	List<OrderItemDTO> orderItemDTOList;


	@Schema(description = "物流投递完成时间")
	private Date logisticsReceivingTime;

	@Schema(description = "优惠金额")
	private BigDecimal totalDiscountAmount;

	@Schema(description = "最后支付时间")
	private Long finalPayTime;

	@Schema(description = "自动确定收货时间")
	private Long countDownTime;

	@Schema(description = "支付单据类型[订单支付:ORDINARY_ORDER,预付款充值:USER_RECHARGE]", allowableValues = "ORDINARY_ORDER,USER_RECHARGE")
	private String settlementType;

	@Schema(description = "活动详情")
	private OrderActivityDetailDTO activityDetailDTO;

	@Schema(description = "使用积分")
	private Integer totalIntegral;

	@Schema(description = "总共抵扣金额")
	private BigDecimal totalDeductionAmount;

	@Schema(description = "预售商品")
	private PreSellOrderBO preSellOrderBO;

	/**
	 * 结算价
	 */
	@Schema(description = "结算价")
	private BigDecimal settlementPrice;

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

	@Schema(description = "是否会员")
	private Boolean isMember;

	@Schema(description = "用户收获地址")
	private String receiverAddress;

	@Schema(description = "订单最终售后截止时间（取订单项里最晚的售后截止时间）")
	private Date finalReturnDeadline;

	@Schema(description = "订单最终售后截止时间（取订单项里最晚的售后截止时间）时间戳")
	private Long finalReturnDeadlineTimestamp;

	@Schema(description = "订单退款类型： 1、整单退 2、分单退")
	private Integer orderRefundType;


	/**
	 * 退款/退货记录ID
	 */
	@Schema(description = "退款/退货记录ID:申请类型:1,仅退款,2退款退货,-1已撤销 3撤回")
	private Long refundId;

	/**
	 * 收货倒计时天
	 */
	@Schema(description = "收货倒计时 天")
	private Integer receivingDay;


	/**
	 * 订单评论有效时间
	 */
	@Schema(description = "订单评论有效时间(天)")
	private Integer commentValidDay;

	@Schema(description = "订单超时支付时间(分钟)")
	private Integer cancelUnpayMinutes;

	@Schema(description = "商家自动确认收货时长(天)")
	private Date shopAutoReceiveProductDay;

	@Schema(description = "付款方式")
	List<OrderPayTypeDTO> orderPayTypeList;

	@Schema(description = "商品倒计时")
	private Long autoCancelRefundTime;

	@Schema(description = "组装申请取消订单信息")
	private OrderAftersalesInformationDTO orderAftersalesInformationDTO;

	@Schema(description = "取消时间")
	private Date cancellationTime;

	@Schema(description = "取消创建来源  1用户取消售后 2逾期取消")
	private Integer cancellationType;

	@Schema(description = "是否可评价")
	private Boolean commentValidDayInvoice;

	@Schema(description = "核销人id")
	private Long shopUserId;

	@Schema(description = "核销人名字")
	private String shopUserName;
	/**
	 * 配送类型 {@link com.legendshop.product.enums.ProductDeliveryTypeEnum}
	 */
	@Schema(description = "配送方式  0: 商家配送 10:到店自提 ")
	private Integer deliveryType;


	public OrderBO() {
		this.platformCouponAmount = BigDecimal.ZERO;
		this.couponAmount = BigDecimal.ZERO;
		this.discountTotalAmount = BigDecimal.ZERO;
		this.totalDeductionAmount = BigDecimal.ZERO;
		this.selfPurchaseAmount = BigDecimal.ZERO;
	}

	public BigDecimal getTotalDiscountAmount() {
		return NumberUtil.add(platformCouponAmount, couponAmount, discountTotalAmount, totalDeductionAmount, selfPurchaseAmount);
	}
}
