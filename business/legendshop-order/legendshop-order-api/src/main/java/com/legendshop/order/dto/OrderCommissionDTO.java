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
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
@Schema(description = "分销信息详情DTO")
public class OrderCommissionDTO {


	@Schema(description = "分销员用户ID(佣金获得者)")
	private Long userId;

	@Schema(description = "分销员昵称")
	private String nickName;

	@Schema(description = "分销员手机")
	private String mobile;

	@Schema(description = "分佣层级 0 ：自购佣金  1：二级分佣  2：三级分佣 3平台佣金")
	private Integer commissionGrade;

	@Schema(description = "预估佣金")
	private BigDecimal estimatedCommission;

	@Schema(description = "结算佣金")
	private BigDecimal settlementCommission;

	@Schema(description = "佣金类型")
	private String commissionType;
}
