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
import cn.hutool.core.util.ObjectUtil;
import com.legendshop.activity.dto.ShopCouponDTO;
import com.legendshop.user.bo.UserInvoiceBO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 提交订单，店铺订单DTO
 *
 * @author legendshop
 */
@Schema(description = "提交订单，店铺订单DTO")
@Data
public class SubmitOrderShopDTO implements Serializable {

	private static final long serialVersionUID = 4312264372068085475L;

	/**
	 * 店铺信息
	 */
	@Schema(description = "店铺ID")
	private Long shopId;

	@Schema(description = "用户ID")
	private Long userId;

	@Schema(description = "店铺名称")
	private String shopName;

	/**
	 * 发票信息
	 */
	@Schema(description = "商家是否允许开具发票")
	private Boolean invoiceFlag;

	@Schema(description = "商家支持开具的发票类")
	private List<String> invoiceTypeList;

	@Schema(description = "用户是否选择开具发票,默认不开具")
	private Boolean userInvoiceFlag;

	@Schema(description = "用户选择的发票ID")
	private Long invoiceId;

	@Schema(description = "发票信息")
	private UserInvoiceBO userInvoiceBo;

	/**
	 * 订单item
	 */
	@Schema(description = "下单商品信息集合")
	private List<SubmitOrderSkuDTO> skuList;

	/**
	 * 选择的优惠券集合
	 */
	@Schema(description = "下单选择商家优惠劵对象")
	private ShopCouponDTO shopCouponDTO;

	@Schema(description = "订单备注")
	private String remark;

	@Schema(description = "商品总金额")
	private BigDecimal productTotalAmount;

	@Schema(description = "促销优惠金额")
	private BigDecimal discountAmount;

	@Schema(description = "优惠券金额")
	private BigDecimal couponAmount;

	@Schema(description = "平台优惠券金额")
	private BigDecimal platformCouponAmount;

	@Schema(description = "运费金额")
	private BigDecimal deliveryAmount;

	@Schema(description = "商家平台优惠券前应付总金额")
	private BigDecimal shopOrderAmountBeforePlatformCoupon;

	@Schema(description = "商家平台优惠券前应付总金额(不含运费)")
	private BigDecimal shopOrderAmountBeforePlatformCouponNoFreight;

	@Schema(description = "预售商家平台优惠券前应付总金额")
	private BigDecimal preShopOrderAmountCoupon;

	@Schema(description = "商家订单应付总金额")
	private BigDecimal shopOrderAmount;

	@Schema(description = "商家订单应付总金额(不含运费)")
	private BigDecimal shopOrderAmountNoFreight;

	@Schema(description = "总抵扣金额")
	private BigDecimal productTotalDeductionAmount;

	@Schema(description = "总积分")
	private BigDecimal productTotalIntegral;

	@Schema(description = "结算价")
	private BigDecimal totalSettlementPrice;

	@Schema(description = "兑换积分,比例 x:1")
	private BigDecimal proportion;

	@Schema(description = "会员优惠")
	private BigDecimal selfPurchaseAmount;

	@Schema(description = "原定金")
	private BigDecimal originalDepositPrice;

	@Schema(description = "原尾款")
	private BigDecimal originalFinalPrice;

	@Schema(description = "最终应付定金")
	private BigDecimal depositPrice;

	@Schema(description = "最终应付尾款")
	private BigDecimal finalPrice;

	public SubmitOrderShopDTO() {
		this.platformCouponAmount = BigDecimal.ZERO;
		this.discountAmount = BigDecimal.ZERO;
		this.couponAmount = BigDecimal.ZERO;
		this.deliveryAmount = BigDecimal.ZERO;
		this.productTotalAmount = BigDecimal.ZERO;
		this.shopOrderAmount = BigDecimal.ZERO;
		this.shopOrderAmountNoFreight = BigDecimal.ZERO;
		this.selfPurchaseAmount = BigDecimal.ZERO;
		this.skuList = new ArrayList<>();
		this.userInvoiceFlag = false;
		this.proportion = BigDecimal.ZERO;
		this.totalSettlementPrice = BigDecimal.ZERO;
	}


	/**
	 * 商品总金额
	 */
	public BigDecimal getProductTotalAmount() {
		return skuList.stream().map(sku -> NumberUtil.mul(sku.getPrice(), sku.getTotalCount())).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * 积分兑换总积分
	 */
	public BigDecimal getProductTotalIntegral() {
		return skuList.stream().filter(s -> ObjectUtil.isNotNull(s.getIntegral())).map(SubmitOrderSkuDTO::getTotalIntegral).map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * 积分可兑换总积分
	 */
	public BigDecimal getShouldProductTotalIntegral() {
		return skuList.stream().filter(s -> ObjectUtil.isNotNull(s.getIntegral())).map(SubmitOrderSkuDTO::getTotalIntegral).map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * 商品抵扣总积分
	 */
	public BigDecimal getProductTotalDeductionIntegral() {
		return skuList.stream()
				.filter(s -> ObjectUtil.isNotNull(s.getDeductionFlag()) && s.getDeductionFlag()
						&& ObjectUtil.isNotNull(s.getIntegral()))
				// 积分计算的时候已经算上商品数量了
				.map(SubmitOrderSkuDTO::getTotalIntegral)
				.map(BigDecimal::new)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * 商品可抵扣总积分
	 */
	public BigDecimal getShouldProductTotalDeductionIntegral() {
		return skuList.stream()
				.filter(s -> ObjectUtil.isNotNull(s.getDeductionFlag())
						&& ObjectUtil.isNotNull(s.getIntegral()))
				.map(SubmitOrderSkuDTO::getTotalIntegral)
				.map(BigDecimal::new).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * 商品抵扣总金额
	 */
	public BigDecimal getProductTotalDeductionAmount() {
		return skuList.stream().filter(s -> ObjectUtil.isNotNull(s.getDeductionFlag()) && s.getDeductionFlag() && ObjectUtil.isNotNull(s.getDeductionAmount())).map(SubmitOrderSkuDTO::getDeductionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * 商品可抵扣总金额
	 */
	public BigDecimal getShouldTotalDeductionAmount() {
		return skuList.stream().filter(s -> ObjectUtil.isNotNull(s.getDeductionFlag()) && ObjectUtil.isNotNull(s.getDeductionAmount())).map(SubmitOrderSkuDTO::getDeductionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * 商品积分总结算价
	 */
	public BigDecimal getTotalSettlementPrice() {
		return skuList.stream().filter(s -> ObjectUtil.isNotNull(s.getExchangeFlag()) && s.getExchangeFlag() && ObjectUtil.isNotNull(s.getSettlementPrice())).map(sku -> sku.getSettlementPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * 商品自购省总金额
	 */
	public BigDecimal getProductTotalSelfPurchaseAmount() {
		return skuList.stream().filter(SubmitOrderSkuDTO::getDistFlag).map(SubmitOrderSkuDTO::getSelfPurchaseTotalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * 商家应付总金额 = 商品总金额 -（促销优惠金额+优惠券优惠金额+自购省金额+积分抵扣）
	 * 运费金额不与优惠互减
	 */
	public BigDecimal getShopOrderAmount() {
		BigDecimal total = NumberUtil.sub(this.getProductTotalAmount(), NumberUtil.add(this.discountAmount, this.couponAmount, this.platformCouponAmount, getProductTotalSelfPurchaseAmount(), getProductTotalDeductionAmount()));
		if (total.compareTo(BigDecimal.ZERO) < 0) {
			total = BigDecimal.ZERO;
		}
		total = total.add(this.deliveryAmount);
		return total;
	}

	/**
	 * 商家应付总金额 = 商品总金额 -（促销优惠金额+优惠券优惠金额+自购省金额+积分抵扣）
	 * 运费金额不与优惠互减 (不含运费)
	 */
	public BigDecimal getShopOrderAmountNoFreight() {
		BigDecimal total = NumberUtil.sub(this.getProductTotalAmount(), NumberUtil.add(this.discountAmount, this.couponAmount, this.platformCouponAmount, getProductTotalSelfPurchaseAmount(), getProductTotalDeductionAmount()));
		return total.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : total;
	}

	/**
	 * 商品促销及优惠券后的价格 = 商品总金额 - 促销金额 - 店铺优惠券金额
	 */
	public BigDecimal getShopCouponAfterAmount() {
		BigDecimal total = NumberUtil.sub(this.getProductTotalAmount(), this.discountAmount, this.couponAmount);
		return total.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : total;
	}

	/**
	 * 获取会员总优惠
	 *
	 * @return
	 */
	public BigDecimal getSelfPurchaseAmount() {
		return getProductTotalSelfPurchaseAmount();
	}

	/**
	 * 商家应付券前总金额 = 商品总金额-（促销优惠金额）
	 *
	 * @return
	 */
	public BigDecimal getShopOrderAmountBeforeSubCoupon() {
		BigDecimal total = NumberUtil.sub(this.getProductTotalAmount(), this.discountAmount);
		BigDecimal zero = new BigDecimal(0);
		return total.compareTo(zero) < 0 ? zero : total;
	}

	/**
	 * 商家平台优惠券前应付总金额 = 商品总金额-(促销优惠金额+店铺优惠券+自购省)
	 * 运费不与优惠互减
	 *
	 * @return
	 */
	public BigDecimal getShopOrderAmountBeforePlatformCoupon() {
		BigDecimal total = NumberUtil.sub(this.getProductTotalAmount(), NumberUtil.add(this.discountAmount, this.couponAmount, getProductTotalSelfPurchaseAmount(), getProductTotalDeductionAmount()));
		if (total.compareTo(BigDecimal.ZERO) < 0) {
			total = BigDecimal.ZERO;
		}
		total = total.add(this.deliveryAmount);
		return total;
	}


	/**
	 * 商家平台优惠券前应付总金额 = 商品总金额-(促销优惠金额+店铺优惠券+自购省)
	 * 运费不与优惠互减	(不含运费)
	 *
	 * @return
	 */
	public BigDecimal getShopOrderAmountBeforePlatformCouponNoFreight() {
		BigDecimal total = NumberUtil.sub(this.getProductTotalAmount(), NumberUtil.add(this.discountAmount, this.couponAmount, getProductTotalSelfPurchaseAmount()));
		return total.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : total;
	}


	/**
	 * 商家店铺优惠券后应付总金额 = 商品总金额 -（促销优惠金额+店铺优惠券）
	 *
	 * @return
	 */
	public BigDecimal getShopOrderAmountAfterShopCoupon() {
		BigDecimal total = NumberUtil.sub(this.getProductTotalAmount(), NumberUtil.add(this.discountAmount, this.couponAmount));
		return total.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : total;
	}
}


