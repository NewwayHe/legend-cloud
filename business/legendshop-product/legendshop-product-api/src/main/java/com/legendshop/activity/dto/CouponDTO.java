/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.legendshop.activity.enums.CouponDesignateEnum;
import com.legendshop.activity.enums.CouponReceiveTypeEnum;
import com.legendshop.activity.enums.CouponStatusEnum;
import com.legendshop.activity.enums.CouponUseTypeEnum;
import com.legendshop.common.core.annotation.DateCompareValid;
import com.legendshop.common.core.annotation.EnumValid;
import com.legendshop.common.core.dto.BaseDTO;
import com.legendshop.product.dto.ProductItemDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.legendshop.common.core.annotation.DateCompareValid.DateCompareTarget;
import static com.legendshop.common.core.annotation.DateCompareValid.DateCompareType;

/**
 * 优惠券(Coupon)DTO
 *
 * @author legendshop
 * @since 2020-09-10 11:22:45
 */
@Data
@DateCompareValid(message = "领取开始时间不能小于领取结束时间")
@Schema(description = "优惠券DTO")
public class CouponDTO extends BaseDTO implements Serializable {

	private static final long serialVersionUID = 4759778001127025787L;

	/**
	 * 优惠券id
	 */
	@Schema(description = "优惠券id")
	private Long couponId;


	/**
	 * 店铺id
	 */
	@Schema(description = "店铺id")
	private Long shopId;

	/**
	 * 店铺名称
	 */
	@Schema(description = "店铺名称")
	private String shopName;

	/**
	 * 优惠券提供方是否为店铺
	 */
	@Schema(description = "优惠券提供方是否为店铺")
	private Boolean shopProviderFlag;

	/**
	 * 优惠券标题
	 */
	@Length(max = 25, message = "优惠券标题字符长度不能超过25个")
	@NotBlank(message = "优惠券标题不能为空")
	@Schema(description = "优惠券标题")
	private String title;

	/**
	 * 优惠券备注
	 */
	@Schema(description = "优惠券备注")
	private String remark;

	/**
	 * 发放数量
	 */

	@Schema(description = "发放数量")
	@Min(value = 1, message = "发放数量不能少于1张")
	@Max(value = 1000000, message = "发放数量不能大于100万张")
	private Integer count;

	/**
	 * 已使用优惠券数量
	 */
	@Schema(description = "已使用优惠券数量")
	private Integer useCount;


	/**
	 * 已领取数量
	 */
	@Schema(description = "已领取优惠券数量")
	private Integer receiveCount;

	/**
	 * 已领取优惠券数量百分比
	 */
	@Schema(description = "已领取优惠券数量百分比")
	private String receiveCountPercent;

	/**
	 * 状态：{@link com.legendshop.activity.enums.CouponStatusEnum}
	 */
	@EnumValid(target = CouponStatusEnum.class, message = "状态类型不匹配")
	@Schema(description = "状态:-1：已失效 0：未开始 1：进行中 2：已暂停 3：已结束")
	private Integer status;

	/**
	 * 面额
	 */
	@Schema(description = "优惠券面额")
	@DecimalMin(value = "0.00", message = "优惠券面额不能小于0")
	@Digits(integer = 6, fraction = 2, message = "优惠券面额保留2位小数且不能大于6位数")
	private BigDecimal amount;

	/**
	 * 使用门槛，0.00为无门槛
	 */
	@DecimalMin(value = "0.00", message = "优惠券门槛不能小于0")
	@Digits(integer = 6, fraction = 2, message = "优惠券使用门槛保留2位小数且不能大于6位数")
	@Schema(description = "使用门槛，0.00为无门槛")
	private BigDecimal minPoint;

	/**
	 * 领取开始时间
	 */
	@NotNull(message = "领取开始时间不能为空")
	@DateCompareTarget(type = DateCompareType.BEFORE, targetField = "receiveEndTime")
	@Schema(description = "领取开始时间")
	private Date receiveStartTime;

	/**
	 * 领取结束时间
	 */
	@NotNull(message = "领取结束时间不能为空")
	@Schema(description = "领取结束时间")
	private Date receiveEndTime;

	/**
	 * 使用开始时间
	 */
	@Schema(description = "使用开始时间")
	private Date useStartTime;

	/**
	 * 使用结束时间
	 */
	@Schema(description = "使用结束时间")
	private Date useEndTime;

	/**
	 * 多少天后可以使用
	 */
	@Schema(description = "多少天后可以使用")
	@Min(value = 0, message = "多少天后可以使用不能小于0")
	@Max(value = 30, message = "多少天后可以使用不能大于30")
	private Integer useDayLater;

	/**
	 * 几天之内可用
	 */
	@Schema(description = "几天之内可用")
	@Min(value = 0, message = "几天之内可用不能小于0")
	@Max(value = 30, message = "几天之内可用不能大于30")
	private Integer withinDay;

	/**
	 * 优惠券使用类型
	 * {@link com.legendshop.activity.enums.CouponUseTypeEnum}
	 */
	@EnumValid(target = CouponUseTypeEnum.class, message = "优惠券使用类型不匹配")
	@Schema(description = "优惠券使用类型：0全场通用，1指定使用，-1排除使用")
	private Integer useType;

	/**
	 * sku商品列表
	 */
	@Schema(description = "sku商品列表")
	private List<Long> skuIdList;

	/**
	 * 领取方式
	 * {@link com.legendshop.activity.enums.CouponReceiveTypeEnum}
	 */
	@EnumValid(target = CouponReceiveTypeEnum.class, message = "领取方式不匹配")
	@Schema(description = "领取方式，0，免费领取，1，卡密领取，2，积分兑换")
	private Integer receiveType;

	/**
	 * 每人每天限领张数
	 */
	@Schema(description = "每人每天限领张数")
	@Min(value = 0, message = "每人每天限领张数不能小于0")
	@Max(value = 999999, message = "每人每天限领张数不能大于999999")
	@NotNull(message = "每人每天限领张数不能为空")
	private Integer perDayLimit;

	/**
	 * 每人总限领张数
	 */
	@Schema(description = "每人总限领张数")
	@Min(value = 0, message = "每人总限领张数不能小于0")
	@Max(value = 999999, message = "每人总限领张数不能大于999999")
	@NotNull(message = "每人总限领张数不能为空")
	private Integer perTotalLimit;

	/**
	 * 积分兑换数
	 */
	@Schema(description = "积分兑换数")
	private Integer integral;

	/**
	 * 用户领取标识
	 */
	@Schema(description = "用户领取标识")
	private boolean receivedFlag;

	/**
	 * 优惠券使用状态
	 */
	@Schema(description = "优惠券使用标识")
	private Boolean useFlag;

	/**
	 * 优惠券描述
	 */
	@Schema(description = "优惠券描述")
	private String description;

	/**
	 * 优惠券图片
	 */
	@NotBlank(message = "优惠券图片不能为空")
	@Schema(description = "优惠券图片")
	private String pic;


	/**
	 * 指定用户{@link com.legendshop.activity.enums.CouponDesignateEnum}
	 */
	@Schema(description = "指定用户")
	@EnumValid(target = CouponDesignateEnum.class, message = "指定用户类型不匹配")
	private Integer designatedUser;

	/**
	 * 指定用户手机号
	 */
	@Schema(description = "指定用户手机号", hidden = true)
	private String designatedUserMobile;

	/**
	 * 指定的用户手机号码
	 */
	@Schema(description = "指定的用户手机号码")
	private String mobileList;

	/**
	 * 提交订单未付款可退还
	 */
	@Schema(description = "提交订单未付款可退还")
	private Boolean nonPaymentRefundableFlag;

	/**
	 * 生成订单申请退款可退还
	 */
	@Schema(description = "生成订单申请退款可退还")
	private Boolean paymentRefundableFlag;

	/**
	 * 生成订单全部商品申请售后可退还
	 */
	@Schema(description = "生成订单全部商品申请售后可退还")
	private Boolean paymentAllAfterSalesRefundableFlag;

	public List<String> getMobile() {
		return ObjectUtil.isNotEmpty(getMobileList()) ? ListUtil.toList(getMobileList().split(",")) : null;
	}

	/**
	 * 指定的用户手机号码
	 */
	private List<String> mobile;

	@Schema(description = "优惠券对应的商品列表")
	private List<ProductItemDTO> productItems;

	@Schema(description = "优惠券对应的店铺列表")
	private List<Long> shopItems;

	@Schema(description = "优惠券对应的已勾选商品id列表")
	private List<Long> selectSkuId;

	@Schema(description = "优惠券对应的未勾选商品id列表")
	private List<Long> unSelectSkuId;

	@Schema(description = "优惠券对应的已勾选店铺id列表")
	private List<Long> selectShopId;

	@Schema(description = "用户优惠券信息")
	private CouponUserDTO couponUserDTO;

	@Schema(description = "历史信息")
	private List<CouponHistoryDTO> historyList;


	/**
	 * 平台删除状态
	 */
	@Schema(description = "平台删除状态")
	private Boolean platformDeleteStatus;
}
