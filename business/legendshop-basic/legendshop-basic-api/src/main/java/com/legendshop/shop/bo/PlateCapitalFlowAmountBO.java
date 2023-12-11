/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.bo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Schema(description = "平台金额BO")
@Data
public class PlateCapitalFlowAmountBO {

	@Schema(description = "平台总收入")
	private BigDecimal sumIncomeAmount;

	@Schema(description = "平台总支出")
	private BigDecimal sumSpendAmount;

	@Schema(description = "平台总金额")
	private BigDecimal sumPlateAmount;
}
