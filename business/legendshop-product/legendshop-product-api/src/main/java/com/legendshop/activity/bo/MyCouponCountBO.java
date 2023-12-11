/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author legendshop
 */
@Data
public class MyCouponCountBO {

	@Schema(description = "可用优惠券数量")
	private Integer availableCount;

	@Schema(description = "已用优惠券数量")
	private Integer usedCount;

	@Schema(description = "已过期优惠券数量")
	private Integer expireCount;
}
