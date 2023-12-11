/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import com.legendshop.order.enums.OrderTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author legendshop
 */
@Data
@Schema(description = "确认订单入参DTO")
@Accessors(chain = true)
public class ConfirmOrderDTO implements Serializable {

	private static final long serialVersionUID = 1577171876357522986L;

	@Schema(description = "用户选择的收货地址Id")
	private Long addressId;


	@Schema(description = "是否立即购买，普通下单传递")
	private Boolean buyNowFlag;


	@Schema(description = "确认订单商品信息集合", required = true)
	private List<ConfirmOrderItemDTO> confirmOrderItemDTOList;


	@Schema(description = "订单类型 ORDINARY:普通订单,PRE_SALE:预售订单", allowableValues = "ORDINARY,PRE_SALE", required = true)
	private OrderTypeEnum orderType;


	@Schema(description = "营销活动ID[秒杀、团购、拼团、拍卖],普通下单不需要传递")
	private Long activityId;

	@Schema(description = "拼团编号，用户参与拼团活动必传，用户是开团或者是普通下单则不需要传递")
	private String groupNumber;

	@Schema(description = "用户ID", hidden = true)
	private Long userId;

	@Schema(description = "开启抵扣（普通订单的）")
	private Boolean deductionFlag;

	@Schema(description = "物料URL")
	private String materialUrl;

	public ConfirmOrderDTO() {
		this.deductionFlag = Boolean.FALSE;
	}
}
