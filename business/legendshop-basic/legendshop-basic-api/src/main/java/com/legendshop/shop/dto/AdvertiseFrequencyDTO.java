/*
 * Copyright (c) 2015-2999 广州朗尊软件科技有限公司<www.legendshop.cn> All rights reserved.
 *
 * https://www.legendshop.cn/
 *
 * 版权所有,并保留所有权利
 *
 */
package com.legendshop.shop.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * (Advertise)DTO
 *
 * @author legendshop
 * @since 2022-04-27 15:23:37
 */
@Data
@Schema(description = "广告频率DTO")
public class AdvertiseFrequencyDTO {

	/**
	 * 广告频率类型
	 */
	@Schema(description = "广告频率类型")
	private String type;

	/**
	 * 广告投放次数
	 */
	@Schema(description = "广告投放次数")
	private String advertiseCount;
}
