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
 * 订单项(orderItem)实体类
 *
 * @author legendshop
 */
@Data
@Entity
@Table(name = "ls_order_item")
public class OrderItem extends BaseEntity implements GenericEntity<Long> {

	private static final long serialVersionUID = 204664674559422540L;

	/**
	 * 订单项ID
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "generator")
	@TableGenerator(name = "generator", pkColumnValue = "ORDER_ITEM_SEQ")
	private Long id;


	/**
	 * 订单id
	 */
	@Column(name = "order_id")
	private Long orderId;


	/**
	 * 订单号
	 */
	@Column(name = "order_number")
	private String orderNumber;


	/**
	 * 用户Id
	 */
	@Column(name = "user_id")
	private Long userId;


	/**
	 * 产品ID
	 */
	@Column(name = "product_id")
	private Long productId;


	/**
	 * 产品SkuID
	 */
	@Column(name = "sku_id")
	private Long skuId;


	/**
	 * 订单项流水号
	 */
	@Column(name = "order_item_number")
	private String orderItemNumber;


	/**
	 * 商品快照id
	 */
	@Column(name = "snapshot_id")
	private Long snapshotId;


	/**
	 * 购物车产品个数
	 */
	@Column(name = "basket_count")
	private Integer basketCount;


	/**
	 * 产品名称
	 */
	@Column(name = "product_name")
	private String productName;


	/**
	 * 产品动态属性
	 */
	@Column(name = "attribute")
	private String attribute;


	/**
	 * 产品主图片路径
	 */
	@Column(name = "pic")
	private String pic;


	/**
	 * 产品原价
	 */
	@Column(name = "original_price")
	private BigDecimal originalPrice;


	/**
	 * 产品现价
	 */
	@Column(name = "price")
	private BigDecimal price;

	/**
	 * 成本价
	 */
	@Column(name = "cost_price")
	private BigDecimal costPrice;


	/**
	 * 商品总金额
	 */
	@Column(name = "product_total_amount")
	private BigDecimal productTotalAmount;


	/**
	 * 获得积分
	 */
	@Column(name = "obtain_integral")
	private Integer obtainIntegral;


	/**
	 * 物流重量(千克)
	 */
	@Column(name = "weight")
	private Double weight;


	/**
	 * 物流体积(立方米)
	 */
	@Column(name = "volume")
	private Double volume;


	/**
	 * 评论状态： 0 未评价  1 已评价
	 */
	@Column(name = "comm_flag")
	private Boolean commFlag;


	/**
	 * 佣金结算状态: 0 待结算  10 已结算   -10 已取消  -20未付款
	 */
	@Column(name = "commission_settle_status")
	private Integer commissionSettleStatus;


	/**
	 * 是否有分销，1：分销单，0：非分销单
	 */
	@Column(name = "dist_flag")
	private Boolean distFlag;


	/**
	 * 是否计算过分销
	 */
	@Column(name = "dist_calc_flag")
	private Boolean distCalcFlag;

	/**
	 * 分销类型：1：推广分佣  2：链路分佣
	 */
	@Column(name = "dist_type")
	private Integer distType;

	/**
	 * 分销商品分销比例
	 */
	@Column(name = "dist_ratio")
	private BigDecimal distRatio;

	/**
	 * 佣金返佣类型: CASH 现金, INTEGRAL 积分
	 */
	@Column(name = "commission_type")
	private String commissionType;

	/**
	 * 返佣的形式为积分时, 每多少元
	 */
	@Column(name = "each_commission_rmb")
	private BigDecimal eachCommissionRmb;

	/**
	 * 返佣的形式为积分时, 多少积分
	 */
	@Column(name = "commission_integral")
	private Integer commissionIntegral;

	/**
	 * 结算方式 0 ： 收货后  1：售后期结束
	 */
	@Column(name = "commission_settlement_type")
	private String commissionSettlementType;


	/**
	 * 退款记录ID
	 */
	@Column(name = "refund_id")
	private Long refundId;


	/**
	 * 0:没发起过退款,1:在处理,2:处理完成,-1:不同意
	 */
	@Column(name = "refund_status")
	private Integer refundStatus;


	/**
	 * 退款金额
	 */
	@Column(name = "refund_amount")
	private BigDecimal refundAmount;


	/**
	 * 申请类型:1:仅退款,2:退款退货
	 */
	@Column(name = "refund_type")
	private Integer refundType;


	/**
	 * 退货数量
	 */
	@Column(name = "refund_count")
	private Integer refundCount;


	/**
	 * 库存计数方式，false：拍下减库存，true：付款减库存
	 */
	@Column(name = "stock_counting")
	private Boolean stockCounting;


	/**
	 * 命中的限时折扣活动id
	 */
	@Column(name = "limit_discounts_marketing_id")
	private Long limitDiscountsMarketingId;


	/**
	 * 限时折扣减免金额
	 */
	@Column(name = "limit_discounts_marketing_price")
	private BigDecimal limitDiscountsMarketingPrice;


	/**
	 * 命中的满减活动id
	 */
	@Column(name = "reward_marketing_id")
	private Long rewardMarketingId;


	/**
	 * 满减促销减免金额
	 */
	@Column(name = "reward_marketing_price")
	private BigDecimal rewardMarketingPrice;


	/**
	 * 总促销优惠
	 */
	@Column(name = "discount_price")
	private BigDecimal discountPrice;


	/**
	 * 促销信息
	 */
	@Column(name = "marketing_info")
	private String marketingInfo;

	/**
	 * 计算了促销活动之后的价格（包括限时折扣促销 ，满折满减）
	 */
	@Column(name = "discounted_price")
	private BigDecimal discountedPrice;

	/**
	 * 使用的店铺优惠券优惠金额
	 */
	@Column(name = "coupon_off_price")
	private BigDecimal couponOffPrice;

	/**
	 * 使用的平台优惠券优惠金额
	 */
	@Column(name = "platform_coupon_off_price")
	private BigDecimal platformCouponOffPrice;

	/**
	 * 计算营销活动和优惠券后的真实价值
	 */
	@Column(name = "actual_amount")
	private BigDecimal actualAmount;


	/**
	 * 使用积分
	 */
	@Column(name = "integral")
	private Integer integral;

	/**
	 * 退换货有效时间(单位：天)
	 */
	@Column(name = "return_valid_period")
	private Integer returnValidPeriod;

	/**
	 * 退换货截止时间
	 */
	@Column(name = "return_deadline")
	private Date returnDeadline;

	/**
	 * 抵扣金额
	 */
	@Column(name = "deduction_amount")
	private BigDecimal deductionAmount;

	/**
	 * 结算价
	 */
	@Column(name = "settlement_price")
	private BigDecimal settlementPrice;


	/**
	 * 自购省金额
	 */
	@Column(name = "self_purchase_amount")
	private BigDecimal selfPurchaseAmount;

	/**
	 * 自购省总金额
	 */
	@Column(name = "self_purchase_total_amount")
	private BigDecimal selfPurchaseTotalAmount;

	/**
	 * 分销的佣金金额
	 */
	@Column(name = "dist_commission_cash")
	private BigDecimal distCommissionCash;

	/**
	 * 物料URL
	 */
	@Column(name = "material_url")
	private String materialUrl;
}
