/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.user.dto;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.legendshop.common.core.serialize.BigDecimalSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 客户账单分组DTO
 *
 * @author legendshop
 */
@Schema(description = "客户账单分组DTO")


@Data
public class CustomerBillGroupDTO implements Serializable {


	@Schema(description = "时间分组")
	private String dateGroup;


	@Schema(description = "时间分组标识")
	private Integer dateGroupId;


	@Schema(description = "支出")
	@JsonSerialize(using = BigDecimalSerialize.class)
	private BigDecimal expenditureAmount;

	@JsonSerialize(using = BigDecimalSerialize.class)
	@Schema(description = "收入")
	private BigDecimal incomeAmount;


	@Schema(description = "客户账单")
	private List<CustomerBillDTO> customerBillDTOList;

	public CustomerBillGroupDTO() {
	}

	public CustomerBillGroupDTO(String dateGroup, Integer dateGroupId, BigDecimal expenditureAmount, BigDecimal incomeAmount, List<CustomerBillDTO> customerBillDTOList) {
		this.dateGroup = dateGroup;
		this.dateGroupId = dateGroupId;
		this.expenditureAmount = expenditureAmount;
		this.incomeAmount = incomeAmount;
		this.customerBillDTOList = customerBillDTOList;
	}
}
