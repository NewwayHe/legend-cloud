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
 * @author legendshop
 */
@Schema(description = "订单商品DTO")
@Data
public class OrderItemDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 订单项id
	 */
	@Schema(description = "订单项id")
	private Long id;

	/**
	 * 订单ID
	 */
	@Schema(description = "订单ID")
	private Long orderId;

	/**
	 * 店铺id
	 */
	@Schema(description = "店铺id")
	private Long shopId;

	/**
	 * 订单号
	 */
	@Schema(description = "订单号")
	private String orderNumber;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private Long userId;

	/**
	 * 订单项流水号
	 */
	@Schema(description = "订单项流水号")
	private String orderItemNumber;

	/**
	 * 商品id
	 */
	@Schema(description = "商品id")
	private Long productId;

	/**
	 * skuId
	 */
	@Schema(description = "skuId")
	private Long skuId;

	/**
	 * 商品快照id
	 */
	@Schema(description = "商品快照id")
	private Long snapshotId;

	/**
	 * 购物车商品数量
	 */
	@Schema(description = "购物车商品数量")
	private Integer basketCount;

	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String productName;

	/**
	 * 商品属性
	 */
	@Schema(description = "商品属性")
	private String attribute;

	/**
	 * 商品图片
	 */
	@Schema(description = "商品图片")
	private String pic;

	@Schema(description = "种草返回商品图片")
	private String productPic;

	/**
	 * 商品现价
	 */
	@Schema(description = "商品现价")
	private BigDecimal price;

	/**
	 * 商品原价
	 */
	@Schema(description = "商品原价")
	private BigDecimal originalPrice;

	/**
	 * 商品总金额
	 */
	@Schema(description = "商品总金额")
	private BigDecimal productTotalAmount;

	/**
	 * 获得积分数
	 */
	@Schema(description = "获得积分数")
	private Double obtainIntegral;

	/**
	 * 物流重量(千克)
	 */
	@Schema(description = "物流重量(千克)")
	private Double weight;

	/**
	 * 物流体积(立方米)
	 */
	@Schema(description = "物流体积(立方米)")
	private Double volume;


	/**
	 * 分销的佣金金额
	 */
	@Schema(description = "分销的佣金金额")
	private BigDecimal distCommissionCash;

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
	 * DistributionSettlementType
	 */
	@Schema(description = "结算方式 0 ： 收货后  1：售后期结束")
	private String commissionSettlementType;


	/**
	 * 成本价
	 */
	@Schema(description = "成本价")
	private BigDecimal costPrice;

	/**
	 * 评论状态： 0 未评价 1 已评价
	 */
	@Schema(description = "评论状态： 0 未评价 1 已评价 ")
	private Boolean commFlag;

	/**
	 * 退款/退货记录ID
	 */
	@Schema(description = "退款/退货记录ID")
	private Long refundId;

	/**
	 * 退款/退货状态
	 */
	@Schema(description = "退款/退货状态")
	private Integer refundStatus;

	/**
	 * 退款金额
	 */
	@Schema(description = "退款金额")
	private BigDecimal refundAmount;

	/**
	 * 申请类型 1:仅退款,2:退货
	 */
	@Schema(description = "申请类型 1:仅退款,2:退货")
	private Integer refundType;

	/**
	 * 退货数量
	 */
	@Schema(description = "退货数量")
	private Integer refundCount;

	/**
	 * 是否在退换货有效时间内
	 */
	@Schema(description = "是否在退换货有效时间内")
	private Boolean inReturnValidPeriodFlag;


	@Schema(description = "命中的限时折扣活动id")
	private Long limitDiscountsMarketingId;


	@Schema(description = "限时折扣减免金额")
	private BigDecimal limitDiscountsMarketingPrice;


	@Schema(description = "命中的满减活动id")
	private Long rewardMarketingId;


	@Schema(description = "满减促销减免金额")
	private BigDecimal rewardMarketingPrice;


	@Schema(description = "总促销优惠")
	private BigDecimal discountPrice;


	@Schema(description = "促销信息")
	private String marketingInfo;


	@Schema(description = "计算了促销活动之后的价格（包括限时折扣促销 ，满折满减）")
	private BigDecimal discountedPrice;


	@Schema(description = "使用的平台优惠券的优惠金额")
	private BigDecimal platformCouponOffPrice;


	@Schema(description = "使用的优惠券优惠金额")
	private BigDecimal couponOffPrice;

	/**
	 * 实际金额
	 */
	@Schema(description = "实际金额")
	private BigDecimal actualAmount;

	/**
	 * 库存计数方式，false：拍下减库存，true：付款减库存
	 */
	@Schema(description = "库存计数方式，false：拍下减库存，true：付款减库存")
	private Boolean stockCounting;

	@Schema(description = "是否有分销，1：分销单，0：非分销单")
	private Boolean distFlag;

	@Schema(description = "是否计算过分销")
	private Boolean distCalcFlag;

	/**
	 * OrderDistTypeEnum
	 */
	@Schema(description = "分销类型：1：推广分佣  2：链路分佣")
	private Integer distType;

	/**
	 * 分销商品分销比例
	 */
	@Schema(description = "分销商品分销比例")
	private BigDecimal distRatio;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private Date createTime;

	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	private Date updateTime;


	@Schema(description = "使用积分")
	private Integer integral;

	@Schema(description = "抵扣金额")
	private BigDecimal deductionAmount;


	@Schema(description = "结算价")
	private BigDecimal settlementPrice;

	/**
	 * 退换货有效时间(单位：天)
	 */
	@Schema(description = "退换货有效时间(单位：天)")
	private Integer returnValidPeriod;

	@Schema(description = "退换货截止时间")
	private Date returnDeadline;

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

	@Schema(description = "物料URL")
	private String materialUrl;

	@Schema(description = "商品状态")
	private Integer productStatus;

	/**
	 * 物流状态:1为待发货,3为待签收，5为待收货，7为已收货 {@link com.legendshop.order.enums.OrderRefundReturnStatusEnum}
	 */
	@Schema(description = "物流状态:1为待发货,3为待签收，5为待收货，7为已收货")
	private Integer goodsStatus;

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
	 * 来源
	 */
	@Schema(description = "来源")
	private String source;
}
