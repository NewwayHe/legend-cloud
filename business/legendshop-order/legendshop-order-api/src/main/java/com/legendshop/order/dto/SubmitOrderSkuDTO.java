/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import cn.hutool.core.util.NumberUtil;
import com.legendshop.activity.dto.CouponOrderDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 提交订单SKUDTO
 *
 * @author legendshop
 */
@Schema(description = "提交订单SKU DTO")
@Data
public class SubmitOrderSkuDTO implements Serializable {

	private static final long serialVersionUID = -2159280085965706733L;

	/**
	 * 商品信息
	 */
	@Schema(description = "商品编码唯一标识")
	private Long skuId;

	@Schema(description = "商品ID")
	private Long productId;

	@Schema(description = "商品名称")
	private String productName;

	@Schema(description = "商品图片")
	private String pic;

	@Schema(description = "规格属性")
	private String cnProperties;

	@Schema(description = "原价")
	private BigDecimal originalPrice;

	@Schema(description = "销售价")
	private BigDecimal price;

	@Schema(description = "数量")
	private Integer totalCount;

	@Schema(description = "物流体积")
	private Double volume;

	@Schema(description = "物流重量(千克)")
	private Double weight;

	@Schema(description = "商品状态异常标识")
	private Boolean statusFlag;

	@Schema(description = "所属商家ID")
	private Long shopId;

	@Schema(description = "运费模板ID")
	private Long transId;

	@Schema(description = "总体积")
	private Double totalWeight;

	@Schema(description = "总重量")
	private Double totalVolume;


	@Schema(description = "库存计数方式，false：拍下减库存，true：付款减库存")
	private Boolean stockCounting;

	@Schema(description = "营销活动ID[秒杀、团购、拼团、拍卖]")
	private Long activityId;


	/**
	 * 相关金额以及优惠信息
	 */
	@Schema(description = "商品总价格")
	private BigDecimal totalPrice;

	@Schema(description = "满减活动id")
	private Long rewardMarketingId;

	@Schema(description = "满减营销减免价格")
	private BigDecimal rewardMarketingPrice;

	@Schema(description = "命中的限时折扣活动id")
	private Long limitDiscountsMarketingId;

	@Schema(description = "限时营销减免价格")
	private BigDecimal limitDiscountsMarketingPrice;

	@Schema(description = "商品促销优惠总金额")
	private BigDecimal totalDiscountAmount;

	@Schema(description = "减去促销优惠后的商品金额金额")
	private BigDecimal discountedPrice;

	@Schema(description = "满减营销信息")
	private String rewardMarketingInfo;

	@Schema(description = "限时营销减免信息")
	private String limitDiscountsMarketingInfo;

	@Schema(description = "使用的平台优惠券的优惠金额")
	private BigDecimal platformCouponAmount;

	@Schema(description = "优惠券订单项关系记录")
	private List<CouponOrderDTO> couponOrderList;

	@Schema(description = "使用的优惠券优惠金额")
	private BigDecimal couponAmount;

	@Schema(description = "订单项减去所有优惠后实际支付的金额(包含数量)")
	private BigDecimal actualAmount;

	@Schema(description = "抵扣金额")
	private BigDecimal deductionAmount;

	@Schema(description = "最大抵扣金额")
	private BigDecimal maxDeductionAmount;

	@Schema(description = "积分")
	private Integer integral;

	@Schema(description = "成本价")
	private BigDecimal costPrice;

	@Schema(description = "是否抵扣")
	private Boolean deductionFlag;

	@Schema(description = "结算价")
	private BigDecimal settlementPrice;

	@Schema(description = "是否积分兑换商品")
	private Boolean exchangeFlag;

	@Schema(description = "单件自购优惠")
	private BigDecimal selfPurchaseAmount;

	@Schema(description = "自购优惠金额")
	private BigDecimal selfPurchaseTotalAmount;

	@Schema(description = "佣金总额（包括自购优惠金额）")
	private BigDecimal distCommissionCash;

	@Schema(description = "分销模式：一级分销佣金比例")
	private BigDecimal firstCommissionRate;

	@Schema(description = "是否有分销，1：分销单，0：非分销单")
	private Boolean distFlag;

	@Schema(description = "分销类型：1：推广分佣  2：链路分佣")
	private Integer distType;

	/**
	 * 分销商品分销比例
	 */
	@Schema(description = "分销商品分销比例")
	private BigDecimal distRatio;

	/**
	 * 结算方式 0 ： 收货后  1：售后期结束
	 * DistributionSettlementType
	 */
	@Schema(description = "结算方式 0 ： 收货后  1：售后期结束")
	private String commissionSettlementType;

	@Schema(description = "物料URL")
	private String materialUrl;

	@Schema(description = "商品预售状态：1：在定金支付时间段内 0：不在定金支付时间段内")
	private Integer status;

	/**
	 * 是否预售商品
	 */
	@Schema(description = "是否预售商品")
	private Boolean preSellFlag;

	@Schema(description = "定金")
	private BigDecimal preDepositPrice;

	@Schema(description = "尾款")
	private BigDecimal finalPrice;

	/**
	 * 预售支付百分比
	 */
	@Schema(description = "预售支付百分比")
	private BigDecimal payPct;

	@Schema(description = "支付方式,0:全额,1:定金")
	private Integer payPctType;

	/**
	 * 配送类型 {@link com.legendshop.product.enums.ProductDeliveryTypeEnum}
	 */
	@Schema(description = "配送方式  0: 商家配送 10:到店自提 ")
	private Integer deliveryType;

	public SubmitOrderSkuDTO() {
		this.totalPrice = BigDecimal.ZERO;
		this.totalDiscountAmount = BigDecimal.ZERO;
		this.couponAmount = BigDecimal.ZERO;
		this.platformCouponAmount = BigDecimal.ZERO;
		this.rewardMarketingPrice = BigDecimal.ZERO;
		this.limitDiscountsMarketingPrice = BigDecimal.ZERO;
		this.discountedPrice = BigDecimal.ZERO;
		this.integral = 0;
		this.selfPurchaseAmount = BigDecimal.ZERO;
		this.selfPurchaseTotalAmount = BigDecimal.ZERO;
		this.distCommissionCash = BigDecimal.ZERO;
		this.settlementPrice = BigDecimal.ZERO;
		this.distFlag = false;
		this.deductionFlag = false;
		this.exchangeFlag = false;
		this.statusFlag = true;
		this.deductionAmount = BigDecimal.ZERO;
		this.couponOrderList = new ArrayList<>();
	}

	public BigDecimal getTotalPrice() {
		return NumberUtil.mul(this.price, this.totalCount);
	}

	public Double getTotalWeight() {
		return NumberUtil.mul(this.weight, this.totalCount).doubleValue();
	}

	public Double getTotalVolume() {
		return NumberUtil.mul(this.volume, this.totalCount).doubleValue();
	}

	/**
	 * sku减去所有优惠后应付总金额 = sku应付总金额-促销优惠金额-店铺优惠券-平台优惠券
	 *
	 * @return
	 */
	public BigDecimal getTotalActualAmount() {
		BigDecimal total = exchangeFlag ? BigDecimal.ZERO : NumberUtil.sub(getTotalPrice()).subtract(this.rewardMarketingPrice).subtract(this.limitDiscountsMarketingPrice).subtract(this.couponAmount).subtract(platformCouponAmount);
		BigDecimal zero = new BigDecimal(0);
		return total.compareTo(zero) < 0 ? zero : total;
	}

	/**
	 * 获取自购佣金后的总实付金额 = sku应付总金额-促销优惠金额-店铺优惠券-平台优惠券 - 自购佣金
	 *
	 * @return
	 */
	public BigDecimal getTotalActualAmountAfterCommission() {
		return exchangeFlag ? BigDecimal.ZERO : getTotalActualAmount().subtract(this.selfPurchaseTotalAmount);
	}

	/**
	 * 获取积分总抵扣金额
	 *
	 * @return
	 */
	public BigDecimal getTotalDeductionAmount() {
		return deductionAmount;
	}

	/**
	 * 获取抵扣总积分
	 *
	 * @return
	 */
	public Integer getTotalIntegral() {
		return integral;
	}

	/**
	 * 商家店铺优惠券后应付总金额 = 商品总金额 -（促销优惠金额+店铺优惠券）
	 *
	 * @return
	 */
	public BigDecimal getShopOrderAmountAfterShopCoupon() {
		BigDecimal total = NumberUtil.sub(this.getTotalPrice(), NumberUtil.add(this.rewardMarketingPrice, this.limitDiscountsMarketingPrice, this.couponAmount));
		return total.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : total;
	}

	/**
	 * 商家应付券前总金额 = 商品总金额-（促销优惠金额）
	 *
	 * @return
	 */
	public BigDecimal getShopOrderAmountBeforeSubCoupon() {
		BigDecimal total = NumberUtil.sub(this.getTotalPrice(), NumberUtil.add(this.rewardMarketingPrice, this.limitDiscountsMarketingPrice));
		BigDecimal zero = new BigDecimal(0);
		return total.compareTo(zero) < 0 ? zero : total;
	}
}
