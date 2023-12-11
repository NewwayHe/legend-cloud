/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;

import com.legendshop.common.core.annotation.EnumValid;
import com.legendshop.pay.enums.DistributionWithdrawOpStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @author legendshop
 */
@Data
public class DistributionWalletAuditDTO {

	@Schema(description = "提现记录id")
	private List<Long> idList;

	@EnumValid(target = DistributionWithdrawOpStatusEnum.class, message = "审核状态不正确！")
	@Schema(description = "审核状态")
	private Integer opStatus;

	@Schema(description = "审核意见")
	private String remark;
}
