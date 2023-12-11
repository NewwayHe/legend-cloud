/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.bo;

import com.legendshop.activity.dto.CouponItemDTO;
import com.legendshop.common.core.enums.VisitSourceEnum;
import com.legendshop.order.dto.InvalidOrderShopDTO;
import com.legendshop.order.dto.SubmitOrderShopDTO;
import com.legendshop.order.dto.UseWalletInfoDTO;
import com.legendshop.order.enums.OrderTypeEnum;
import com.legendshop.user.bo.UserAddressBO;
import com.legendshop.user.bo.UserContactBO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 预生成订单BO
 *
 * @author legendshop
 */
@Schema(description = "预生成订单BO")
@Data
@Getter
public class ConfirmOrderBO implements Serializable {

	private static final long serialVersionUID = 7544259870032598139L;

	@Schema(description = "用户ID")
	private Long userId;

	@Schema(description = "防止重复提交")
	private String token;

	@Schema(description = "预生成订单ID，唯一标识")
	private String id;

	@Schema(description = "订单类型")
	private OrderTypeEnum type;

	@Schema(description = "请求来源")
	private VisitSourceEnum sourceEnum;

	@Schema(description = "营销活动ID[秒杀、团购、拼团、拍卖]")
	private Long activityId;

	@Schema(description = "用户选择的收货地址Id")
	private Long addressId;

	@Schema(description = "用户收货地址")
	private UserAddressBO userAddressBO;

	@Schema(description = "用户提货信息")
	private UserContactBO userContactBO;


	@Schema(description = "区域限售标识")
	private Boolean regionalSalesFlag;

	@Schema(description = "订单留言JsonStr")
	private String orderMessage;

	@Schema(description = "店铺下单信息集合")
	private List<SubmitOrderShopDTO> shopOrderList;

	// todo 平台优惠券使用明细信息
	//	private List<CouponOrderDTO> couponOrderList;

	@Schema(description = "下单选择平台优惠劵")
	private List<CouponItemDTO> platformCoupons;

	@Schema(description = "下单不可用平台优惠劵")
	private List<CouponItemDTO> platformUnAvailableCouponList;

	@Schema(description = "平台优惠券抵扣金额")
	private BigDecimal platformAmount;

	@Schema(description = "使用平台优惠券数量")
	private Integer usePlatformCouponCount;

	@Schema(description = "总金额")
	private BigDecimal totalAmount;

	@Schema(description = "总金额(不含运费)")
	private BigDecimal totalAmountNoFreight;

	@Schema(description = "总积分")
	private Integer totalIntegral;

	@Schema(description = "可用总积分")
	private Integer shouldTotalIntegral;

	@Schema(description = "积分抵扣金额")
	private BigDecimal deductionAmount;

	@Schema(description = "积分可抵扣金额")
	private BigDecimal shouldDeductionAmount;

	@Schema(description = "失效店铺商品信息集合")
	private List<InvalidOrderShopDTO> invalidOrderShopList;

	@Schema(description = "拼团编号，用户参与拼团活动必传，用户是开团或者是普通下单则不需要传递")
	private String groupNumber;


	@Schema(description = "用户钱包余额抵扣参数")
	private UseWalletInfoDTO useWalletInfo;


	@Schema(description = "开启抵扣（普通订单的）")
	private Boolean deductionFlag;

	/**
	 * 可用积分
	 */
	@Schema(description = "可用积分")
	private Integer availableIntegral;

	@Schema(description = "物料URL")
	private String materialUrl;

	@Schema(description = "最终应付定金")
	private BigDecimal depositPrice;

	@Schema(description = "最终应付尾款")
	private BigDecimal finalPrice;


	/**
	 * 自提点id
	 */
	@Schema(description = "自提点id")
	private Long pointId;

	@Schema(description = "商家id")
	private Long shopId;
	/**
	 * 配送类型 {@link com.legendshop.product.enums.ProductDeliveryTypeEnum}
	 */
	@Schema(description = "配送方式  0: 商家配送")
	private Integer deliveryType;

	@Schema(description = "提货信息ID,如果不为空，则按此ID的提货信息提交")
	private Long contactId;

	public ConfirmOrderBO() {
		this.deductionFlag = Boolean.FALSE;
		this.platformAmount = BigDecimal.ZERO;
		this.usePlatformCouponCount = 0;
	}

	/**
	 * 计算预订单总价 = 所有商家应付总金额
	 */
	public BigDecimal getTotalAmount() {
		BigDecimal totalAmount = shopOrderList.stream()
				.map(SubmitOrderShopDTO::getShopOrderAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal zero = new BigDecimal(0);
		BigDecimal amount = OrderTypeEnum.INTEGRAL.equals(type) ? zero : totalAmount;
		return amount.compareTo(zero) < 0 ? zero : useWalletInfo == null || (!useWalletInfo.getUseWallet() && !useWalletInfo.getAllowed()) ? amount : amount.subtract(useWalletInfo.getAmount());
	}

	/**
	 * 计算预订单总价 = 所有商家应付总金额(不含运费)
	 */
	public BigDecimal getTotalAmountNoFreight() {
		BigDecimal totalAmount = shopOrderList.stream()
				.map(SubmitOrderShopDTO::getShopOrderAmountNoFreight)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal zero = new BigDecimal(0);
		BigDecimal amount = OrderTypeEnum.INTEGRAL.equals(type) ? zero : totalAmount;
		return amount.compareTo(zero) < 0 ? zero : useWalletInfo == null || (!useWalletInfo.getUseWallet() && !useWalletInfo.getAllowed()) ? amount : amount.subtract(useWalletInfo.getAmount());
	}

	public Integer getTotalIntegral() {
		BigDecimal reduce = shopOrderList.stream().map(
				e -> OrderTypeEnum.INTEGRAL.getValue().equals(type.getValue()) ? e.getProductTotalIntegral() : e.getProductTotalDeductionIntegral()
		).reduce(BigDecimal.ZERO, BigDecimal::add);
		return reduce.intValue();
	}

	public Integer getShouldTotalIntegral() {
		BigDecimal reduce = shopOrderList.stream().map(
				e -> OrderTypeEnum.INTEGRAL.getValue().equals(type.getValue()) ? e.getShouldProductTotalIntegral() : e.getShouldProductTotalDeductionIntegral()
		).reduce(BigDecimal.ZERO, BigDecimal::add);
		return reduce.intValue();
	}

	public BigDecimal getDeductionAmount() {
		return shopOrderList.stream().map(SubmitOrderShopDTO::getProductTotalDeductionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public BigDecimal getShouldDeductionAmount() {
		return shopOrderList.stream().map(SubmitOrderShopDTO::getShouldTotalDeductionAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
