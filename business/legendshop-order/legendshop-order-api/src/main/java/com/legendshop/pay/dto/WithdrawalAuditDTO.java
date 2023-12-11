/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author legendshop
 */
@Data
public class WithdrawalAuditDTO implements Serializable {
	@NotNull(message = "ID不能为空")
	@Schema(description = "ID")
	private List<Long> id;

	@NotNull(message = "操作不能为空 ,1 未审核通过")
	@Schema(description = "操作")
	private int opinion;

	@Schema(description = "原因")
	private String refuseReason;
}
