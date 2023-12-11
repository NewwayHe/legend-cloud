/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 平台批量确认取消订单DTO
 *
 * @author legendshop
 */
@Data
public class ConfirmRefundCanelListDTO {

	@Schema(description = "平台备注")
	private String adminMessage;

	@Schema(description = "取消订单DTO")
	private List<ConfirmRefundDTO> confirmRefundDTO;

	@NotNull(message = "是否同意不能为空")
	@Schema(description = "是否同意")
	private boolean agree;

	@Schema(description = "平台操作人")
	private String adminOperator;


}
