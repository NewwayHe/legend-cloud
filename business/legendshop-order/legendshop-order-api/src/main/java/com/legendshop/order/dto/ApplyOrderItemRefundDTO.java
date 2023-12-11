/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import cn.hutool.json.JSONUtil;
import com.legendshop.common.core.annotation.EnumValid;
import com.legendshop.order.enums.OrderRefundTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 申请订单项退货/退款DTO
 *
 * @author legendshop
 */
@Schema(description = "申请订单项退货/退款DTO")
@Data
public class ApplyOrderItemRefundDTO implements Serializable {

	private static final long serialVersionUID = 635466333513382942L;

	@Schema(description = "订单项id列表", required = true)
	@NotEmpty(message = "订单项id不能为空")
	private List<Long> orderItemIds;

	@Schema(description = "退款原因", required = true)
	@NotEmpty(message = "退款原因不能为空")
	private String reason;

	@Schema(description = "退款说明")
	private String buyerMessage;

	@Schema(description = "退款凭证")
	private String photoVoucher;


	@Schema(description = "退款金额")
	private BigDecimal refundAmount;

	@EnumValid(target = OrderRefundTypeEnum.class)
	@Schema(description = "售后类型")
	private Integer refundType;
	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 订单id
	 */
	@NotNull(message = "订单id不能为空")
	@Schema(description = "订单id")
	private Long orderId;

	public void setPhotoVoucher(List<String> photoVoucherList) {
		this.photoVoucher = JSONUtil.toJsonStr(photoVoucherList);
	}
}
