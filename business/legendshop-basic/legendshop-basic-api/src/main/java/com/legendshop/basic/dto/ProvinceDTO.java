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

import java.io.Serializable;
import java.util.List;

/**
 * 省
 *
 * @author legendshop
 */
@Data
@Schema(description = "省")
public class ProvinceDTO implements Serializable {

	private static final long serialVersionUID = 1427290507548500122L;

	@Schema(description = "地区ID")
	private Long id;

	@Schema(description = "地区编码")
	private String code;

	@Schema(description = "地区名")
	private String name;

	@Schema(description = "城市")
	private List<CityDTO> children;

}
