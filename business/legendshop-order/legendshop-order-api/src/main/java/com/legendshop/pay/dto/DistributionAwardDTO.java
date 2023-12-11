/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.pay.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.legendshop.common.core.serialize.BigDecimalSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author legendshop
 * @version 1.0.0
 * @title DistributionAwardDTO
 * @date 2022/4/1 18:25
 * @description：
 */
@Data
public class DistributionAwardDTO implements Serializable {

	private static final long serialVersionUID = -7730905739820555047L;

	@Schema(description = "时间分组")
	private String dateGroup;


	@Schema(description = "时间分组标识")
	private Integer dateGroupId;

	@JsonSerialize(using = BigDecimalSerialize.class)
	@Schema(description = "收入")
	private BigDecimal incomeAmount;


	@Schema(description = "奖励记录")
	private List<DistributionWalletDetailDTO> commissionRecordDetailList;

	public DistributionAwardDTO() {
	}

	public DistributionAwardDTO(String dateGroup, Integer dateGroupId, BigDecimal incomeAmount, List<DistributionWalletDetailDTO> commissionRecordList) {
		this.dateGroup = dateGroup;
		this.dateGroupId = dateGroupId;
		this.incomeAmount = incomeAmount;
		this.commissionRecordDetailList = commissionRecordList;
	}
}
