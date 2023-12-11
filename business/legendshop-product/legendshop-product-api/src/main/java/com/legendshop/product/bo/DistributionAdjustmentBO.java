/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.product.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 暂时调整比例
 *
 * @author legendshop
 * @create: 2021-05-19 15:01
 */
@Data
public class DistributionAdjustmentBO implements Serializable {

	private static final long serialVersionUID = -4588811201244447533L;

	@Schema(description = "ID")
	private Long id;

	/**
	 * 有效开始时间
	 */
	@Schema(description = "有效开始时间")
	private Date limitTimeStart;

	/**
	 * 有效结束时间
	 */
	@Schema(description = "有效结束时间")
	private Date limitTimeEnd;
}
