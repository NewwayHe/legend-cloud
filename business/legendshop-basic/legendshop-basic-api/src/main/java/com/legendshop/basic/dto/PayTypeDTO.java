/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.basic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 支付方式
 *
 * @author legendshop
 */
@Data
@Schema(description = "支付方式")
public class PayTypeDTO {


	@Schema(description = "支付方式标识ID")
	private String payTypeId;

	@Schema(description = "支付方式名称")
	private String payTypeName;

	@Schema(description = "备注")
	private String memo;

}
