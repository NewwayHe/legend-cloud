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
import com.legendshop.common.core.annotation.EnumValid;
import com.legendshop.order.enums.OrderTypeEnum;
import com.legendshop.order.enums.ShopOrderBillOrderTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 订单查询
 *
 * @author legendshop
 */
@Data
@Schema(description = "订单查询")
public class ShopOrderBillOrderQuery extends PageParams {

	private static final long serialVersionUID = -7200120244505572555L;

	@Schema(description = "订单编号")
	private String orderNumber;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Schema(description = "下单开始时间")
	private String orderStartTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Schema(description = "下单结束时间")
	private String orderEndTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Schema(description = "支付开始时间")
	private String payStartTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Schema(description = "支付结束时间")
	private String payEndTime;

	@NotNull(message = "账单ID不能为空")
	@Schema(description = "账单ID")
	private Long id;

	@Schema(description = "商家ID", hidden = true)
	private Long shopId;

	/**
	 * 订单结算编号
	 */
	@Schema(description = "结算编码", hidden = true)
	private String sn;

	@EnumValid(target = ShopOrderBillOrderTypeEnum.class)
	@Schema(description = "查询类型 1、订单金额  2、退款金额 3、分销佣金 4、预售定金 5、拍卖保证金")
	private Integer orderType;

	/**
	 * 订单类型
	 * {@link OrderTypeEnum}
	 */
	@Schema(description = "订单类型 [O:普通订单， P：预售订单,G:团购订单,S:秒杀订单,MG:拼团订单,I积分订单]")
	private String oType;
}
