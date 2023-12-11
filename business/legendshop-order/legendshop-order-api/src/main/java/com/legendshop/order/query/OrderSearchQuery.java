/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.query;

import cn.legendshop.jpaplus.support.PageParams;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 订单查询的参数
 *
 * @author legendshop
 */
@Schema(description = "订单查询参数")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderSearchQuery extends PageParams {

	private static final long serialVersionUID = 8434954261109197313L;

	/**
	 * 订单状态
	 * {@link com.legendshop.order.enums.OrderStatusEnum}
	 */
	@Schema(description = "订单状态 全部（0）待付款（1）、待成团（2）、待发货（5）、待签收（10）、待收货（15）、已完成（20）、已取消（-5）")
	private Integer status;

	/**
	 * 订单号
	 */
	@Schema(description = "订单号")
	private String orderNumber;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private Long userId;


	/**
	 * 自提点ID
	 */
	@Schema(description = "自提点ID")
	private Long pointId;

	/**
	 * 商家ID
	 */
	@Schema(description = "商家ID")
	private Long shopId;

	@Schema(description = "用户昵称")
	private String nickName;

	/**
	 * 商家名称
	 */
	@Schema(description = "商家名称")
	private String shopName;

	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String productName;

	/**
	 * 开始时间-用于查询某段时间范围产生的订单
	 */
	@Schema(description = "开始时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date startDate;

	/**
	 * 结束时间-用于查询某段时间范围产生的订单
	 */
	@Schema(description = "结束时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date endDate;

	/**
	 * 订单类型 : 普通订单,团购订单,秒杀订单等等
	 * {@link com.legendshop.order.enums.OrderTypeEnum}
	 */
	@Schema(description = "订单类型 [O:普通订单， P：预售订单,G:团购订单,S:秒杀订单,MG:拼团订单]")
	private String orderType;


	@Schema(description = "营销活动ID[秒杀、团购、拼团、拍卖]")
	private Long activityId;

	/**
	 * 付款开始时间-用于查询某段时间范围产生的订单
	 */
	@Schema(description = "付款开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date payStartDate;

	/**
	 * 付款结束时间-用于查询某段时间范围产生的订单
	 */
	@Schema(description = "付款结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date payEndDate;

	/**
	 * 买家电话
	 */
	@Schema(description = "买家电话")
	private String userMobile;

	/**
	 * 是否添加商家备注
	 */
	@Schema(description = "是否添加商家备注")
	private Boolean shopRemarkFlag;

	/**
	 * 退款状态
	 */
	@Schema(description = "退款状态 未退款（0）、退货退款处理中（1）、已完成（2）")
	private Integer refundStatus;

	/**
	 * 收货人电话
	 */
	@Schema(description = "收货人电话")
	private String receiverMobile;

	/**
	 * 收货人名称
	 */
	@Schema(description = "收货人名称")
	private String receiverName;

	/**
	 * 用于缓存
	 */
	@Schema(description = "用于缓存")
	private Integer deleteStatus = 0;

	/**
	 * 是否包含用户删除的订单
	 */
	@Schema(description = "是否包含用户删除的订单")
	private boolean includeDeleted = Boolean.FALSE;

	/**
	 * 是否已经支付
	 */
	@Schema(description = "是否已经支付")
	private Boolean payedFlag;

	@Schema(description = "下单时商家是否支持开具发票")
	private Boolean invoiceFlag;

	@Schema(description = "账单id")
	private Long shopOrderBillId;

	@Schema(description = "结算单号")
	private String billSn;

	/**
	 * 运单号
	 */
	@Schema(description = "运单号")
	private String shipmentNumber;

	@Schema(description = "选中的订单号")
	private List<String> numberList;

	@Schema(description = "是否会员")
	private Boolean isMember;
}
