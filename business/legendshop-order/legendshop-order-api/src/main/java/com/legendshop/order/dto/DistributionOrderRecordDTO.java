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

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author legendshop
 */
@Data
public class DistributionOrderRecordDTO implements Serializable {

	@Schema(description = "分销商品金额")
	private BigDecimal allProductAmount;

	@Schema(description = "分销商品数")
	private Integer allProductNum;

	@Schema(description = "分销单数")
	private Integer allOrderNum;

}
