/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.query;

import cn.legendshop.jpaplus.support.PageParams;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 优惠券(Coupon)Query分页查询对象
 *
 * @author legendshop
 * @since 2020-09-10 11:00:04
 */
@Data
@Schema(description = "优惠券查询参数")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponQuery extends PageParams {

	private static final long serialVersionUID = -78329626977169103L;

	/**
	 * 优惠券Id
	 */
	@Schema(description = "优惠券Id")
	Long couponId;

	/**
	 * 礼券提供方是否为店铺
	 */
	@Schema(description = "礼券提供方是否为店铺")
	Boolean shopProviderFlag;

	/**
	 * 用户Id
	 */
	@Schema(description = "用户Id")
	Long userId;

	/**
	 * 店铺Id
	 */
	@Schema(description = "店铺Id")
	Long shopId;

	/**
	 * 商品Id
	 */
	@Schema(description = "商品Id")
	List<Long> skuIds;

	/**
	 * 优惠券名称
	 */
	@Schema(description = "优惠券名称")
	String name;

	/**
	 * 领取日期范围
	 */
	@Schema(description = "领取日期范围")
	List<Date> receiveTime = new ArrayList<>(2);

	/**
	 * 使用日期范围
	 */
	@Schema(description = "使用日期范围")
	List<Date> useTime = new ArrayList<>(2);

	/**
	 * 领取开始日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
	Date receiveTimeBeg;

	/**
	 * 领取结束日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
	Date receiveTimeEnd;

	/**
	 * 使用开始日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
	Date useTimeBeg;

	/**
	 * 使用结束日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
	Date useTimeEnd;

	/**
	 * 注册时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
	Date registerTime;

	/**
	 * 领取方式
	 * {@link com.legendshop.activity.enums.CouponReceiveTypeEnum}
	 */
	@Schema(description = "领取方式")
	private Integer receiveType;

	/**
	 * 优惠券使用类型
	 * {@link com.legendshop.activity.enums.CouponUseTypeEnum}
	 */
	@Schema(description = "优惠券使用商品类型：0全场通用，1指定商品使用，-1排除商品使用")
	Integer useType;

	/**
	 * 状态：{@link com.legendshop.activity.enums.CouponStatusEnum}
	 */
	@Schema(description = "状态:  -2:已删除, -1：已失效, 0：未开始, 1：进行中, 2：已暂停, 3：已结束")
	private Integer status;

	@Schema(description = "店铺名称")
	private String shopName;

	@Schema(description = "是平台调用")
	private Boolean isPlatform;
}
