/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.legendshop.order.enums.OrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author legendshop
 */
@Data
@Schema(description = "商家申请列表显示DTO")
@HeadRowHeight(20)
@ContentRowHeight(20)
public class OrderCancelReasonDTO {

	/**
	 * 主键id
	 */

	@Schema(description = "主键id")
	private Long id;

	/**
	 * 订单商品规格属性
	 */
	@Schema(description = "订单商品规格属性")
	private String productAttribute;

	/**
	 * 买家ID
	 */
	@Schema(description = "买家ID")
	private Long userId;

	/**
	 * 订单类型 O:普通订单  P:预售订单  G：团购订单  S：秒杀订单  MG：拼团订单 I:积分商品订单
	 */
	@Schema(description = "订单类型 O:普通订单  P:预售订单  G：团购订单  S：秒杀订单  MG：拼团订单 I:积分商品订单")
	private String orderType;

	/**
	 * 店铺ID
	 */
	@Schema(description = "店铺ID")
	private Long shopId;

	/**
	 * 店铺名称
	 */
	@Schema(description = "店铺名称")
	private String shopName;
	/**
	 * 用户名
	 */
	@Schema(description = "用户名")
	private String nickName;

	/**
	 * 订购ID
	 */
	@Schema(description = "订购ID")
	private Long orderId;

	/**
	 * 订单项集合
	 */
	@Schema(description = "订单项集合")
	List<OrderItemDTO> orderItemDTOList;
	/**
	 * 订单号
	 */
	@Schema(description = "订单号")
	private String orderNumber;

	/**
	 * 订单状态
	 * {@link OrderStatusEnum}
	 */
	@Schema(description = "订单状态 ：全部 0  待付款 1  待成团 2 已付定金 3  待发货 5 待签收 10  待收货 15  已完成 20  已取消 -5")
	private Integer orderStatus;
	/**
	 * 售后号
	 */
	@Schema(description = "申请状态:待卖家处理（1） 待管理员处理（2） 已完成（3）退款取消（-1）")
	private Integer sellerStatus;


	@Schema(description = "商家操作人")
	private String shopOperator;

	@Schema(description = "商家操作时间")
	private Date shopOperatorTime;


	@Schema(description = "商家操作人")
	private String adminOperator;

	@Schema(description = "商家操作时间")
	private Date adminOperatorTime;

	@Schema(description = "售后号")
	private String refundSn;

	/**
	 * 售后数量
	 */
	@Schema(description = "售后数量")
	private String goodsNum;

	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String productName;

	/**
	 * 商品图片
	 */
	@Schema(description = "商品图片")
	private String productImage;

	/**
	 * 申请时间
	 */
	@Schema(description = "卖家处理时间")
	private Date sellerTime;

	/**
	 * 申请时间
	 */
	@Schema(description = "管理员处理时间")
	private Date adminTime;


	/**
	 * 申请时间
	 */
	@Schema(description = "申请时间")
	private Date createTime;

	/**
	 * 订单总金额
	 */
	@Schema(description = "订单总金额")
	private String orderMoney;


	/**
	 * 订单SKU ID,全部退款是0
	 */
	@Schema(description = "订单SKU ID,全部退款是0")
	private Long skuId;

	/**
	 * 商品规格属性组合
	 */
	@Schema(description = "商品规格属性组合")
	private String cnProperties;

	/**
	 * 运费
	 */
	@Schema(description = "运费")
	private String freightPrice;

	/**
	 * 运费
	 */
	@Schema(description = "运费")
	private String shippingPrice;

	/**
	 * 退款原因
	 */
	@Schema(description = "取消原因")
	private String reason;

	/**
	 * 卖家备注
	 */
	@Schema(description = "卖家备注")
	private String sellerMessage;

	/**
	 * 退款说明
	 */
	@Schema(description = "退款说明")
	private String buyerMessage;
	/**
	 * 备注
	 */
	@Schema(description = "平台备注")
	private String adminMessage;

	@Schema(description = "备注")
	private String remark;


	@Schema(description = "状态: 待审核（2） 已同意（3）已取消（-1） 已拒绝(-4)")
	private Integer applyStatus;
}
