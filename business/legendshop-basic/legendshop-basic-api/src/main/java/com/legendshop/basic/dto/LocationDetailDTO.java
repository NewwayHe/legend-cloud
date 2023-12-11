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
 * @author legendshop
 */
@Data
@Schema(description = "地址详情DTO")
public class LocationDetailDTO {

	@Schema(description = "省份id")
	private Long provinceId;

	@Schema(description = "城市id")
	private Long cityId;

	@Schema(description = "地区id")
	private Long areaId;

	@Schema(description = "街道id")
	private Long streetId;

	@Schema(description = "详细地址")
	private String detailAddress;

}
