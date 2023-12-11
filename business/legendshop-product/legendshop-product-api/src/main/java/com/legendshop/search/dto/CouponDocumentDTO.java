/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.search.dto;

import com.legendshop.activity.enums.CouponDesignateEnum;
import com.legendshop.activity.enums.CouponStatusEnum;
import com.legendshop.activity.enums.CouponUseTypeEnum;
import com.legendshop.common.core.annotation.EnumValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author legendshop
 */
@Data
public class CouponDocumentDTO implements Serializable {

	/**
	 * 主键
	 */
	private Long id;

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
	 * sku ids
	 */
	@Schema(description = "选中的skuId")
	private List<Long> skuIds;

	@Schema(description = "选中的店铺ID")
	private List<Long> shopIds;

	/**
	 * 指定用户{@link com.legendshop.activity.enums.CouponDesignateEnum}
	 */
	@Schema(description = "指定用户")
	@EnumValid(target = CouponDesignateEnum.class, message = "指定用户类型不匹配")
	private Integer designatedUser;


	/**
	 * 优惠券提供方是否为店铺
	 */
	private Boolean shopProviderFlag;

	/**
	 * 优惠券使用类型
	 * {@link com.legendshop.activity.enums.CouponUseTypeEnum}
	 */
	@EnumValid(target = CouponUseTypeEnum.class, message = "优惠券使用类型不匹配")
	@Schema(description = "优惠券使用类型：0全场通用，1指定使用，-1排除使用")
	private Integer useType;


	/**
	 * 状态：{@link com.legendshop.activity.enums.CouponStatusEnum}
	 */
	@EnumValid(target = CouponStatusEnum.class, message = "状态类型不匹配")
	@Schema(description = "状态:-1：已失效 0：未开始 1：进行中 2：已暂停 3：已结束")
	private Integer status;

	/**
	 * 优惠券标题
	 */
	@Length(max = 25, message = "优惠券标题字符长度不能超过25个")
	@NotBlank(message = "优惠券标题不能为空")
	private String title;

	/**
	 * 优惠券备注
	 */
	private String remark;


	/**
	 * 面额
	 */
	@DecimalMin(value = "0.00", message = "优惠券面额不能小于0")
	@Digits(integer = 6, fraction = 2, message = "优惠券面额保留2位小数且不能大于6位数")
	private BigDecimal amount;

	/**
	 * 使用门槛，0.00为无门槛
	 */
	@Digits(integer = 6, fraction = 2, message = "优惠券使用门槛保留2位小数且不能大于6位数")
	@Schema(description = "使用门槛，0.00为无门槛")
	private BigDecimal minPoint;


	/**
	 * 领取开始时间
	 */
	@NotNull(message = "领取开始时间不能为空")
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


}
