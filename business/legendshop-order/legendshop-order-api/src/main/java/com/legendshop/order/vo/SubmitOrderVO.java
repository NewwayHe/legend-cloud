/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.vo;

import com.legendshop.order.dto.SubmitOrderShopDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 提交订单的VO
 *
 * @author legendshop
 */
@Schema(description = "提交订单VO")
@Data
public class SubmitOrderVO implements Serializable {

	private static final long serialVersionUID = 7544259870032598139L;

	@Schema(description = "防止重复提交", requiredMode = Schema.RequiredMode.REQUIRED)
	private String token;

	@Schema(description = "预生成订单ID，唯一标识", requiredMode = Schema.RequiredMode.REQUIRED)
	private String id;

	@Schema(description = "订单类型", requiredMode = Schema.RequiredMode.REQUIRED)
	private String type;

	@Schema(description = "收货人信息Id")
	private String addressId;

	@Schema(description = "订单集合", requiredMode = Schema.RequiredMode.REQUIRED)
	private List<SubmitOrderShopDTO> orderList;

	@Schema(description = "下单选择平台优惠劵(红包)集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
	private List<Long> platformCouponIds;

	@Schema(description = "减去优惠后的最终总金额")
	private BigDecimal totalAmount;

	@Schema(description = "运费金额")
	private BigDecimal deliveryAmount;

	@Schema(description = "优惠金额")
	private BigDecimal discountAmount;
}
