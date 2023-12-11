/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.activity.dto;

import com.legendshop.activity.enums.MarketingTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author legendshop
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityShopUsageDTO {

	/**
	 * {@link MarketingTypeEnum}
	 */
	@Schema(description = "活动类型")
	private Integer type;

	@Schema(description = "饼图：时间段总发布次数")
	private Integer usage;


}
