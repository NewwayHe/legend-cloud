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
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 退款退货搜索对象
 *
 * @author legendshop
 */
@Schema(description = "取消原因列表列表参数")
@Data
public class OrderCancelReasonQuery extends PageParams {

	/**
	 * 订单id
	 */
	@Schema(description = "订单id")
	private Long orderId;

	/**
	 * 店铺id
	 **/
	@Schema(description = "店铺id")
	private Long shopId;

	/**
	 * 订单号
	 */
	@Schema(description = "订单编号")
	private String orderNumber;


	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String productName;

	/**
	 * 取消原因
	 */
	@Schema(description = "取消原因")
	private String reason;


	/**
	 * 开始时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@Schema(description = "开始时间")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@Schema(description = "结束时间")
	private Date endTime;

	/**
	 * 卖家处理状态
	 */
	@Schema(description = "卖家处理状态")
	private Integer sellerState;


	@Schema(description = "状态: 待审核（2） 已同意（3）已取消（-1） 已拒绝(-4)")
	private Integer applyStatus;


	@Schema(description = "创建来源  0 用户 1 商家 2 平台',")
	private Integer refundSource;

	@Schema(description = "申请类型:1,仅退款,2退款退货,-1已撤销,3 商家申请取消")
	private Integer applyType;


}
